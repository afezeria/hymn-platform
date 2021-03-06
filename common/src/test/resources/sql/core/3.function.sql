-- 触发器以a开头的表示通用函数的触发器
-- 以c10开头的表示特定表的函数构建的触发器
-- 以c开头接其他数字的表示根据特定字段值区分的特定表的函数构建的触发器

-- biz_object_field 中字段的 source_column 如果以 pl_ 开头表示该字段并不对应数据表中的某一列,
-- 该列的相关数据由应用层生成然后展示给前端

-- 授予 hymn_user 角色业务对象视图的相关权限
create or replace function hymn.grant_object_view_permission(obj_id text) returns void
    language plpgsql as
$$
declare
    obj       hymn.core_biz_object;
    perm_text text := 'select';
    sql_str   text;
begin
    select * into obj from hymn.core_biz_object where id = obj_id;
    if not found then
        raise exception '[f:inner:00100] 对象 [id:%] 不存在',obj_id;
    end if;
    perform *
    from pg_class pc
             join pg_namespace pn on pn.oid = pc.relnamespace
    where pn.nspname = 'hymn_view'
      and pc.relkind = 'v'
      and pc.relname = obj.api;
    if not FOUND then
        raise exception '[f:inner:00200] 视图 % 不存在',obj.api;
    end if;
    if obj.type in ('custom', 'module') then
        if obj.can_insert then
            perm_text := perm_text || ',insert';
        end if;
        if obj.can_update then
            perm_text := perm_text || ',update';
        end if;
        if obj.can_delete then
            perm_text := perm_text || ',delete';
        end if;
        sql_str := format('grant %s on hymn_view.%I to hymn_user;', perm_text, obj.api);
        execute sql_str;
    end if;
end;
$$;
-- 授予 hymn_user 角色表连接视图的权限
create or replace function hymn.grant_join_view_permission(view_name text) returns void
    language plpgsql as
$$
declare
    sql_str text;
begin
    perform *
    from pg_class pc
             join pg_namespace pn on pn.oid = pc.relnamespace
    where pn.nspname = 'hymn_view'
      and pc.relkind = 'v'
      and pc.relname = view_name;
    if not FOUND then
        raise exception '[f:inner:00300] 视图 % 不存在',view_name;
    end if;
    sql_str := format('grant select,insert,update,delete on hymn_view.%I to hymn_user;', view_name);
    execute sql_str;
end;
$$;


-- 缓存表相关函数
create or replace function hymn.get_cache(f_group text, f_pattern text) returns setof hymn.core_cache
    language plpgsql as
$$
declare
    row hymn.core_cache;
begin
    for row in select *
               from hymn.core_cache
               where c_group = f_group
                 and c_key like f_pattern for update
        loop
            if now() > row.last_time + row.expiry * interval '1 second' then
                delete from hymn.core_cache where c_key = row.c_key;
            else
                return next row;
            end if;
        end loop;
    return;
end;
$$;

create or replace function hymn.set_cache(f_group text, f_key text, f_value text, f_expiry bigint) returns bool
    language plpgsql as
$$
declare
    is_new_cache bool;
begin
    insert into hymn.core_cache (c_group, c_key, c_value, last_time, expiry)
    values (f_group, f_key, f_value, now(), f_expiry)
    on conflict (c_group,c_key) do update set c_value=excluded.c_value,
                                              expiry = excluded.expiry,
                                              last_time=now()
    returning new_cache into is_new_cache;
    return is_new_cache;
end;
$$;
create or replace function hymn.remove_cache(f_group text, f_key text) returns int
    language plpgsql as
$$
declare
    count int;
begin
    delete from hymn.core_cache where c_group = f_group and c_key = f_key;
    get diagnostics count= row_count;
    return count;
end;
$$;
comment on function hymn.remove_cache is '删除缓存并返回受影响行数';
create or replace function hymn.core_cache_trigger_fun_before_insert_or_update() returns trigger
    language plpgsql as
$$
declare
    record_old hymn.core_cache := old;
    record_new hymn.core_cache := new;
begin
    if tg_op = 'UPDATE' then
        if now() > (record_old.last_time + record_old.expiry * interval '1 second') then
            record_new.new_cache = true;
        else
            record_new.new_cache = false;
        end if;
    elseif tg_op = 'INSERT' then
        record_new.new_cache = true;
    end if;
    return record_new;
end;
$$;
comment on function hymn.core_cache_trigger_fun_before_insert_or_update is 'core_cache表更新/插入触发器函数，如果缓存已存在且未过期则设置new_cache字段为false，否则为true';
create trigger c10_core_cache_before_insert_or_update
    before insert or update
    on hymn.core_cache
    for each row
execute function hymn.core_cache_trigger_fun_before_insert_or_update();



-- 阻止改变所属对象id
create or replace function hymn.cannot_change_biz_object_id() returns trigger
    language plpgsql as
$$
begin
    if old.biz_object_id != new.biz_object_id then
        raise exception '[f:inner:00400] 不能修改所属业务对象';
    end if;
    return new;
end;
$$;
comment on function hymn.cannot_change_biz_object_id() is '不能修改业务对象相关数据的对象id';
drop trigger if exists a00_cannot_change_biz_object_id on hymn.core_biz_object_field;
create trigger a00_cannot_change_biz_object_id
    before update
    on hymn.core_biz_object_field
    for each row
execute function hymn.cannot_change_biz_object_id();
drop trigger if exists a00_cannot_change_biz_object_id on hymn.core_biz_object_type;
create trigger a00_cannot_change_biz_object_id
    before update
    on hymn.core_biz_object_type
    for each row
execute function hymn.cannot_change_biz_object_id();
drop trigger if exists a00_cannot_change_biz_object_id on hymn.core_biz_object_type_layout;
create trigger a00_cannot_change_biz_object_id
    before update
    on hymn.core_biz_object_type_layout
    for each row
execute function hymn.cannot_change_biz_object_id();
drop trigger if exists a00_cannot_change_biz_object_id on hymn.core_biz_object_layout;
create trigger a00_cannot_change_biz_object_id
    before update
    on hymn.core_biz_object_layout
    for each row
execute function hymn.cannot_change_biz_object_id();
drop trigger if exists a00_cannot_change_biz_object_id on hymn.core_biz_object_trigger;
create trigger a00_cannot_change_biz_object_id
    before update
    on hymn.core_biz_object_trigger
    for each row
execute function hymn.cannot_change_biz_object_id();

create or replace function hymn.cannot_change_api() returns trigger
    language plpgsql as
$$
begin
    if old.api != new.api then
        raise exception '[f:inner:00500] 不能修改api';
    end if;
    return new;
end;
$$;
comment on function hymn.cannot_change_api() is '不能修改api';
drop trigger if exists a20_cannot_change_api on hymn.core_biz_object;
create trigger a20_cannot_change_api
    before update
    on hymn.core_biz_object
    for each row
execute function hymn.cannot_change_api();
drop trigger if exists a20_cannot_change_api on hymn.core_biz_object_field;
create trigger a20_cannot_change_api
    before update
    on hymn.core_biz_object_field
    for each row
execute function hymn.cannot_change_api();
drop trigger if exists a20_cannot_change_api on hymn.core_biz_object_trigger;
create trigger a20_cannot_change_api
    before update
    on hymn.core_biz_object_trigger
    for each row
execute function hymn.cannot_change_api();
drop trigger if exists a20_cannot_change_api on hymn.core_custom_button;
create trigger a20_cannot_change_api
    before update
    on hymn.core_custom_button
    for each row
execute function hymn.cannot_change_api();
drop trigger if exists a20_cannot_change_api on hymn.core_custom_component;
create trigger a20_cannot_change_api
    before update
    on hymn.core_custom_component
    for each row
execute function hymn.cannot_change_api();
drop trigger if exists a20_cannot_change_api on hymn.core_custom_api;
create trigger a20_cannot_change_api
    before update
    on hymn.core_custom_api
    for each row
execute function hymn.cannot_change_api();
drop trigger if exists a20_cannot_change_api on hymn.core_custom_page;
create trigger a20_cannot_change_api
    before update
    on hymn.core_custom_page
    for each row
execute function hymn.cannot_change_api();
drop trigger if exists a20_cannot_change_api on hymn.core_dict;
create trigger a20_cannot_change_api
    before update
    on hymn.core_dict
    for each row
execute function hymn.cannot_change_api();
drop trigger if exists a20_cannot_change_api on hymn.core_module_function;
create trigger a20_cannot_change_api
    before update
    on hymn.core_module_function
    for each row
execute function hymn.cannot_change_api();
drop trigger if exists a20_cannot_change_api on hymn.core_custom_function;
create trigger a20_cannot_change_api
    before update
    on hymn.core_custom_function
    for each row
execute function hymn.cannot_change_api();



-- 对象停用时禁止插入/更新
create or replace function hymn.cannot_upsert_when_object_is_inactive() returns trigger
    language plpgsql as
$$
declare
    obj hymn.core_biz_object;
begin
    select * into obj from hymn.core_biz_object where id = new.biz_object_id;
    if not FOUND then
        raise exception '[f:inner:00600] 对象 [id:%] 不存在',new.biz_object_id;
    end if;
    if not obj.active then
        raise exception '[f:inner:00700] 对象 [id:%] 已停用，不能新增/更新相关数据',new.biz_object_id;
    end if;
    return new;
end;
$$;
comment on function hymn.cannot_upsert_when_object_is_inactive() is '检查当前数据所属的对象是否已停用，对象处于停用状态时不能修改相关数据（insert，update触发器）';
drop trigger if exists a10_check_object_active_status_upsert on hymn.core_biz_object_field;
create trigger a10_check_object_active_status_upsert
    before insert or update
    on hymn.core_biz_object_field
    for each row
execute function hymn.cannot_upsert_when_object_is_inactive();
drop trigger if exists a10_check_object_active_status_upsert on hymn.core_biz_object_layout;
create trigger a10_check_object_active_status_upsert
    before insert or update
    on hymn.core_biz_object_layout
    for each row
execute function hymn.cannot_upsert_when_object_is_inactive();
drop trigger if exists a10_check_object_active_status_upsert on hymn.core_biz_object_type_layout;
create trigger a10_check_object_active_status_upsert
    before insert or update
    on hymn.core_biz_object_type_layout
    for each row
execute function hymn.cannot_upsert_when_object_is_inactive();
drop trigger if exists a10_check_object_active_status_upsert on hymn.core_biz_object_type;
create trigger a10_check_object_active_status_upsert
    before insert or update
    on hymn.core_biz_object_type
    for each row
execute function hymn.cannot_upsert_when_object_is_inactive();
drop trigger if exists a10_check_object_active_status_upsert on hymn.core_biz_object_trigger;
create trigger a10_check_object_active_status_upsert
    before insert or update
    on hymn.core_biz_object_trigger
    for each row
execute function hymn.cannot_upsert_when_object_is_inactive();

-- 对象停用时禁止删除相关数据
create or replace function hymn.cannot_delete_when_object_is_inactive() returns trigger
    language plpgsql as
$$
declare
    obj hymn.core_biz_object;
begin
    select * into obj from hymn.core_biz_object where id = old.biz_object_id;
    if not obj.active then
        raise exception '[f:inner:00800] 对象 [id:%] 已停用，不能删除相关数据',new.biz_object_id;
    end if;
    return old;
end;
$$;
comment on function hymn.cannot_delete_when_object_is_inactive() is '检查当前数据所属的对象是否已停用，对象处于停用状态时不能修改相关数据（delete触发器）';
-- drop trigger if exists a11_check_object_active_status_delete on hymn.core_biz_object_field;
-- create trigger a11_check_object_active_status_delete
--     before delete
--     on hymn.core_biz_object_field
--     for each row
-- execute function hymn.cannot_delete_when_object_is_inactive();
drop trigger if exists a11_check_object_active_status_delete on hymn.core_biz_object_layout;
create trigger a11_check_object_active_status_delete
    before delete
    on hymn.core_biz_object_layout
    for each row
execute function hymn.cannot_delete_when_object_is_inactive();
drop trigger if exists a11_check_object_active_status_delete on hymn.core_biz_object_type_layout;
create trigger a11_check_object_active_status_delete
    before delete
    on hymn.core_biz_object_type_layout
    for each row
execute function hymn.cannot_delete_when_object_is_inactive();
drop trigger if exists a11_check_object_active_status_delete on hymn.core_biz_object_type;
create trigger a11_check_object_active_status_delete
    before delete
    on hymn.core_biz_object_type
    for each row
execute function hymn.cannot_delete_when_object_is_inactive();
drop trigger if exists a11_check_object_active_status_delete on hymn.core_biz_object_trigger;
create trigger a11_check_object_active_status_delete
    before delete
    on hymn.core_biz_object_trigger
    for each row
execute function hymn.cannot_delete_when_object_is_inactive();

-- 工具方法
create or replace function hymn.throw_if_api_is_illegal(api_name text, api_value text) returns void
    language plpgsql as
$$
declare
begin
    if api_value not similar to '[a-zA-Z][a-zA-Z0-9_]{1,22}' then
        raise exception '[f:inner:00900] 无效的 %, 值必须匹配正则: [a-zA-Z][a-zA-Z0-9_]{1,22}',api_name;
    end if;
    perform keyword from hymn.sql_keyword where keyword = lower(api_value);
    if FOUND then
        raise exception '[f:inner:01000] 无效的 %, % 是数据库关键字',api_name,api_value;
    end if;
end;
$$;
comment on function hymn.throw_if_api_is_illegal(api_name text, api_value text) is '检查api是否合法，不合法时抛出异常，api必须匹配 [a-zA-Z][a-zA-Z0-9_]{1,24} ，且不能为数据库关键字';

create or replace function hymn.reset_increment_seq_of_data_table(obj_id text) returns void
    language plpgsql as
$$
declare
    seq_name text;
    obj      hymn.core_biz_object;
begin
    select * into obj from hymn.core_biz_object scbo where id = obj_id;
    if not FOUND then
        raise exception '[f:inner:01100] 对象 [id:%] 不存在',obj_id;
    end if;
    if obj.source_table not like 'core_data_table_%' or obj.type <> 'custom' then
        raise exception '[f:inner:01200] 非自定义对象需不能自动重置递增序列';
    end if;
    seq_name := 'hymn.' || obj.source_table || '_seq';
    perform setval(seq_name, 1, false);
end;
$$;
comment on function hymn.reset_increment_seq_of_data_table(obj_id text) is '重置业务对象用于自动编号的自增序列';

create or replace function hymn.rebuild_object_view(obj_id text) returns void
    language plpgsql as
$$
declare
    obj        hymn.core_biz_object;
    field      hymn.core_biz_object_field;
    column_arr text[] := ARRAY [' id as id '];
begin
    select * into obj from hymn.core_biz_object where id = obj_id;
    if FOUND then
        if obj.type = 'custom' or (obj.type = 'module' and obj.can_soft_delete = true) then
            column_arr := array_append(column_arr, ' deleted as deleted ');
        end if;
        for field in select *
                     from hymn.core_biz_object_field
                     where active = true
                       and source_column not similar to 'pl_%'
                       and biz_object_id = obj_id
                     order by create_date
            loop
                column_arr := array_append(column_arr,
                                           format(' %I as %I ', field.source_column, field.api));
            end loop;
        execute format('drop view if exists hymn_view.%I', obj.api);
        execute format('create view hymn_view.%I as select %s from hymn.%I', obj.api,
                       array_to_string(column_arr, ','),
                       obj.source_table);
        perform hymn.grant_object_view_permission(obj.id);
    end if;
end;
$$;
comment on function hymn.rebuild_object_view(obj_id text) is '重建对象视图';

create or replace function hymn.field_type_2_column_prefix(field_type text) returns text
    language plpgsql
    strict immutable as
$$
begin
    case field_type
        when 'text', 'check_box_group', 'select', 'reference', 'auto'
            then return 'text';
        when 'master_slave' then return 'master';
        when 'check_box' then return 'bool';
        when 'integer' then return 'bigint';
        when 'float' then return 'double';
        when 'picture' then return 'picture';
        when 'files' then return 'files';
        when 'money' then return 'decimal';
        when 'datetime','date' then return 'datetime' ;
        when 'summary' then return 'pl_summary';
        when 'mreference' then return 'mref';
        when 'areference' then return 'aref';
        else raise exception '[f:inner:01300] 未知的字段类型 %',field_type;
    end case;
end ;
$$;
comment on function hymn.field_type_2_column_prefix(field_type text) is '根据字段类型获取对应的sql列名前缀';

create or replace function hymn.column_prefix_2_field_type_description(prefix text) returns text
    language plpgsql
    strict immutable as
$$
begin
    case prefix
        when 'text' then return '文本/复选框组/下拉/关联/自动编号';
        when 'bool' then return '复选框';
        when 'bigint' then return '整型';
        when 'double' then return '浮点型';
        when 'decimal' then return '金额';
        when 'picture' then return '图片';
        when 'files' then return '文件';
        when 'datetime' then return '日期/日期时间';
        when 'master' then return '主从';
        when 'mref' then return '多选关联';
        when 'aref' then return '任意关联';
        when 'pl_summary' then return '汇总';
        else raise exception '[f:inner:01400] 未知的列名前缀: %',prefix;
    end case;
end;
$$;
comment on function hymn.column_prefix_2_field_type_description(prefix text) is '根据列名前缀获取可能的所有字段类型的表述';

create or replace function hymn.field_type_description(field_type text) returns text
    language plpgsql
    strict immutable as
$$
begin
    case field_type
        when 'text' then return '文本';
        when 'check_box' then return '复选框';
        when 'check_box_group' then return '复选框组';
        when 'select' then return '下拉';
        when 'master_slave' then return '主从';
        when 'reference' then return '关联';
        when 'mreference' then return '多选关联';
        when 'areference' then return '任意关联';
        when 'summary' then return '汇总';
        when 'auto' then return '自动编号';
        when 'picture' then return '图片';
        when 'files' then return '文件';
        when 'integer' then return '整数';
        when 'float' then return '浮点数';
        when 'money' then return '金额';
        when 'datetime' then return '日期时间';
        when 'date' then return '日期';
        else raise exception '[f:inner:01500] 未知的字段类型： %',field_type;
    end case;
end;
$$;

create or replace function hymn.get_remaining_available_object_info() returns int
    language plpgsql as
$$
declare
    n int;
begin
    select count(*)
    into n
    from hymn.core_table_obj_mapping
    where obj_api is null
      and table_name similar to 'core_data_table%';
    return n;
end;
$$;
comment on function hymn.get_remaining_available_object_info() is '获取剩余可创建自定义对象数';

create or replace function hymn.get_remaining_available_field_info(object_api text, out f_type text, out count bigint)
    returns setof record
    language sql
as
$$
select hymn.column_prefix_2_field_type_description(
               substring(ccfm.column_name from '^[a-zA-Z]+')) as type,
       count(*)                                               as count
from hymn.core_column_field_mapping ccfm
         left join hymn.core_biz_object cbo on cbo.source_table = ccfm.table_name
where cbo.api = object_api
  and field_api is null
group by substring(column_name from '^[a-zA-Z]+')
$$;
comment on function hymn.get_remaining_available_field_info(object_api text, out f_type text, out count bigint) is '获取剩余可创建自定义对象数';


create or replace function hymn.get_join_table_name(view_name text) returns text
    language plpgsql
    immutable as

$$
begin
    return 'core_' || view_name;
end;
$$;
comment on function hymn.get_join_table_name(view_name text) is '获取业务对象多选关联字段对应的中间表表名';

create or replace function hymn.get_join_view_name(t_api text, f_api text) returns text
    language plpgsql
    immutable as
$$
begin
    return 'join_' || t_api || '_' || f_api;
end;
$$;
comment on function hymn.get_join_view_name(t_api text, f_api text) is '获取业务对象多选关联字段对应的中间表视图名';


create or replace function hymn.rebuild_multiple_refernece_trigger_function(obj_id text) returns void
    language plpgsql as
$BODY$
declare
    obj              hymn.core_biz_object;
    field            hymn.core_biz_object_field;
    fun_header       text;
    fun_body         text := '';
    fun_tail         text := E'    return new;\nend;\n$$;\n';
    trigger_name     text;
    trigger_fun_name text;
    join_view_name   text;
    sql_str          text;
begin
    select * into obj from hymn.core_biz_object where id = obj_id;
    if not FOUND then
        raise exception '[f:inner:01600] 对象 [id:%] 不存在',obj_id;
    end if;
    if obj.active = false then
        raise exception '[f:inner:01700] 对象 [id:%] 未启用',obj_id;
    end if;
    if obj.type <> 'custom' then
        raise notice '对象 % 不是自定义业对象，不支持创建自动编号字段',obj.api;
        return;
    end if;
    trigger_fun_name := obj.source_table || '_mref_trigger_function';
    trigger_name := 'a20_' || obj.source_table || '_mref_trigger';
    fun_header :=
            format(E'create or replace function hymn.%I() returns trigger\n'
                       '    language plpgsql as\n'
                       '$$\n'
                       'declare\n'
                       '    old_v           text;\n'
                       '    new_v           text;\n'
                       '    old_mref_arr    text[];\n'
                       '    new_mref_arr    text[];\n'
                       '    add_arr         text[];\n'
                       '    remove_arr      text[];\n'
                       '    i               text;\n'
                       '    join_table_name text;\n'
                       '    sql_str         text;\n'
                       'begin\n', trigger_fun_name);
    for field in select *
                 from hymn.core_biz_object_field cbof
                          left join hymn.core_biz_object cbo on cbof.ref_id = cbo.id
                 where cbof.active = true
                   and cbo.active = true
                   and cbof.type = 'mreference'
                   and cbof.biz_object_id = obj_id
        loop
            join_view_name := field.join_view_name;
            fun_body := fun_body ||
                        format(
                                E'    if new.%I is null then\n'
                                    '        new_v = '''';\n'
                                    '    else\n'
                                    '        new_v = new.%I;\n'
                                    '    end if;\n'
                                    '    if old.%I is null then\n'
                                    '        old_v = '''';\n'
                                    '    else\n'
                                    '        old_v = old.%I;\n'
                                    '    end if;\n'
                                    '    if old_v <> new_v then\n'
                                    '        old_mref_arr := array_remove(regexp_split_to_array(old_v, '',''), '''');\n'
                                    '        new_mref_arr := array_remove(regexp_split_to_array(new_v, '',''), '''');\n'
                                    '        if old_v = '' then\n'
                                    '            add_arr = new_mref_arr;\n'
                                    '            remove_arr = null;\n'
                                    '        elsif new_v = '' then\n'
                                    '            remove_arr = old_mref_arr;\n'
                                    '            add_arr = null;\n'
                                    '        else\n'
                                    '            select array_agg(el)\n'
                                    '            into remove_arr\n'
                                    '            from unnest(old_mref_arr) el\n'
                                    '            where el <> all (new_mref_arr);\n'
                                    '            select array_agg(el)\n'
                                    '            into add_arr\n'
                                    '            from unnest(new_mref_arr) el\n'
                                    '            where el <> all (old_mref_arr);\n'
                                    '        end if;\n'
                                    '        if remove_arr is not null then\n'
                                    '            sql_str := ''delete from hymn_view.%I where s_id=$1 and t_id = any ($2)'';\n'
                                    '            execute sql_str using new.id,remove_arr;\n'
                                    '        end if;\n'
                                    '        if add_arr is not null then\n'
                                    '            sql_str := ''insert into hymn_view.%I (s_id,t_id) values ($1,$2)'';\n'
                                    '            foreach i in array add_arr\n'
                                    '                loop\n'
                                    '                    execute sql_str using new.id,i;\n'
                                    '                end loop;\n'
                                    '        end if;\n'
                                    '    end if;\n'
                            , field.source_column, field.source_column,
                                field.source_column, field.source_column,
                                join_view_name, join_view_name);
        end loop;
--     没有符合条件的字段时尝试删除原触发器和函数
    if fun_body = '' then
        sql_str := format('drop trigger if exists %I on hymn.%I', trigger_name, obj.source_table);
        execute sql_str;
        sql_str := format('drop function if exists hymn.%I()', trigger_fun_name);
        execute sql_str;
        return;
    end if;
    sql_str := fun_header || fun_body || fun_tail;
    execute sql_str;


--     如果触发器不存在则创建触发器，如果触发器已存在则跳过
    perform *
    from pg_trigger pt
             left join pg_class pc on pt.tgrelid = pc.oid
             left join pg_namespace pn on pn.oid = pc.relnamespace
    where pc.relname = obj.source_table
      and pn.nspname = 'hymn'
      and pt.tgname = trigger_name;
    if not FOUND then
        execute format(E'create trigger %s\n'
                           'before insert or update\n'
                           'on hymn.%s\n'
                           'for each row\n'
                           'execute function hymn.%s();\n',
                       trigger_name, obj.source_table, trigger_fun_name);
    end if;
    return;
end ;
$BODY$;
comment on function hymn.rebuild_multiple_refernece_trigger_function(obj_id text) is '重建多选字段触发器，触发器在多选字段值变更时自动修改中间表的数据';


create or replace function hymn.rebuild_auto_numbering_trigger(obj_id text) returns void
    language plpgsql as
$BODY$
declare
    obj                  hymn.core_biz_object;
    field                hymn.core_biz_object_field;
    template_name        text;
    data_table_name      text;
    auto_number_seq_name text;
    fun_header           text;
    fun_body             text := E'begin\n';
    fun_tail             text := E'    return new;\nend;\n$$;\n';
    trigger_name         text;
    trigger_fun_name     text;
    sql_str              text;
begin
    select * into obj from hymn.core_biz_object where id = obj_id;
    if not FOUND then
        raise exception '[f:inner:01800] 对象 [id:%] 不存在',obj_id;
    end if;
    if obj.active = false then
        raise exception '[f:inner:01900] 对象 [id:%] 未启用',obj_id;
    end if;
    if obj.type <> 'custom' then
        raise notice '对象 % 不是自定义业对象，不支持创建自动编号字段',obj.api;
        return;
    end if;


    data_table_name := obj.source_table;
    trigger_fun_name := obj.api || '_auto_number_trigger_fun';
    trigger_name := 'a10_' || obj.api || '_auto_number';
    auto_number_seq_name := obj.api || '_auto_number_seq';

    perform *
    from pg_class pc
             left join pg_namespace pn on pn.oid = pc.relnamespace
    where pn.nspname = 'hymn_view'
      and pc.relkind = 'S'
      and pc.relname = auto_number_seq_name;
    if not FOUND then
        sql_str := format('create sequence hymn_view.%I start 1', auto_number_seq_name);
        execute sql_str;
        sql_str :=
                format('grant usage on sequence hymn_view.%I to hymn_user;', auto_number_seq_name);
        execute sql_str;
    end if;

    fun_header := format(
            E'create or replace function hymn.%I() returns trigger\n'
                '   language plpgsql as\n'
                '$$\n'
                'declare\n'
                'seq             text      := nextval(''hymn_view.%s'');\n'
                'now             timestamp := now();\n'
                'year_v            text      := date_part(''year'', now);\n'
                'yy_v              text      := substr(year_v, 3);\n'
                'month_v           text      := date_part(''month'', now);\n'
                'day_v             text      := date_part(''day'', now);\n'
                'hour_v            text      := date_part(''hour'', now);\n'
                'minute_v          text      := date_part(''minute'', now);\n'
                'size              int;\n'
                'tmp             text;\n', trigger_fun_name, auto_number_seq_name);

    for field in select *
                 from hymn.core_biz_object_field
                 where biz_object_id = obj_id
                   and active = true
                   and type = 'auto'
        loop
            template_name := field.source_column || '_template';
            fun_header := fun_header ||
                          format(E'%s text := %L;\n', template_name, field.gen_rule);

            fun_body := fun_body ||
                        format(E'%s := replace(%s, ''{yyyy}'', year_v);\n'
                                   '%s := replace(%s, ''{yy}'', yy_v);\n'
                                   '%s := replace(%s, ''{mm}'', month_v);\n'
                                   '%s := replace(%s, ''{dd}'', day_v);\n'
                                   '%s := replace(%s, ''{hh}'', hour_v);\n'
                                   '%s := replace(%s, ''{mm}'', minute_v);\n'
                                   'for tmp in select (regexp_matches(%s, ''\{(0+)\}'', ''g''))[1]\n'
                                   '    loop\n'
                                   '        size := length(tmp);'
                                   '        %s := replace(%s, ''{'' || tmp || ''}'', lpad(seq,size,''0''));\n'
                                   '    end loop;\n'
                                   'new.%s := %s;\n', template_name, template_name,
                               template_name, template_name, template_name,
                               template_name, template_name, template_name,
                               template_name, template_name, template_name,
                               template_name, template_name, template_name,
                               template_name, field.source_column, template_name);
        end loop;
    execute fun_header || fun_body || fun_tail;

--     如果触发器不存在则创建触发器，如果触发器已存在则跳过
    perform *
    from pg_trigger pt
             left join pg_class pc on pt.tgrelid = pc.oid
             left join pg_namespace pn on pn.oid = pc.relnamespace
    where pc.relname = data_table_name
      and pn.nspname = 'hymn'
      and pt.tgname = trigger_name;
    if not FOUND then
        execute format(E'create trigger %s\n'
                           'before insert\n'
                           'on hymn.%s\n'
                           'for each row\n'
                           'execute function hymn.%s();\n',
                       trigger_name, data_table_name, trigger_fun_name);
    end if;
end ;
$BODY$;
comment on function hymn.rebuild_auto_numbering_trigger(obj_id text) is '重建数据表的自动编号触发器，生成触发器函数';


create or replace function hymn.rebuild_data_table_history_trigger(obj_id text) returns void
    language plpgsql as
$BODY$
declare
    obj                hymn.core_biz_object;
    field              hymn.core_biz_object_field;
    data_table_name    text;
    field_api          text;
    field_column       text;
    fun_header         text;
    fun_body           text := '';
    fun_tail           text ;
    trigger_name       text;
    trigger_fun_name   text;
    history_table_name text;
    sql_str            text;
begin
    select * into obj from hymn.core_biz_object where id = obj_id;
    if not FOUND then
        raise exception '[f:inner:02000] 对象 [id:%] 不存在',obj_id;
    end if;
    if obj.active = false then
        raise exception '[f:inner:02100] 对象 [id:%] 未启用',obj_id;
    end if;
    if obj.type <> 'custom' then
        raise notice '对象 % 不是自定义业对象，不执行创建历史记录触发器相关动作',obj.api;
        return;
    end if;
    data_table_name := obj.source_table;
    trigger_fun_name := data_table_name || '_history_trigger_fun';
    trigger_name := 'a99_' || data_table_name || '_history';
    history_table_name := obj.api || '_history';
--     创建历史记录表及索引
    perform *
    from pg_class pc
             left join pg_namespace pn on pc.relnamespace = pn.oid
    where pn.nspname = 'hymn_view'
      and pc.relname = history_table_name;
    if not FOUND then
        sql_str := format('create table hymn_view.%I ' ||
                          '(id text,operation text,stamp timestamptz,change text)',
                          history_table_name);
        execute sql_str;
        sql_str := format('create index on hymn_view.%I (id)', history_table_name);
        execute sql_str;
        sql_str := format('grant select,insert on table hymn_view.%I to hymn_user',
                          history_table_name);
        execute sql_str;
    end if;

    fun_header := format(
            E'create or replace function hymn.%I() returns trigger\n'
                '    language plpgsql as\n'
                '$$\n'
                'declare\n'
                '    js  jsonb := ''{}'';\n'
                '    tmp jsonb := ''{}'';\n'
                '    op  text;\n'
                'begin\n'
                '    if tg_op = ''DELETE'' then\n'
                '        js := to_jsonb(old);\n'
                '        insert into hymn_view.%I (id, operation, stamp, change)\n'
                '        values (old.id, ''d'', now(), js::text);\n'
                '    elseif tg_op = ''UPDATE'' then\n', trigger_fun_name, history_table_name);
    fun_tail :=
            format(E'    end if;\n'
                '    return null;\n'
                'end;\n'
                '$$;\n');
    for field in select *
                 from hymn.core_biz_object_field cbof
                 where cbof.biz_object_id = obj_id
                   and cbof.source_column not like 'pl_%'
                   and cbof.active = true
                   and cbof.history = true
        loop
            field_api := field.api;
            field_column := field.source_column;

            fun_body := fun_body ||
                        format(
                                E'        if old.%s != new.%s then\n'
                                    '            tmp := jsonb_insert(tmp, ''{o}'', to_jsonb(old.%s));\n'
                                    '            tmp := jsonb_insert(tmp, ''{n}'', to_jsonb(new.%s));\n'
                                    '            js := jsonb_insert(js, ''{%s}'', tmp);\n'
                                    '            tmp := ''{}'';\n'
                                    '            op := ''u'';\n'
                                    '        end if;\n',
                                field_column, field_column, field_column, field_column, field_api);
        end loop;
    if obj.type = 'custom' then
        fun_body := fun_body ||
                    E'        if old.deleted = false and new.deleted = true then\n'
                        '            js := jsonb_insert(js, ''{deleted}'', ''{"o": false,"n": true}''::jsonb);\n'
                        '            op := ''sd'';\n'
                        '        elsif old.deleted = true and new.deleted = false then\n'
                        '            js := jsonb_insert(js, ''{deleted}'', ''{"o": true,"n": false}''::jsonb);\n'
                        '            op := ''r'';\n'
                        '        end if;\n';
    end if;

    fun_body := fun_body ||
                format(E'        if op is not null then\n'
                           '            insert into hymn_view.%I (id, operation, stamp, change)\n'
                           '            values (old.id, op , now(), js::text);\n'
                           '        end if;\n',
                       history_table_name);
    execute fun_header || fun_body || fun_tail;

--     如果触发器不存在则创建触发器，如果触发器已存在则跳过
    perform *
    from pg_trigger pt
             left join pg_class pc on pt.tgrelid = pc.oid
             left join pg_namespace pn on pn.oid = pc.relnamespace
    where pc.relname = data_table_name
      and pn.nspname = 'hymn'
      and pt.tgname = trigger_name;
    if not FOUND then
        execute format(E'create trigger %s\n'
                           'after update or delete\n'
                           'on hymn.%s\n'
                           'for each row\n'
                           'execute function hymn.%s();\n',
                       trigger_name, data_table_name, trigger_fun_name);
    end if;
end;
$BODY$;
comment on function hymn.rebuild_data_table_history_trigger(obj_id text) is '重建数据表的历史记录触发器，生成触发器函数';

-- 触发器函数
create or replace function hymn.dict_before_delete_trigger_fun() returns trigger
    language plpgsql as
$$
declare
    record_old hymn.core_dict := old;
    field_arr  text;
begin
    select array_agg(cbo.api || '.' || cbof.api)::text
    into field_arr
    from hymn.core_biz_object_field cbof
             left join hymn.core_biz_object cbo on cbof.biz_object_id = cbo.id
    where cbof.type in ('select', 'check_box_group')
      and cbof.dict_id = record_old.id;
    if field_arr is not null then
        raise exception '[f:inner:02101] 当前字典被以下字段引用，不能删除: %',field_arr;
    end if;
    return record_old;
end;
$$;
comment on function hymn.dict_before_delete_trigger_fun() is '当字典被引用时阻止删除字典';
drop trigger if exists c10_dict_before_delete on hymn.core_dict;
create trigger c10_dict_before_delete
    before delete
    on hymn.core_dict
    for each row
execute function hymn.dict_before_delete_trigger_fun();


-- flag:object 对象相关触发器
create or replace function hymn.object_trigger_fun_before_insert() returns trigger
    language plpgsql as
$$
declare
    record_new hymn.core_biz_object := new;
begin
    perform hymn.throw_if_api_is_illegal('api', record_new.api);
    if record_new.api similar to '%_history' then
        raise exception '[f:inner:02200] 对象api不能以 _history 结尾';
    end if;

    record_new.active = true;

    return record_new;
end;
$$;
comment on function hymn.object_trigger_fun_before_insert() is '业务对象 before insert 触发器函数, 申请数据表';
-- c10开头为通用逻辑触发器
drop trigger if exists c10_object_before_insert on hymn.core_biz_object;
create trigger c10_object_before_insert
    before insert
    on hymn.core_biz_object
    for each row
execute function hymn.object_trigger_fun_before_insert();

create or replace function hymn.object_trigger_fun_before_update() returns trigger
    language plpgsql as
$$
declare
    record_new hymn.core_biz_object := new;
    record_old hymn.core_biz_object := old;
begin
    --     停用对对象停用状态未改变时不能更新其他状态
    if record_new.active = false and record_old.active = false then
        return record_old;
    end if;

    if record_new.source_table <> record_old.source_table then
        raise exception '[f:inner:02800] 不能修改source_table';
    end if;
    if record_new.type <> record_old.type then
        raise exception '[f:inner:02900] 不能修改对象类型';
    end if;

    -- 停用/启用是独立操作，不能同时修改其他数据
    if record_new.active <> record_old.active then
        record_old.active = record_new.active;
        record_old.modify_by = record_new.modify_by;
        record_old.modify_by_id = record_new.modify_by_id;
        record_old.modify_date = record_new.modify_date;
        return record_old;
    end if;
    return record_new;
end;
$$;
comment on function hymn.object_trigger_fun_before_update() is '业务对象 before update 触发器函数, 阻止修改 api 和 source_table ，对象停用时阻止更新';
drop trigger if exists c10_object_before_update on hymn.core_biz_object;
create trigger c10_object_before_update
    before update
    on hymn.core_biz_object
    for each row
execute function hymn.object_trigger_fun_before_update();

create or replace function hymn.object_trigger_fun_before_delete() returns trigger
    language plpgsql as
$$
declare
    record_old hymn.core_biz_object := old;
    ref_field  hymn.core_biz_object_field;
    sql_str    text;
begin
    if record_old.active = true then
        raise exception '[f:inner:03000] 无法删除启用的对象';
    end if;
--     对象删除的同时删除所有关联到该对象的字段
    raise notice 'delete bussiness object [id:%,api:%]',record_old.id,record_old.api;
    for ref_field in select *
                     from hymn.core_biz_object_field
                     where (type in ('mreference', 'reference', 'master_slave')
                         and ref_id = record_old.id)
                        or (type = 'summary' and s_id = record_old.id)
        loop
            raise notice 'delete reference field [id:%,biz_object_id:%]',ref_field.id,ref_field.biz_object_id;
            delete from hymn.core_biz_object_field where id = ref_field.id;
        end loop;
    --     删除所有独有的触发器函数
    if record_old.type <> 'remote' then
        sql_str := format('drop function if exists hymn.%s_mref_trigger_function() cascade',
                          record_old.source_table);
        execute sql_str;
        sql_str := format('drop function if exists hymn.%s_auto_number_trigger_fun() cascade',
                          record_old.api);
        execute sql_str;
        sql_str := format('drop function if exists hymn.%s_history_trigger_fun() cascade',
                          record_old.source_table);
        execute sql_str;
    end if;
    return record_old;
end;
$$;
comment on function hymn.object_trigger_fun_before_delete() is '业务对象 before delete 触发器函数，阻止删除未停用的对象';
drop trigger if exists c10_object_before_delete on hymn.core_biz_object;
create trigger c10_object_before_delete
    before delete
    on hymn.core_biz_object
    for each row
execute function hymn.object_trigger_fun_before_delete();


create or replace function hymn.custom_object_trigger_fun() returns trigger
    language plpgsql as
$$
declare
    record_new       hymn.core_biz_object := new;
    record_old       hymn.core_biz_object := old;
    ref_field_arr    text;
    sql_str          text;
    trigger_fun_name text;
    trigger_name     text;
    share_table_name text;
begin
    if record_old.type = 'custom' or record_new.type = 'custom' then
        if tg_when = 'BEFORE' then
            if tg_op = 'INSERT' then
--                 业务对象自动在api后面添加 __co 的后缀
                record_new.api := record_new.api || '__co';
--                 申请数据表
                update hymn.core_table_obj_mapping sctom
                set obj_api= record_new.api
                where table_name = (
                    select s.table_name
                    from hymn.core_table_obj_mapping s
                    where s.obj_api is null
                      and s.table_name like 'core_data_table_%'
                    limit 1 for update skip locked)
                returning table_name into record_new.source_table;
                if not FOUND then
                    raise exception '[f:inner:02300] 自定义对象的数量已达到上限';
                end if;
            end if;
            if tg_op = 'UPDATE' then
                if record_old.active = true and record_new.active = false then
                    --                      停用对象时删除视图
--                      停用对象时不能有其他对象引用当前对象
                    select array_agg(scbo.api || '.' || scbof.api)::text
                    into ref_field_arr
                    from hymn.core_biz_object scbo
                             inner join hymn.core_biz_object_field scbof
                                        on scbof.biz_object_id = scbo.id
                    where scbof.type in ('mreference', 'reference', 'master_slave', 'summary')
                      and scbof.active = true
                      and scbo.active = true
                      and (scbof.ref_id = record_new.id or scbof.s_id = record_new.id);
                    if ref_field_arr is not null then
                        raise exception '[f:inner:02400] 不能停用当前对象，以下字段引用了当前对象：%',ref_field_arr;
                    end if;
                    sql_str := format('drop view if exists hymn_view.%I cascade', record_old.api);
                    execute sql_str;
                end if;
--                  启用时重建视图
                if record_old.active = false and record_new.active = true then
                    perform hymn.rebuild_object_view(record_old.id);
                end if;
            end if;
        end if;
        if tg_when = 'AFTER' then
            if tg_op = 'INSERT' then
--                 创建历史记录触发器
                perform hymn.rebuild_data_table_history_trigger(record_new.id);
--                 创建视图
                perform hymn.rebuild_object_view(record_new.id);
--                 创建共享表并授权
                share_table_name := record_new.api || '_share';
                sql_str := format(E'create table hymn_view.%I'
                                      '(\n'
                                      '    data_id         text not null,\n'
                                      '    role_id         text,\n'
                                      '    org_id          text,\n'
                                      '    account_id      text,\n'
                                      '    read_only       bool not null default false\n'
                                      ')', share_table_name);
                execute sql_str;
                sql_str := format('grant select,insert,delete on hymn_view.%I to hymn_user;',
                                  share_table_name);
                execute sql_str;


            end if;
            if tg_op = 'DELETE' then
                -- 删除数据
                sql_str := format('truncate hymn.%I', record_old.source_table);
                execute sql_str;
-- 删除历史记录
                sql_str := format('drop table hymn_view.%s_history cascade', record_old.api);
                execute sql_str;
--                 删除共享表
                sql_str := format('drop table hymn_view.%I cascade', record_old.api || '_share');
                execute sql_str;
--             删除序列
                sql_str := format('drop sequence if exists hymn_view.%s_auto_number_seq cascade',
                                  record_old.api);
                execute sql_str;
--             删除引用当前对象的多选关联字段
                delete
                from hymn.core_biz_object_field
                where type = 'mreference'
                  and ref_id = record_old.id;

--     删除触发器及触发器函数
                for trigger_fun_name,trigger_name in select pp.proname, pt.tgname
                                                     from pg_trigger pt
                                                              left join pg_class pc on pt.tgrelid = pc.oid
                                                              left join pg_namespace pn on pn.oid = pc.relnamespace
                                                              left join pg_proc pp on pt.tgfoid = pp.oid
                                                     where pc.relname = record_old.source_table
                                                       and pn.nspname = 'hymn'
                    loop
                        sql_str := format('drop trigger if exists %I on hymn.%I cascade;',
                                          trigger_name,
                                          old.source_table);
                        execute sql_str;
                        sql_str := format('drop function if exists %I;', trigger_fun_name);
                        execute sql_str;
                    end loop;
                --     归还字段和表资源
                update hymn.core_table_obj_mapping
                set obj_api=null
                where table_name = record_old.source_table;
                update hymn.core_column_field_mapping
                set field_api=null
                where table_name = record_old.source_table;
            end if;
        end if;
    end if;
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        return record_new;
    elsif tg_op = 'DELETE' then
        return record_old;
    end if;
end ;
$$;
comment on function hymn.custom_object_trigger_fun() is '业务对象对象类型为custom的自定义对象的触发器';
-- c20开头为根据type字段区分逻辑的触发器
drop trigger if exists c20_custom_object_before on hymn.core_biz_object;
create trigger c20_custom_object_before
    before insert or update
    on hymn.core_biz_object
    for each row
execute function hymn.custom_object_trigger_fun();
drop trigger if exists c20_custom_object_after on hymn.core_biz_object;
create trigger c20_custom_object_after
    after insert or delete
    on hymn.core_biz_object
    for each row
execute function hymn.custom_object_trigger_fun();

create or replace function hymn.module_object_trigger_fun() returns trigger
    language plpgsql as
$$
declare
    record_new    hymn.core_biz_object := new;
    record_old    hymn.core_biz_object := old;
    ref_field_arr text;
    sql_str       text;
begin
    if record_old.type = 'module' or record_new.type = 'module' then
        if tg_when = 'BEFORE' then
            if tg_op = 'INSERT' or tg_op = 'UPDATE' then
                if record_new.can_soft_delete = true then
                    perform *
                    from pg_class pc
                             left join pg_attribute pa on pc.oid = pa.attrelid
                             left join pg_namespace pn on pc.relnamespace = pn.oid
                    where pn.nspname = 'hymn'
                      and pc.relname = record_new.source_table
                      and pc.relkind = 'v'
                      and pa.attname = 'deleted';
                    if not found then
                        raise exception '[f:inner:02401] 模块对象数据表中缺少 deleted 字段，can_soft_delete 不能为true';
                    end if;
                end if;
            end if;
            if tg_op = 'INSERT' then

                if record_new.source_table is null then
                    raise exception '[f:inner:02500] 创建模块对象必须指定数据表';
                end if;
                perform pn.nspname, pc.relname
                from pg_class pc
                         left join pg_namespace pn on pn.oid = pc.relnamespace
                where pc.relkind = 'r'
                  and pn.nspname = 'hymn'
                  and pc.relname = record_new.source_table;
                if not FOUND then
                    raise exception '[f:inner:02600] 表 %.% 不存在','hymn',record_new.source_table;
                end if;
            end if;
            if tg_op = 'UPDATE' then
                if record_old.active = true and record_new.active = false then
                    --                      停用对象时删除视图
--                      停用对象时不能有其他对象引用当前对象
                    select array_agg(scbo.api || '.' || scbof.api)::text
                    into ref_field_arr
                    from hymn.core_biz_object scbo
                             inner join hymn.core_biz_object_field scbof
                                        on scbof.biz_object_id = scbo.id
                    where scbof.type in ('mreference', 'reference', 'master_slave', 'summary')
                      and scbof.active = true
                      and scbo.active = true
                      and (scbof.ref_id = record_new.id or scbof.s_id = record_new.id);
                    if ref_field_arr is not null then
                        raise exception '[f:inner:02700] 不能停用当前对象，以下字段引用了当前对象：%',ref_field_arr;
                    end if;
                    sql_str :=
                            format('drop view if exists hymn_view.%I cascade', record_old.api);
                    execute sql_str;
                end if;
--                  启用时重建视图
                if record_old.active = false and record_new.active = true then
                    perform hymn.rebuild_object_view(record_old.id);
                end if;
            end if;
        end if;
        if tg_when = 'AFTER' then
            if tg_op = 'INSERT' then
--                 创建视图
                perform hymn.rebuild_object_view(record_new.id);
            end if;
            --             模块对象删除时清理数据等操作需要手动执行
--             if tg_op = 'DELETE' then
--
--             end if;
        end if;
    end if;
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        return record_new;
    elsif tg_op = 'DELETE' then
        return record_old;
    end if;
end ;
$$;
comment on function hymn.module_object_trigger_fun() is '业务对象对象类型为module的自定义对象的触发器';
drop trigger if exists c20_module_object_before on hymn.core_biz_object;
create trigger c20_module_object_before
    before insert or update
    on hymn.core_biz_object
    for each row
execute function hymn.module_object_trigger_fun();
drop trigger if exists c20_module_object_after on hymn.core_biz_object;
create trigger c20_module_object_after
    after insert or delete
    on hymn.core_biz_object
    for each row
execute function hymn.module_object_trigger_fun();

create or replace function hymn.remote_object_trigger_fun() returns trigger
    language plpgsql as
$$
declare
    record_new hymn.core_biz_object := new;
    record_old hymn.core_biz_object := old;
begin
    if record_old.type = 'remote' or record_new.type = 'remote' then
        if tg_when = 'BEFORE' then
            if tg_op = 'INSERT' then
                record_new.api = record_new.api || '__cr';
            end if;
        end if;
        if tg_when = 'AFTER' then
        end if;
    end if;
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        return record_new;
    elsif tg_op = 'DELETE' then
        return record_old;
    end if;
end;
$$;
comment on function hymn.remote_object_trigger_fun() is '业务对象对象类型为remote的自定义对象的触发器';
drop trigger if exists c20_remote_object_before on hymn.core_biz_object;
create trigger c20_remote_object_before
    before insert
    on hymn.core_biz_object
    for each row
execute function hymn.remote_object_trigger_fun();
-- drop trigger if exists c20_remote_object_after on hymn.core_biz_object;
-- create trigger c20_remote_object_after
--     after insert
--     on hymn.core_biz_object
--     for each row
-- execute function hymn.remote_object_trigger_fun();


-- flag:field 业务对象字段触发器
create or replace function hymn.field_trigger_fun_before_insert() returns trigger
    language plpgsql as
$$
declare
    record_new    hymn.core_biz_object_field := new;
    column_prefix text;
    obj           hymn.core_biz_object;
begin
    perform hymn.throw_if_api_is_illegal('api', record_new.api);
    --     新建字段启用状态为真
    record_new.active = true;
    select * into obj from hymn.core_biz_object where id = record_new.biz_object_id;
    if not FOUND then
--         不会执行，a10_check_object_active_status_upsert 触发器已执行相同的检查
        raise exception '[f:inner:03100] 对象 [id:%] 不存在',record_new.biz_object_id;
    end if;
    if obj.active = false then
--         不会执行，a10_check_object_active_status_upsert 触发器已执行相同的检查
        raise exception '[f:inner:03200] 对象 [id:%] 未启用',record_new.biz_object_id;
    end if;
    if record_new.predefined = true and record_new.source_column is null then
        raise exception '[f:inner:03300] 预定义字段必须指定 source_column';
    end if;
    if obj.type = 'remote' then
        --     远程对象不需要数据表中的列
        record_new.source_column = '';
        if record_new.predefined = true then
            raise exception '[f:inner:03400] 远程对象不能创建预定义字段';
        end if;
        if record_new.type in ('picture', 'files', 'mreference', 'summary', 'master_slave') then
            raise exception '[f:inner:03500] 远程对象不能创建 图片/文件/多选关联/汇总/主从 字段';
        end if;
    end if;
    if record_new.predefined = true then
        if record_new.type in ('mreference', 'summary', 'master_slave') then
            raise exception '[f:inner:03501] 预定义字段类型不能为 多选关联/汇总/主从';
        end if;
    end if;
--     检查字段类型
    select hymn.field_type_2_column_prefix(record_new.type) into column_prefix;
--     预定义字段不需要申请列
    if record_new.predefined = false and obj.type <> 'remote' then
--     自定义字段末尾加上 __cf
        record_new.api := record_new.api || '__cf';
--     申请列名
        update hymn.core_column_field_mapping sccfm
        set field_api= record_new.api
        where (table_name, column_name) = (
            select s.table_name, s.column_name
            from hymn.core_column_field_mapping s
                     inner join hymn.core_biz_object scbo on scbo.source_table = s.table_name
            where s.field_api is null
              and starts_with(s.column_name, column_prefix)
              and scbo.id = record_new.biz_object_id
            limit 1 for update skip locked)
        returning column_name into record_new.source_column;
        if not FOUND then
            raise exception '[f:inner:03600] %类型字段的数量已达到上限',hymn.field_type_description(record_new.type);
        end if;
    end if;
    return record_new;
end ;
$$;
comment on function hymn.field_trigger_fun_before_insert() is '字段 before insert 触发器函数，新建字段时执行校验和相关操作';
drop trigger if exists c10_field_before_insert on hymn.core_biz_object_field;
create trigger c10_field_before_insert
    before insert
    on hymn.core_biz_object_field
    for each row
execute function hymn.field_trigger_fun_before_insert();


create or replace function hymn.field_trigger_fun_after_insert() returns trigger
    language plpgsql as
$$
declare
    record_new hymn.core_biz_object_field := new;
    obj        hymn.core_biz_object;
begin
    select * into obj from hymn.core_biz_object where id = record_new.biz_object_id;
    if obj.type <> 'remote' then
        if record_new.source_column not like 'pl_%' then
--     构建视图
            perform hymn.rebuild_object_view(record_new.biz_object_id);
        end if;
    end if;
    return record_new;
end;
$$;
comment on function hymn.field_trigger_fun_after_insert() is '字段 after insert 触发器函数，重建视图，如果是自动编号字段则重建触发器';
drop trigger if exists c10_field_after_insert on hymn.core_biz_object_field;
create trigger c10_field_after_insert
    after insert
    on hymn.core_biz_object_field
    for each row
execute function hymn.field_trigger_fun_after_insert();


create or replace function hymn.field_trigger_fun_before_update() returns trigger
    language plpgsql as
$$
declare
    record_old        hymn.core_biz_object_field := old;
    record_new        hymn.core_biz_object_field := new;
    summary_field_api text;
begin
    --     状态为停用的字段不允许修改数据
    if record_new.active = record_old.active and record_new.active = false then
        raise exception '[f:inner:03601] 字段停用时无法修改属性%',new;
    end if;
    --     禁止修改type
    if record_new.type <> record_old.type then
        raise exception '[f:inner:03700] 不能修改字段类型';
    end if;

    --     字段被汇总字段引用时不能停用
    if record_old.active = true and record_new.active = false and
       record_old.type in ('integer', 'float', 'money') then
        select cbo.api || '.' || cbof.api
        into summary_field_api
        from hymn.core_biz_object_field cbof
                 left join hymn.core_biz_object cbo on cbof.biz_object_id = cbo.id
        where cbof.type = 'summary'
          and cbof.active = true
          and cbo.active = true
          and cbof.s_field_id = record_old.id;
        if FOUND then
            raise exception '[f:inner:04400] 当前字段被汇总字段 % 引用，不能停用',summary_field_api;
        end if;
    end if;

    -- 停用/启用字段是独立操作，不能同时修改其他数据
    if record_new.active <> record_old.active then
        record_old.active = record_new.active;
        record_old.modify_by = record_new.modify_by;
        record_old.modify_by_id = record_new.modify_by_id;
        record_old.modify_date = record_new.modify_date;
        return record_old;
    end if;
    return record_new;
end ;
$$;
comment on function hymn.field_trigger_fun_before_update() is '字段 before update 触发器函数，检查字段属性';
drop trigger if exists c10_field_before_update on hymn.core_biz_object_field;
create trigger c10_field_before_update
    before update
    on hymn.core_biz_object_field
    for each row
execute function hymn.field_trigger_fun_before_update();


create or replace function hymn.field_trigger_fun_after_update() returns trigger
    language plpgsql as
$$
declare
    record_old hymn.core_biz_object_field := old;
    record_new hymn.core_biz_object_field := new;
    obj_type   text;

begin
    select type into obj_type from hymn.core_biz_object where id = record_new.biz_object_id;
    if obj_type <> 'remote' then
        if record_old.source_column not like 'pl_%' then
            --         启用字段时重新创建视图
            if record_new.active <> record_old.active then
                perform hymn.rebuild_object_view(record_new.biz_object_id);
            end if;
--         历史记录状态变更时重置历史记录触发器
            if record_new.history <> record_old.history or
               (record_new.history = true and record_old.active <> record_new.active) then
                perform hymn.rebuild_data_table_history_trigger(record_new.biz_object_id);
            end if;

        end if;
    end if;
    return null;
end;
$$;
comment on function hymn.field_trigger_fun_after_update() is '字段 after update 触发器函数，重建视图，如果是自动编号字段则重建触发器';
drop trigger if exists c10_field_after_update on hymn.core_biz_object_field;
create trigger c10_field_after_update
    after update
    on hymn.core_biz_object_field
    for each row
execute function hymn.field_trigger_fun_after_update();

create or replace function hymn.field_trigger_fun_before_delete() returns trigger
    language plpgsql as
$$
declare
    record_old hymn.core_biz_object_field := old;
    obj        hymn.core_biz_object;
    sql_str    text;
begin
    select * into obj from hymn.core_biz_object where id = record_old.biz_object_id;
    if FOUND then
        if record_old.active = true then
            raise exception '[f:inner:04500] 字段 % 未停用，无法删除',record_old.api;
        end if;
    end if;
--     删除预定义字段需手动处理
    if record_old.predefined = false then
        if obj.type <> 'remote' then
            --     如果found为true说明只删除了字段，需要执行数据清理和归还字段资源
--     如果为false说明执行的是删除对象的流程，相关的操作由对象触发器处理
            if FOUND then
                if record_old.source_column ~ '^(?!pl_)' then
--                 删除有数据列的字段时清空列中的数据
                    sql_str :=
                            format('update hymn.%I set %I = null', obj.source_table,
                                   record_old.source_column);
                    execute sql_str;
                end if;
                update hymn.core_column_field_mapping
                set field_api = null
                where table_name = obj.source_table
                  and column_name = record_old.source_column;
            end if;
        end if;
    end if;
    if record_old.source_column <> '' then
--         删除引用当前字段的汇总字段
        delete
        from hymn.core_biz_object_field
        where type = 'summary'
          and s_field_id = record_old.id;
    end if;
    return old;
end;
$$;
drop trigger if exists c10_field_before_delete on hymn.core_biz_object_field;
create trigger c10_field_before_delete
    before delete
    on hymn.core_biz_object_field
    for each row
execute function hymn.field_trigger_fun_before_delete();

create or replace function hymn.mreference_field_trigger_fun() returns trigger
    language plpgsql as
$$
declare
    record_new      hymn.core_biz_object_field := new;
    record_old      hymn.core_biz_object_field := old;
    obj             hymn.core_biz_object;
    ref_obj         hymn.core_biz_object;
    join_table_name text;
    join_view_name  text;
    sql_str         text;
begin
    if record_old.type = 'mreference' or record_new.type = 'mreference' then
        if tg_when = 'BEFORE' then
--             属性检查
            if tg_op = 'INSERT' or tg_op = 'UPDATE' then
                if record_new.ref_id is null or record_new.ref_delete_policy is null then
                    raise exception '[f:inner:04600] 关联对象/关联对象删除策略不能为空';
                end if;
                --                 关联字段及引用的对象的数据被删除时，执行删除操作的用户可能没有删除当前数据的权限
--                 所以关联字段和多选关联字段的删除策略只能是 restrict 或 null
                if record_new.ref_delete_policy not in ('restrict', 'no_action') then
                    raise exception '[f:inner:04700] 删除策略必须为 restrict/no_action';
                end if;
                select * into ref_obj from hymn.core_biz_object where id = record_new.ref_id;
                if not FOUND then
                    raise exception '[f:inner:04800] 引用对象 [id:%] 不存在',record_new.ref_id;
                end if;
                if ref_obj.active = false then
                    raise exception '[f:inner:04900] 引用对象 [id:%] 未启用',ref_obj.id;
                end if;
                select * into obj from hymn.core_biz_object where id = record_new.biz_object_id;
                if obj.type = 'remote' or ref_obj.type = 'remote' then
                    raise exception '[f:inner:05000] 远程对象不能创建多选关联字段或被多选关联字段引用';
                end if;
            end if;
            if tg_op = 'INSERT' then
--                 字段不属于远程对象且不是预定义字段
                if record_new.source_column ~ 'mref\d{3}' and record_new.predefined = false then
--             字段类型为多选关联字段时创建中间表及视图
                    select * into ref_obj from hymn.core_biz_object where id = record_new.ref_id;
                    join_view_name := hymn.get_join_view_name(obj.api, record_new.api);
                    join_table_name := hymn.get_join_table_name(join_view_name);
--             创建连接表
                    sql_str =
                            format('create table hymn.%I(s_id text, t_id text);', join_table_name);
                    execute sql_str;
--             创建连接表视图
                    sql_str = format('create view hymn_view.%I as select s_id,t_id from hymn.%I;',
                                     join_view_name, join_table_name);
                    execute sql_str;
--             创建连接表索引
                    sql_str = format('create index %I on hymn.%I (s_id);',
                                     join_table_name || '_s_idx',
                                     join_table_name);
                    execute sql_str;
                    sql_str = format('create index %I on hymn.%I (t_id);',
                                     join_table_name || '_t_idx',
                                     join_table_name);
                    execute sql_str;
                    record_new.join_view_name := join_view_name;
                    perform hymn.grant_join_view_permission(join_view_name);
                end if;
            end if;
            if tg_op = 'UPDATE' then
            end if;
        end if;
        if tg_when = 'AFTER' then
            if tg_op = 'INSERT' then
                if record_new.source_column ~ 'mref\d{3}' and record_new.predefined = false then
                    --     创建多选关联触发器
                    perform hymn.rebuild_multiple_refernece_trigger_function(
                            record_new.biz_object_id);
                end if;
            end if;
            if tg_op = 'UPDATE' then
                if record_new.source_column ~ 'mref\d{3}' and record_new.predefined = false then
                    if record_old.active <> record_new.active then
--                         启用/停用时重建触发器
                        perform hymn.rebuild_multiple_refernece_trigger_function(
                                record_old.biz_object_id);
                        if record_old.active = true then
--                 停用多选关联字段时删除视图
                            sql_str = format('drop view hymn_view.%I;',
                                             record_old.join_view_name);
                            execute sql_str;
                        else
--                 启用多选关联字段时创建视图
                            join_table_name := hymn.get_join_table_name(record_old.join_view_name);
                            sql_str = format(
                                    'create view hymn_view.%I as select s_id,t_id from hymn.%I;',
                                    record_old.join_view_name, join_table_name);
                            execute sql_str;
                            perform hymn.grant_join_view_permission(record_old.join_view_name);
                        end if;
                    end if;
                end if;
            end if;
            if tg_op = 'DELETE' then
                if record_old.source_column ~ 'mref\d{3}' and record_old.predefined = false then
                    --                     perform hymn.rebuild_multiple_refernece_trigger_function(
--                             record_old.biz_object_id);
--                 删除中间表
                    sql_str := format('drop table hymn.%I cascade;',
                                      hymn.get_join_table_name(record_old.join_view_name));
                    execute sql_str;
                end if;
            end if;
        end if;
    end if;
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        return record_new;
    elsif tg_op = 'DELETE' then
        return record_old;
    end if;
end;
$$;
comment on function hymn.mreference_field_trigger_fun() is '多选关联字段触发器函数';
drop trigger if exists c20_mreference_field_before on hymn.core_biz_object_field;
create trigger c20_mreference_field_before
    before insert or update
    on hymn.core_biz_object_field
    for each row
execute function hymn.mreference_field_trigger_fun();
-- 多选关联的 after delete 触发器需要在通用的after delete清楚表数据之前重建多选关联触发器
drop trigger if exists c00_mreference_field_after on hymn.core_biz_object_field;
create trigger c00_mreference_field_after
    after insert or update or delete
    on hymn.core_biz_object_field
    for each row
execute function hymn.mreference_field_trigger_fun();

create or replace function hymn.text_field_trigger_fun() returns trigger
    language plpgsql as
$$
declare
    record_old hymn.core_biz_object_field := old;
    record_new hymn.core_biz_object_field := new;
begin

    if (record_old.type = 'text' or record_new.type = 'text') and
       (record_new.source_column <> '' or record_old.source_column <> '') then
        if tg_when = 'BEFORE' then
--             属性检查
            if tg_op = 'INSERT' or tg_op = 'UPDATE' then
--         文本类型：最小值 最大值 显示行数
                if record_new.min_length is null or record_new.max_length is null or
                   record_new.visible_row is null then
                    raise exception '[f:inner:05100] 最小长度、最大长度和显示行数都不能为空';
                end if;
                if record_new.min_length < 0 then
                    raise exception '[f:inner:05200] 最小长度必须大于等于0';
                end if;
                if record_new.max_length > 50000 then
                    raise exception '[f:inner:05300] 最大长度必须小于等于50000';
                end if;
                if record_new.max_length < record_new.min_length then
                    raise exception '[f:inner:05400] 最小长度必须小于等于最大长度';
                end if;
                if record_new.visible_row < 1 then
                    raise exception '[f:inner:05500] 可见行数必须大于等于1';
                end if;
                if record_new.api = 'name' then
                    if record_new.max_length > 255 then
                        raise exception '[f:inner:05600] name字段最大长度必须小于等于255';
                    end if;
                    record_new.visible_row = 1;
                    if record_new.standard_type is null or record_new.standard_type <> 'name' then
                        raise exception '[f:inner:05800] name字段 standard_type 属性必须为 ''name''';
                    end if;
                end if;
            end if;
        end if;
        if tg_when = 'AFTER' then
        end if;
    end if;
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        return record_new;
    elsif tg_op = 'DELETE' then
        return record_old;
    end if;
end;
$$;
comment on function hymn.text_field_trigger_fun() is '文本字段触发器函数';
drop trigger if exists c20_text_field_before on hymn.core_biz_object_field;
create trigger c20_text_field_before
    before insert or update
    on hymn.core_biz_object_field
    for each row
execute function hymn.text_field_trigger_fun();

create or replace function hymn.check_box_group_field_trigger_fun() returns trigger
    language plpgsql as
$$
declare
    record_old hymn.core_biz_object_field := old;
    record_new hymn.core_biz_object_field := new;
    dict       hymn.core_dict;
begin
    if (record_old.type = 'check_box_group' or record_new.type = 'check_box_group') and
       (record_new.source_column <> '' or record_old.source_column <> '') then
        if tg_when = 'BEFORE' then
--             属性检查
            if tg_op = 'INSERT' or tg_op = 'UPDATE' then
--         复选框组： 选项个数 字典id
                if record_new.optional_number is null or record_new.dict_id is null then
                    raise exception '[f:inner:05900] 可选个数/字典项不能为空';
                end if;
                if record_new.optional_number < 1 then
                    raise exception '[f:inner:06000] 可选个数必须大于0';
                end if;
                perform * from hymn.core_dict where id = record_new.dict_id;
                if not FOUND then
                    raise exception '[f:inner:06100] 字典 [id:%] 不存在',record_new.dict_id;
                end if;
            end if;
        end if;
        if tg_when = 'AFTER' then
            if tg_op = 'DELETE' then
--                 删除字段时删除专属与该字段的字典
                for dict in select * from hymn.core_dict where field_id = record_old.id
                    loop
                        delete from hymn.core_dict where id = dict.id;
                    end loop;
            end if;
        end if;
    end if;
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        return record_new;
    elsif tg_op = 'DELETE' then
        return record_old;
    end if;
end;
$$;
comment on function hymn.check_box_group_field_trigger_fun() is '复选框组字段触发器函数';
drop trigger if exists c20_check_box_group_field_before on hymn.core_biz_object_field;
create trigger c20_check_box_group_field_before
    before insert or update
    on hymn.core_biz_object_field
    for each row
execute function hymn.check_box_group_field_trigger_fun();
drop trigger if exists c20_check_box_group_field_after on hymn.core_biz_object_field;
create trigger c20_check_box_group_field_after
    after delete
    on hymn.core_biz_object_field
    for each row
execute function hymn.check_box_group_field_trigger_fun();

create or replace function hymn.select_field_trigger_fun() returns trigger
    language plpgsql as
$$
declare
    record_old hymn.core_biz_object_field := old;
    record_new hymn.core_biz_object_field := new;
    dict       hymn.core_dict;
begin
    if (record_old.type = 'select' or record_new.type = 'select') and
       (record_new.source_column <> '' or record_old.source_column <> '') then
        if tg_when = 'BEFORE' then
--             属性检查
            if tg_op = 'INSERT' or tg_op = 'UPDATE' then
--                  可选个数 可见行数 字典id
                if record_new.optional_number is null or record_new.visible_row is null or
                   record_new.dict_id is null then
                    raise exception '[f:inner:06200] 可选个数/可见行数/字典项不能为空';
                end if;
                if record_new.optional_number < 1 then
                    raise exception '[f:inner:06300] 可选个数必须大于0';
                end if;
                if record_new.visible_row < 1 then
                    raise exception '[f:inner:06400] 可见行数必须大于0';
                end if;
                perform * from hymn.core_dict where id = record_new.dict_id;
                if not FOUND then
                    raise exception '[f:inner:06500] 字典 [id:%] 不存在',record_new.dict_id;
                end if;
            end if;
        end if;
        if tg_when = 'AFTER' then
            if tg_op = 'DELETE' then
--                 删除字段时删除专属与该字段的字典
                for dict in select * from hymn.core_dict where field_id = record_old.id
                    loop
                        delete from hymn.core_dict where id = dict.id;
                    end loop;
            end if;
        end if;
    end if;
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        return record_new;
    elsif tg_op = 'DELETE' then
        return record_old;
    end if;
end;
$$;
comment on function hymn.select_field_trigger_fun() is '下拉多选字段触发器函数';
drop trigger if exists c20_select_field_before on hymn.core_biz_object_field;
create trigger c20_select_field_before
    before insert or update
    on hymn.core_biz_object_field
    for each row
execute function hymn.select_field_trigger_fun();
drop trigger if exists c20_select_field_after on hymn.core_biz_object_field;
create trigger c20_select_field_after
    after delete
    on hymn.core_biz_object_field
    for each row
execute function hymn.select_field_trigger_fun();

create or replace function hymn.integer_field_trigger_fun() returns trigger
    language plpgsql as
$$
declare
    record_old hymn.core_biz_object_field := old;
    record_new hymn.core_biz_object_field := new;
begin
    if (record_old.type = 'integer' or record_new.type = 'integer') and
       (record_new.source_column <> '' or record_old.source_column <> '') then
        if tg_when = 'BEFORE' then
--             属性检查
            if tg_op = 'INSERT' or tg_op = 'UPDATE' then
                if record_new.min_length is null or record_new.max_length is null then
                    raise exception '[f:inner:06600] 最小值/最大值不能为空';
                end if;
                if record_new.min_length > record_new.max_length then
                    raise exception '[f:inner:06700] 最小值必须小于最大值';
                end if;
            end if;
        end if;
        if tg_when = 'AFTER' then
        end if;
    end if;
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        return record_new;
    elsif tg_op = 'DELETE' then
        return record_old;
    end if;
end;
$$;
comment on function hymn.integer_field_trigger_fun() is '整数字段触发器函数';
drop trigger if exists c20_integer_field_before on hymn.core_biz_object_field;
create trigger c20_integer_field_before
    before insert or update
    on hymn.core_biz_object_field
    for each row
execute function hymn.integer_field_trigger_fun();

create or replace function hymn.float_field_trigger_fun() returns trigger
    language plpgsql as
$$
declare
    record_old hymn.core_biz_object_field := old;
    record_new hymn.core_biz_object_field := new;
begin
    if (record_old.type = 'float' or record_new.type = 'float') and
       (record_new.source_column <> '' or record_old.source_column <> '') then
        if tg_when = 'BEFORE' then
--             属性检查
            if tg_op = 'INSERT' or tg_op = 'UPDATE' then
--                  整数位长度 小数位长度
                if record_new.min_length is null or record_new.max_length is null then
                    raise exception '[f:inner:06800] 小数位数/整数位数不能为空';
                end if;
                if record_new.min_length < 1 then
                    raise exception '[f:inner:06900] 小数位数必须大于等于1';
                end if;
                if record_new.max_length < 1 then
                    raise exception '[f:inner:07000] 整数位数必须大于等于1';
                end if;
                if (record_new.min_length + record_new.max_length) > 18 then
                    raise exception '[f:inner:07100] 总位数必须小于等于18';
                end if;
            end if;
        end if;
        if tg_when = 'AFTER' then
        end if;
    end if;
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        return record_new;
    elsif tg_op = 'DELETE' then
        return record_old;
    end if;
end;
$$;
comment on function hymn.float_field_trigger_fun() is '浮点数字段触发器函数';
drop trigger if exists c20_float_field_before on hymn.core_biz_object_field;
create trigger c20_float_field_before
    before insert or update
    on hymn.core_biz_object_field
    for each row
execute function hymn.float_field_trigger_fun();

create or replace function hymn.money_field_trigger_fun() returns trigger
    language plpgsql as
$$
declare
    record_old hymn.core_biz_object_field := old;
    record_new hymn.core_biz_object_field := new;
begin
    if (record_old.type = 'money' or record_new.type = 'money') and
       (record_new.source_column <> '' or record_old.source_column <> '') then
        if tg_when = 'BEFORE' then
--             属性检查
            if tg_op = 'INSERT' or tg_op = 'UPDATE' then
--                  货币: 整数位长度 小数位长度
                if record_new.min_length is null or record_new.max_length is null then
                    raise exception '[f:inner:07200] 小数位数/整数位数不能为空';
                end if;
                if record_new.min_length < 1 then
                    raise exception '[f:inner:07300] 小数位数必须大于等于1';
                end if;
                if record_new.max_length < 1 then
                    raise exception '[f:inner:07400] 整数位数必须大于等于1';
                end if;
            end if;
        end if;
        if tg_when = 'AFTER' then
        end if;
    end if;
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        return record_new;
    elsif tg_op = 'DELETE' then
        return record_old;
    end if;
end;
$$;
comment on function hymn.money_field_trigger_fun() is '金额字段触发器函数';
drop trigger if exists c20_money_field_before on hymn.core_biz_object_field;
create trigger c20_money_field_before
    before insert or update
    on hymn.core_biz_object_field
    for each row
execute function hymn.money_field_trigger_fun();

create or replace function hymn.picture_field_trigger_fun() returns trigger
    language plpgsql as
$$
declare
    record_old hymn.core_biz_object_field := old;
    record_new hymn.core_biz_object_field := new;
begin
    if (record_old.type = 'picture' or record_new.type = 'picture') and
       (record_new.source_column <> '' or record_old.source_column <> '') then
        if tg_when = 'BEFORE' then
--             属性检查
            if tg_op = 'INSERT' or tg_op = 'UPDATE' then
--                  图片
                if record_new.min_length is null or record_new.max_length is null then
                    raise exception '[f:inner:07500] 图片数量/图片大小不能为空';
                end if;
                if record_new.min_length < 1 then
                    raise exception '[f:inner:07600] 图片数量必须大于等于1';
                end if;
                if record_new.max_length < 1 then
                    raise exception '[f:inner:07700] 图片大小必须大于1';
                end if;

            end if;
        end if;
        if tg_when = 'AFTER' then
        end if;
    end if;
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        return record_new;
    elsif tg_op = 'DELETE' then
        return record_old;
    end if;
end;
$$;
comment on function hymn.picture_field_trigger_fun() is '图片字段触发器函数';
drop trigger if exists c20_picture_field_before on hymn.core_biz_object_field;
create trigger c20_picture_field_before
    before insert or update
    on hymn.core_biz_object_field
    for each row
execute function hymn.picture_field_trigger_fun();

create or replace function hymn.files_field_trigger_fun() returns trigger
    language plpgsql as
$$
declare
    record_old hymn.core_biz_object_field := old;
    record_new hymn.core_biz_object_field := new;
begin
    if (record_old.type = 'files' or record_new.type = 'files') and
       (record_new.source_column <> '' or record_old.source_column <> '') then
        if tg_when = 'BEFORE' then
--             属性检查
            if tg_op = 'INSERT' or tg_op = 'UPDATE' then
--                  图片
                if record_new.min_length is null or record_new.max_length is null then
                    raise exception '[f:inner:07800] 文件数量/文件大小不能为空';
                end if;
                if record_new.min_length < 1 then
                    raise exception '[f:inner:07900] 文件数量必须大于等于1';
                end if;
                if record_new.max_length < 1 then
                    raise exception '[f:inner:08000] 文件大小必须大于1';
                end if;

            end if;
        end if;
        if tg_when = 'AFTER' then
        end if;
    end if;
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        return record_new;
    elsif tg_op = 'DELETE' then
        return record_old;
    end if;
end;
$$;
comment on function hymn.files_field_trigger_fun() is '文件字段触发器函数';
drop trigger if exists c20_files_field_before on hymn.core_biz_object_field;
create trigger c20_files_field_before
    before insert or update
    on hymn.core_biz_object_field
    for each row
execute function hymn.files_field_trigger_fun();

create or replace function hymn.reference_field_trigger_fun() returns trigger
    language plpgsql as
$$
declare
    record_old hymn.core_biz_object_field := old;
    record_new hymn.core_biz_object_field := new;
    ref_obj    hymn.core_biz_object;
begin
    if (record_old.type = 'reference' or record_new.type = 'reference') and
       (record_new.source_column <> '' or record_old.source_column <> '') then
        if tg_when = 'BEFORE' then
--             属性检查
            if tg_op = 'INSERT' or tg_op = 'UPDATE' then
--                  关联关系
                if record_new.ref_id is null or record_new.ref_delete_policy is null then
                    raise exception '[f:inner:08100] 关联对象/关联对象删除策略不能为空';
                end if;
                --                 关联字段及引用的对象的数据被删除时，执行删除操作的用户可能没有删除当前数据的权限
--                 所以关联字段和多选关联字段的删除策略只能是 restrict 或 null
                if record_new.ref_delete_policy not in ('restrict', 'set_null') then
                    raise exception '[f:inner:08101] 删除策略必须为 restrict/set_null';
                end if;
                select * into ref_obj from hymn.core_biz_object where id = record_new.ref_id;
                if not FOUND then
                    raise exception '[f:inner:08200] 引用对象 [id:%] 不存在',record_new.ref_id;
                end if;
                if ref_obj.active = false then
                    raise exception '[f:inner:08300] 引用对象 [id:%] 未启用',ref_obj.id;
                end if;
            end if;
        end if;
        if tg_when = 'AFTER' then
        end if;
    end if;
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        return record_new;
    elsif tg_op = 'DELETE' then
        return record_old;
    end if;
end;
$$;
comment on function hymn.reference_field_trigger_fun() is '引用字段触发器函数';
drop trigger if exists c20_reference_field_before on hymn.core_biz_object_field;
create trigger c20_reference_field_before
    before insert or update
    on hymn.core_biz_object_field
    for each row
execute function hymn.reference_field_trigger_fun();

create or replace function hymn.areference_field_trigger_fun() returns trigger
    language plpgsql as
$$
declare
    record_old hymn.core_biz_object_field := old;
    record_new hymn.core_biz_object_field := new;
begin
    if (record_old.type = 'areference' or record_new.type = 'areference') and
       (record_new.source_column <> '' or record_old.source_column <> '') then
        if tg_when = 'BEFORE' then
--             属性检查
            if tg_op = 'INSERT' or tg_op = 'UPDATE' then
--                 过滤filter_list中错误的对象id
                if record_new.filter_list is distinct from record_old.filter_list then
                    select string_agg(id, ',')
                    into record_new.filter_list
                    from hymn.core_biz_object
                    where active = true
                      and id = any
                          (array_remove(regexp_split_to_array(record_new.filter_list, ','), ''));
                end if;
            end if;
        end if;
        if tg_when = 'AFTER' then
        end if;
    end if;
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        return record_new;
    elsif tg_op = 'DELETE' then
        return record_old;
    end if;
end ;
$$;
comment on function hymn.areference_field_trigger_fun() is '任意引用字段触发器函数';
drop trigger if exists c20_areference_field_before on hymn.core_biz_object_field;
create trigger c20_areference_field_before
    before insert or update
    on hymn.core_biz_object_field
    for each row
execute function hymn.areference_field_trigger_fun();


create or replace function hymn.master_slave_field_trigger_fun() returns trigger
    language plpgsql as
$$
declare
    record_old         hymn.core_biz_object_field := old;
    record_new         hymn.core_biz_object_field := new;
    ref_obj            hymn.core_biz_object;
    summary_field_name text;
begin
    if (record_old.type = 'master_slave' or record_new.type = 'master_slave') and
       (record_new.source_column <> '' or record_old.source_column <> '') then
        if tg_when = 'BEFORE' then
--             属性检查
            if tg_op = 'INSERT' or tg_op = 'UPDATE' then

--         主详关系
                if record_new.ref_id is null or record_new.ref_list_label is null or
                   record_new.ref_delete_policy is null then
                    raise exception '[f:inner:08400] 关联对象/关联对象相关列表标签/删除策略不能为空';
                end if;
                if record_new.ref_delete_policy not in ('cascade', 'restrict') then
                    raise exception '[f:inner:08401] 主从字段删除策略必须为 cascade 或 restrict';
                end if;
                select * into ref_obj from hymn.core_biz_object where id = record_new.ref_id;
                if not FOUND then
                    raise exception '[f:inner:08500] 引用对象 [id:%] 不存在',record_new.ref_id;
                end if;
                if ref_obj.active = false then
                    raise exception '[f:inner:08600] 引用对象 [id:%] 未启用',ref_obj.id;
                end if;
            end if;
        end if;
        if tg_op = 'UPDATE' then
            if record_new.active <> record_old.active and record_new.active = false then
                select scbo.api || '.' || scbof.api
                into summary_field_name
                from hymn.core_biz_object scbo
                         inner join hymn.core_biz_object_field scbof
                                    on scbof.biz_object_id = scbo.id
                where scbof.type = 'summary'
                  and scbof.active = true
                  and scbo.active = true
                  and scbof.s_id = record_old.biz_object_id;
                if summary_field_name is not null then
                    raise exception '[f:inner:08700] 不能停用当前字段，汇总字段 % 引用了当前对象',summary_field_name;
                end if;
            end if;
        end if;
        if tg_when = 'AFTER' then
        end if;
    end if;
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        return record_new;
    elsif tg_op = 'DELETE' then
        return record_old;
    end if;
end;
$$;
comment on function hymn.master_slave_field_trigger_fun() is '主从字段触发器函数';
drop trigger if exists c20_master_slave_field_before on hymn.core_biz_object_field;
create trigger c20_master_slave_field_before
    before insert or update
    on hymn.core_biz_object_field
    for each row
execute function hymn.master_slave_field_trigger_fun();

create or replace function hymn.auto_field_trigger_fun() returns trigger
    language plpgsql as
$$
declare
    record_old hymn.core_biz_object_field := old;
    record_new hymn.core_biz_object_field := new;
    count      int;
begin
    if (record_old.type = 'auto' or record_new.type = 'auto') and
       (record_new.source_column <> '' or record_old.source_column <> '') then
        if tg_when = 'BEFORE' then
--             属性检查
            if tg_op = 'INSERT' or tg_op = 'UPDATE' then
--                  自动编号
                if record_new.gen_rule is null then
                    raise exception '[f:inner:08800] 编号规则不能为空';
                end if;
                select count(*)
                into count
                from regexp_matches(record_new.gen_rule, '\{0+\}', 'g') t;
                if count != 1 then
                    raise exception '[f:inner:08900] 编号规则中有且只能有一个{0}占位符';
                end if;
            end if;
        end if;
        if tg_when = 'AFTER' then
            if tg_op = 'INSERT' then
                perform hymn.rebuild_auto_numbering_trigger(record_new.biz_object_id);
            end if;
            if tg_op = 'UPDATE' then
                if (record_old.gen_rule != record_new.gen_rule or
                    record_new.active <> record_old.active) then
                    perform hymn.rebuild_auto_numbering_trigger(record_new.biz_object_id);
                end if;
            end if;
        end if;
    end if;
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        return record_new;
    elsif tg_op = 'DELETE' then
        return record_old;
    end if;
end;
$$;
comment on function hymn.auto_field_trigger_fun() is '自动编号字段触发器函数';
drop trigger if exists c20_auto_field_before on hymn.core_biz_object_field;
create trigger c20_auto_field_before
    before insert or update
    on hymn.core_biz_object_field
    for each row
execute function hymn.auto_field_trigger_fun();
drop trigger if exists c20_auto_field_after on hymn.core_biz_object_field;
create trigger c20_auto_field_after
    after insert or update
    on hymn.core_biz_object_field
    for each row
execute function hymn.auto_field_trigger_fun();

create or replace function hymn.summary_field_trigger_fun() returns trigger
    language plpgsql as
$$
declare
    record_old hymn.core_biz_object_field := old;
    record_new hymn.core_biz_object_field := new;
    ref_obj    hymn.core_biz_object;
    ref_field  hymn.core_biz_object_field;
    is_active  bool;
begin
    if (record_old.type = 'summary' or record_new.type = 'summary') and
       (record_new.source_column <> '' or record_old.source_column <> '') then
        if tg_when = 'BEFORE' then
--             属性检查
            if tg_op = 'INSERT' or tg_op = 'UPDATE' then
--         汇总字段
                if record_new.s_id is null or record_new.s_type is null or
                   record_new.min_length is null then
                    raise exception '[f:inner:09000] 汇总对象/汇总类型/小数位长度不能为空';
                end if;
                if record_new.s_type in ('max', 'min', 'sum') and record_new.s_field_id is null then
                    raise exception '[f:inner:09100] 汇总字段不能为空';
                end if;
                if record_new.min_length < 0 then
                    raise exception '[f:inner:09200] 小数位长度必须大于等于0';
                end if;
                select * into ref_obj from hymn.core_biz_object where id = record_new.s_id;
                if not FOUND then
                    raise exception '[f:inner:09300] 汇总对象 [id:%] 不存在',record_new.s_id;
                end if;
                if ref_obj.active = false then
                    raise exception '[f:inner:09400] 汇总对象 [id:%] 未启用',ref_obj.id;
                end if;
                perform *
                from hymn.core_biz_object_field
                where biz_object_id = record_new.s_id
                  and active = true
                  and type = 'master_slave';
                if not FOUND then
                    raise exception '[f:inner:09500] 汇总对象必须是当前对象的子对象';
                end if;
                if record_new.s_type in ('max', 'min', 'sum') then
                    select *
                    into ref_field
                    from hymn.core_biz_object_field
                    where id = record_new.s_field_id
                      and biz_object_id = record_new.s_id;
                    if not FOUND then
                        raise exception '[f:inner:09600] 汇总字段 [id:%] 不存在',record_new.s_field_id;
                    end if;
                    if ref_field.type not in ('integer', 'float', 'money') then
                        raise exception '[f:inner:09700] 最大值、最小值、总和的汇总字段类型必须为：整型/浮点型/金额';
                    end if;
                end if;

            end if;
            if tg_op = 'UPDATE' then
                if record_old.active = false and record_new.active then
                    if record_old.s_type in ('sum', 'max', 'min') then
                        select active
                        into is_active
                        from hymn.core_biz_object_field
                        where record_old.s_field_id = id;
                        if not found then
--                      不可达，目标字段删除时会删除所有引用该字段的汇总字段
                            raise exception '[f:inner:04000] 汇总目标字段已删除，不能启用字段';
                        end if;
                        if is_active = false then
                            raise exception '[f:inner:04100] 汇总目标字段已停用，不能启用当前字段';
                        end if;
                    end if;
                end if;
            end if;
        end if;
        if tg_when = 'AFTER' then
        end if;
    end if;
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        return record_new;
    elsif tg_op = 'DELETE' then
        return record_old;
    end if;
end;
$$;
comment on function hymn.summary_field_trigger_fun() is '汇总字段触发器函数';
drop trigger if exists c20_summary_field_before on hymn.core_biz_object_field;
create trigger c20_summary_field_before
    before insert or update
    on hymn.core_biz_object_field
    for each row
execute function hymn.summary_field_trigger_fun();


create or replace function hymn.query_data_name(object_id_2_data_id_arr json)
    returns setof record
    language plpgsql
as
$$
declare
    object_table text;
    kv           record;
    row          record;
    sql_str      text;
begin
    for kv in select key as object_id, array(select json_array_elements_text(value)) as data_id_arr
              from json_each(object_id_2_data_id_arr)
        loop
            select api
            into object_table
            from hymn.core_biz_object
            where id = kv.object_id
              and active = true;
            if FOUND then
                sql_str := format('select %L,id,name from hymn_view.%I where id = any ($1)',
                                  kv.object_id, object_table);
                for row in execute sql_str using kv.data_id_arr
                    loop
                        return next row;
                    end loop;
            end if;
        end loop;
    return;
end;
$$;
comment on function hymn.query_data_name(json) is '根据对象id和数据id查询数据name字段的值，参数为json，key为自定义对象id，value为数据id数组，参数有效性由调用者保证，用于dataService的query方法';