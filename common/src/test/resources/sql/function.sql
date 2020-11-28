-- 阻止改变所属对象id
create or replace function hymn.cannot_change_object_id() returns trigger
    language plpgsql as
$$
begin
    if old.object_id != new.object_id then
        raise exception 'cannot change object_id';
    end if;
    return new;
end;
$$;
comment on function hymn.cannot_change_object_id() is '不能修改业务对象相关数据的对象id';
drop trigger if exists a00_cannot_change_object_id on hymn.sys_core_b_object_field;
create trigger a00_cannot_change_object_id
    before update
    on hymn.sys_core_b_object_field
    for each row
execute function hymn.cannot_change_object_id();
drop trigger if exists a00_cannot_change_object_id on hymn.sys_core_b_object_type;
create trigger a00_cannot_change_object_id
    before update
    on hymn.sys_core_b_object_type
    for each row
execute function hymn.cannot_change_object_id();
drop trigger if exists a00_cannot_change_object_id on hymn.sys_core_b_object_type_layout;
create trigger a00_cannot_change_object_id
    before update
    on hymn.sys_core_b_object_type_layout
    for each row
execute function hymn.cannot_change_object_id();
drop trigger if exists a00_cannot_change_object_id on hymn.sys_core_b_object_layout;
create trigger a00_cannot_change_object_id
    before update
    on hymn.sys_core_b_object_layout
    for each row
execute function hymn.cannot_change_object_id();
drop trigger if exists a00_cannot_change_object_id on hymn.sys_core_b_object_trigger;
create trigger a00_cannot_change_object_id
    before update
    on hymn.sys_core_b_object_trigger
    for each row
execute function hymn.cannot_change_object_id();

create or replace function hymn.cannot_change_api() returns trigger
    language plpgsql as
$$
begin
    if old.api != new.api then
        raise exception 'cannot change api';
    end if;
    return new;
end;
$$;
create trigger a20_cannot_change_api
    before update
    on hymn.sys_core_b_object
    for each row
execute function hymn.cannot_change_api();
create trigger a20_cannot_change_api
    before update
    on hymn.sys_core_b_object_field
    for each row
execute function hymn.cannot_change_api();
create trigger a20_cannot_change_api
    before update
    on hymn.sys_core_b_object_trigger
    for each row
execute function hymn.cannot_change_api();
create trigger a20_cannot_change_api
    before update
    on hymn.sys_core_custom_button
    for each row
execute function hymn.cannot_change_api();
create trigger a20_cannot_change_api
    before update
    on hymn.sys_core_custom_component
    for each row
execute function hymn.cannot_change_api();
create trigger a20_cannot_change_api
    before update
    on hymn.sys_core_custom_interface
    for each row
execute function hymn.cannot_change_api();
create trigger a20_cannot_change_api
    before update
    on hymn.sys_core_custom_page
    for each row
execute function hymn.cannot_change_api();
create trigger a20_cannot_change_api
    before update
    on hymn.sys_core_dict
    for each row
execute function hymn.cannot_change_api();
create trigger a20_cannot_change_api
    before update
    on hymn.sys_core_module_function
    for each row
execute function hymn.cannot_change_api();
create trigger a20_cannot_change_api
    before update
    on hymn.sys_core_shared_code
    for each row
execute function hymn.cannot_change_api();



-- 对象停用时禁止插入/更新
create or replace function hymn.cannot_upsert_when_object_is_inactive() returns trigger
    language plpgsql as
$$
declare
    obj hymn.sys_core_b_object;
begin
    select * into obj from hymn.sys_core_b_object where id = new.object_id;
    if not FOUND then
        raise exception 'object does not exist';
    end if;
    if not obj.active then
        raise exception 'object is inactive, cannot insert/update/delete related data';
    end if;
    return new;
end;
$$;
comment on function hymn.cannot_upsert_when_object_is_inactive() is '检查当前数据所属的对象是否已停用，对象处于停用状态时不能修改相关数据（insert，update触发器）';
drop trigger if exists a10_check_object_active_status_upsert on hymn.sys_core_b_object_field;
create trigger a10_check_object_active_status_upsert
    before insert or update
    on hymn.sys_core_b_object_field
    for each row
execute function hymn.cannot_upsert_when_object_is_inactive();
drop trigger if exists a10_check_object_active_status_upsert on hymn.sys_core_b_object_layout;
create trigger a10_check_object_active_status_upsert
    before insert or update
    on hymn.sys_core_b_object_layout
    for each row
execute function hymn.cannot_upsert_when_object_is_inactive();
drop trigger if exists a10_check_object_active_status_upsert on hymn.sys_core_b_object_type_layout;
create trigger a10_check_object_active_status_upsert
    before insert or update
    on hymn.sys_core_b_object_type_layout
    for each row
execute function hymn.cannot_upsert_when_object_is_inactive();
drop trigger if exists a10_check_object_active_status_upsert on hymn.sys_core_b_object_type;
create trigger a10_check_object_active_status_upsert
    before insert or update
    on hymn.sys_core_b_object_type
    for each row
execute function hymn.cannot_upsert_when_object_is_inactive();
drop trigger if exists a10_check_object_active_status_upsert on hymn.sys_core_b_object_trigger;
create trigger a10_check_object_active_status_upsert
    before insert or update
    on hymn.sys_core_b_object_trigger
    for each row
execute function hymn.cannot_upsert_when_object_is_inactive();

-- 对象停用时禁止删除相关数据
create or replace function hymn.cannot_delete_when_object_is_inactive() returns trigger
    language plpgsql as
$$
declare
    obj hymn.sys_core_b_object;
begin
    select * into obj from hymn.sys_core_b_object where id = old.object_id;
    if not obj.active then
        raise exception 'object is inactive, cannot insert/update/delete related data';
    end if;
    return old;
end;
$$;
comment on function hymn.cannot_delete_when_object_is_inactive() is '检查当前数据所属的对象是否已停用，对象处于停用状态时不能修改相关数据（delete触发器）';
drop trigger if exists a11_check_object_active_status_delete on hymn.sys_core_b_object_field;
create trigger a11_check_object_active_status_delete
    before delete
    on hymn.sys_core_b_object_field
    for each row
execute function hymn.cannot_delete_when_object_is_inactive();
drop trigger if exists a11_check_object_active_status_delete on hymn.sys_core_b_object_layout;
create trigger a11_check_object_active_status_delete
    before delete
    on hymn.sys_core_b_object_layout
    for each row
execute function hymn.cannot_delete_when_object_is_inactive();
drop trigger if exists a11_check_object_active_status_delete on hymn.sys_core_b_object_type_layout;
create trigger a11_check_object_active_status_delete
    before delete
    on hymn.sys_core_b_object_type_layout
    for each row
execute function hymn.cannot_delete_when_object_is_inactive();
drop trigger if exists a11_check_object_active_status_delete on hymn.sys_core_b_object_type;
create trigger a11_check_object_active_status_delete
    before delete
    on hymn.sys_core_b_object_type
    for each row
execute function hymn.cannot_delete_when_object_is_inactive();
drop trigger if exists a11_check_object_active_status_delete on hymn.sys_core_b_object_trigger;
create trigger a11_check_object_active_status_delete
    before delete
    on hymn.sys_core_b_object_trigger
    for each row
execute function hymn.cannot_delete_when_object_is_inactive();

-- 工具方法
create or replace function hymn.throw_if_api_is_illegal(api_name text, api_value text) returns void
    language plpgsql as
$$
declare
begin
    if api_value not similar to '[a-zA-Z][a-zA-Z0-9_]{1,40}' then
        raise exception 'invalid %, must match [a-zA-Z][a-zA-Z0-9_]{1,40}',api_name;
    end if;
    perform keyword from hymn.sql_keyword where keyword = lower(api_value);
    if FOUND then
        raise exception 'invalid %, % is sql keyword',api_name,api_value;
    end if;
end;
$$;
comment on function hymn.throw_if_api_is_illegal(api_name text, api_value text) is '检查api是否合法，不合法时抛出异常，api必须匹配 [a-zA-Z][a-zA-Z0-9_]{1,40} ，且不能为数据库关键字';

create or replace function hymn.reset_increment_seq_of_data_table(obj_id text) returns void
    language plpgsql as
$$
declare
    seq_name text;
    obj      hymn.sys_core_b_object;
begin
    raise notice 'reset_increment_seq_of_data_table';
    select * into obj from hymn.sys_core_b_object scbo where id = obj_id;
    if not FOUND then
        raise exception 'sys_core_b_object [id:%] not found',obj_id;
    end if;
    if obj.source_table not like 'sys_core_data_table_%' then
        raise exception 'only the increment sequence of business objects can be reset';
    end if;
    seq_name := 'hymn.' || obj.source_table || '_seq';
    raise notice '%',seq_name;
    perform setval(seq_name, 1, false);
end;
$$;
comment on function hymn.reset_increment_seq_of_data_table(obj_id text) is '重置业务对象用于自动编号的自增序列';

create or replace function hymn.rebuild_object_view(obj_id text) returns void
    language plpgsql as
$$
declare
    obj     hymn.sys_core_b_object;
    field   hymn.sys_core_b_object_field;
    columns text := ' id as id ';
begin
    raise notice 'rebuild_object_view';
    raise notice 'obj_id is %',obj_id;
    select * into obj from hymn.sys_core_b_object where id = obj_id;
    if FOUND then
        for field in select *
                     from hymn.sys_core_b_object_field
                     where active = true
                       and object_id = obj_id
            loop
                columns := format(' %I as %I,', field.source_column, field.api) || columns;
            end loop;
        execute format('drop view if exists hymn_view.%I', obj.api);
        execute format('create view hymn_view.%I as select %s from hymn.%I', obj.api, columns,
                       obj.source_table);
    end if;
end;
$$;
comment on function hymn.rebuild_object_view(obj_id text) is '重建对象视图';

create or replace function hymn.field_type_2_column_prefix(field_type text) returns text
    language plpgsql as
$$
declare
    sql_type text;
begin
    raise notice 'field_type_2_sql_type';
    case field_type
        when 'text', 'check_box', 'check_box_group', 'select', 'master_slave', 'reference', 'auto', 'picture'
            then sql_type := 'text';
        when 'integer' then sql_type := 'bigint';
        when 'float' then sql_type := 'double';
        when 'money' then sql_type := 'decimal';
        when 'datetime','date' then sql_type := 'datetime' ;
        else raise exception 'unknown field type: %',field_type;
    end case;
    return sql_type;
end;
$$;
comment on function hymn.field_type_2_column_prefix(field_type text) is '根据字段类型获取对应的sql列名前缀';

-- 创建数据字典，用于创建多选和单选的字段时生成字典数据
create or replace function hymn.create_dict_by_field(field hymn.sys_core_b_object_field) returns uuid
    language plpgsql as
$$
declare
    object_row  hymn.sys_core_b_object;
    dict_name   text;
    dict_api    text;
    new_dict_id text;
    item        record;
begin
    raise notice 'create_dict_by_field';
    select * into object_row from hymn.sys_core_b_object where id = field.object_id;
    if not FOUND then
        raise exception 'sys_core_b_object [id:%] not found',field.object_id;
    end if;
    dict_name := object_row.api || '_' || field.name;
    dict_api := object_row.api || '_' || field.name;
--     字典id不为空，表示当前字段选项值直接复制已有字典
    if field.dict_id is not null then
        select * from hymn.sys_core_dict where id = field.dict_id;
        if not FOUND then
            raise exception 'sys_core_dict [id:%] not found',field.dict_id;
        end if;
--          新建字典
        insert into hymn.sys_core_dict (field_id, parent_dict_id, name, api, remark, create_by_id,
                                        create_by, modify_by_id, modify_by, create_date,
                                        modify_date)
        values (field.id, field.dict_id, dict_name, dict_api, format('copy from [id:%s]', field_id),
                field.create_by_id, field.create_by, field.modify_by_id,
                field.modify_by, field.create_date, field.modify_date)
        returning id into new_dict_id;
--         复制字典项
        for item in select * from hymn.sys_core_dict_item where dict_id = field.dict_id
            loop
                insert into hymn.sys_core_dict_item (dict_id, name, code, parent_code,
                                                     create_by_id,
                                                     create_by, modify_by_id, modify_by,
                                                     create_date, modify_date)
                values (new_dict_id, item.name, item.code, item.parent_code, field.create_by_id,
                        field.create_by, field.modify_by_id, field.modify_by,
                        field.create_date, field.modify_date);
            end loop;
    else
--         新建字典
        insert into hymn.sys_core_dict (field_id, parent_dict_id, name, api, remark, create_by_id,
                                        create_by, modify_by_id, modify_by, create_date,
                                        modify_date)
        values (field.id, null, dict_name, dict_api, 'auto-generated',
                field.create_by_id, field.create_by, field.modify_by_id,
                field.modify_by, field.create_date, field.modify_date)
        returning id into new_dict_id;
--         新建字典项
        FOR item IN SELECT *
                    FROM json_to_recordset(field.tmp::json)
                             as x(name text, code text, parent_code text)
            LOOP
                insert into hymn.sys_core_dict_item (dict_id, name, code, parent_code,
                                                     create_by_id,
                                                     create_by, modify_by_id, modify_by,
                                                     create_date, modify_date)
                values (new_dict_id, item.name, item.code, item.parent_code, field.create_by_id,
                        field.create_by, field.modify_by_id, field.modify_by,
                        field.create_date, field.modify_date);
            END LOOP;
    end if;
    return new_dict_id;
end;
$$;
comment on function hymn.create_dict_by_field(field hymn.sys_core_b_object_field) is '根据字段内容创建数据字典，在创建字段的触发器中调用';

create or replace function hymn.rebuild_auto_numbering_trigger(obj_id text) returns void
    language plpgsql as
$BODY$
declare
    obj                                hymn.sys_core_b_object;
    field                              hymn.sys_core_b_object_field;
    template_name                      text;
    data_table_name                    text;
    data_table_auto_numbering_seq_name text;
    fun_header                         text;
    fun_body                           text := E'begin\n';
    fun_tail                           text := E'    return new;\nend;\n$$;\n';
    trigger_name                       text;
    trigger_fun_name                   text;
begin
    raise notice 'replace_auto_numbering_trigger';
    select * into obj from hymn.sys_core_b_object where id = obj_id;
    if not FOUND then
        raise exception 'object [%] does not exist',obj_id;
    end if;
    if obj.active = false then
        raise exception 'object must be active';
    end if;
    data_table_name := obj.source_table;
    trigger_fun_name := data_table_name || '_auto_numbering_trigger_fun';
    trigger_name := data_table_name || '_auto_numbering';
    data_table_auto_numbering_seq_name := data_table_name || '_seq';

    fun_header := format(
            E'create or replace function hymn.%s() returns trigger\n'
                '   language plpgsql as\n'
                '$$\n'
                'declare\n'
                'seq             text      := nextval(''hymn.%s'');\n'
                'now             timestamp := now();\n'
                'year_v            text      := date_part(''year'', now);\n'
                'yy_v              text      := substr(year_v, 3);\n'
                'month_v           text      := date_part(''month'', now);\n'
                'day_v             text      := date_part(''day'', now);\n'
                'hour_v            text      := date_part(''hour'', now);\n'
                'minute_v          text      := date_part(''minute'', now);\n'
                'size              int;\n'
                'tmp             text;\n', trigger_fun_name, data_table_auto_numbering_seq_name);

    for field in select *
                 from hymn.sys_core_b_object_field
                 where object_id = obj_id
                   and type = 'auto'
        loop
            template_name := field.source_column || '_template';
            fun_header := fun_header ||
                          format(E'%s text := %L;\n', template_name, field.gen_rule);

            fun_body := fun_body ||
                        format(E'raise notice ''%%'',seq;\n'
                                   '%s := replace(%s, ''{yyyy}'', year_v);\n'
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
    obj              hymn.sys_core_b_object;
    field            hymn.sys_core_b_object_field;
    data_table_name  text;
    field_api        text;
    field_column     text;
    fun_header       text;
    fun_body         text := '';
    fun_tail         text ;
    trigger_name     text;
    trigger_fun_name text;
begin
    raise notice 'rebuild_data_table_history_trigger';
    select * into obj from hymn.sys_core_b_object where id = obj_id;
    if not FOUND then
        raise exception 'object [%] does not exist',obj_id;
    end if;
    if obj.active = false then
        raise exception 'object must be active';
    end if;
    data_table_name := obj.source_table;
    trigger_fun_name := data_table_name || '_history_trigger_fun';
    trigger_name := data_table_name || '_history';

    fun_header := format(
            E'create or replace function hymn.%s() returns trigger\n'
                '    language plpgsql as\n'
                '$$\n'
                'declare\n'
                '    js         jsonb                        := ''{}'';\n'
                '    operation  text;\n'
                '    id         text;\n'
                '    o          text;\n'
                '    n          text;\n'
                'begin\n'
                '    if tg_op = ''DELETE'' then\n'
                '        js := to_jsonb(old);\n'
                '        insert into hymn.%s_history (id, operation, stamp, change)\n'
                '        values (old.id, ''d'', now(), js::text);\n'
                '    elseif tg_op = ''UPDATE'' then\n', trigger_fun_name, data_table_name);
    fun_tail :=
            format(E'    end if;\n'
                '    return null;\n'
                'end;\n'
                '$$;\n');
    for field in select *
                 from hymn.sys_core_b_object_field
                 where object_id = obj_id
                   and field.history = true
        loop
            field_api := field.api;
            field_column := field.source_column;

            fun_body := fun_body ||
                        format(
                                E'        if old.%s != new.%s then\n'
                                    '            if old.%s is null then\n'
                                    '                o := ''null'';\n'
                                    '            else\n'
                                    '                o := to_jsonb(old.%s);\n'
                                    '            end if;\n'
                                    '            if new.%s is null then\n'
                                    '                n := ''null'';\n'
                                    '            else\n'
                                    '                n := to_jsonb(new.%s);\n'
                                    '            end if;\n'
                                    '            js := jsonb_insert(js, ''{%s}'',\n'
                                    '                               to_jsonb(''{"o":'' || o || '',"n":'' || n || ''}''));\n'
                                    '        end if;\n',
                                field_column, field_column, field_column, field_column,
                                field_column, field_column, field_api);
        end loop;
    if fun_body <> '' then
        fun_body := fun_body || E'    insert into hymn.%s_history (id, operation, stamp, change)\n'
            '    values (old.id, ''u'', now(), js::text);\n';
    end if;
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
end ;
$BODY$;
comment on function hymn.rebuild_data_table_history_trigger(obj_id text) is '重建数据表的历史记录触发器，生成触发器函数';



create or replace function hymn.check_field_properties(record_new hymn.sys_core_b_object_field) returns void
    language plpgsql as
$$
declare
    own_obj   hymn.sys_core_b_object;
    ref_field hymn.sys_core_b_object_field;
begin
    raise notice 'check_field_properties';
    if record_new.is_predefined = true and record_new.source_column is null then
        raise exception 'source_column cannot be null';
    end if;
    if record_new.type = 'text' then
--         文本类型：最小值 最大值 显示行数
        if record_new.min_length IS null or record_new.max_length is null or
           record_new.visible_row is null then
            raise exception 'min_length,max_length,row cannot be null';
        end if;
        if record_new.min_length < 0 then
            raise exception 'min_length must greater than 0';
        end if;
        if record_new.max_length > 50000 then
            raise exception 'max_length must less than 50000';
        end if;
        if record_new.max_length < record_new.min_length then
            raise exception 'max_length must greater than or equal to min_length';
        end if;
        if record_new.visible_row < 0 then
            raise exception 'row must greater than 0';
        end if;
--         如果字段是主属性则长度不能超过255
        if record_new.api = 'name' then
            if record_new.max_length > 255 then
                raise exception 'max_length must less than or equal to 255';
            end if;
            record_new.default_value := null;
            record_new.formula := null;
        end if;
    elseif record_new.type = 'check_box' then
--         复选框字段必须为预定义字段
        if record_new.is_predefined = false then
            raise exception 'is_predefined must be true';
        end if;
    elseif record_new.type = 'check_box_group' then
--         复选框组： 选项个数 字典id/字典项文本（字典项文本存储在tmp中，这连个字段只要取一个就行）
        if record_new.optional_number is null then
            raise exception 'optional_number cannot be null';
        end if;
        if record_new.optional_number < 1 then
            raise exception 'optional_number must greater than 0';
        end if;
    elseif record_new.type = 'select' then
--         选项列表： 选项个数 字典id/字典项文本（字典项文本存储在tmp中，这连个字段只要取一个就行）
        if record_new.optional_number is null then
            raise exception 'optional_number cannot be null';
        end if;
        if record_new.optional_number < 1 then
            raise exception 'optional_number must greater than 0';
        end if;
    elseif record_new.type = 'integer' then
--         整型
        if record_new.min_length > record_new.max_length then
            raise exception 'min_length must less than max_length';
        end if;
    elseif record_new.type = 'float' then
--         浮点: 整数位长度 小数位长度
        if record_new.min_length is null or record_new.max_length is null then
            raise exception 'min_length,max_length cannot be null';
        end if;
        if record_new.min_length < 0 then
            raise exception 'min_length must greater than 0';
        end if;
        if record_new.max_length < 1 then
            raise exception 'max_length must greater than or equal to 1';
        end if;
        if (record_new.min_length + record_new.max_length) > 18 then
            raise exception 'min_length plus max_length must be less than or equal to 18';
        end if;
    elseif record_new.type = 'money' then
--          货币: 整数位长度 小数位长度
        if record_new.min_length is null or record_new.max_length is null then
            raise exception 'min_length,max_length cannot be null';
        end if;
        if record_new.min_length < 0 then
            raise exception 'min_length must greater than or equal to 0';
        end if;
        if record_new.max_length < 1 then
            raise exception 'max_length must greater than or equal to 1';
        end if;
    elseif record_new.type = 'date' then
--         日期 没有字段限制
    elseif record_new.type = 'datetime' then
--         日期时间 没有字段限制
    elseif record_new.type = 'picture' then
--         图片
        if record_new.min_length is null or record_new.max_length is null then
            raise exception 'min_length, max_length cannot be null';
        end if;
        if record_new.min_length < 1 then
            raise exception 'min_length must greater than or equal to 1';
        end if;
        if record_new.max_length <= 0 then
            raise exception 'max_length must greater than 1';
        end if;
    elseif record_new.type = 'reference' then
--         关联关系
        if record_new.ref_id is null or record_new.ref_list_label is null or
           record_new.ref_delete_policy is null then
            raise exception 'ref_id, ref_list_label, ref_delete_policy cannot be null';
        end if;
        select * into own_obj from hymn.sys_core_b_object where id = record_new.ref_id;
        if not FOUND then
            raise exception 'reference object does not exist';
        end if;
        if own_obj.active = false then
            raise exception 'reference object is not active';
        end if;
    elseif record_new.type = 'master_slave' then
--         主详关系
        if record_new.ref_id is null or record_new.ref_list_label is null then
            raise exception 'ref_id, ref_list_label cannot be null';
        end if;
        select * into own_obj from hymn.sys_core_b_object where id = record_new.ref_id;
        if not FOUND then
            raise exception 'reference object does not exist';
        end if;
        if own_obj.active = false then
            raise exception 'reference object is not active';
        end if;
    elseif record_new.type = 'auto' then
--         自动编号
        if record_new.gen_rule is null then
            raise exception 'gen_rule cannot be null';
        end if;
        if record_new.gen_rule not similar to '%\{0+\}%' then
            raise exception 'gen_rule must include {0}';
        end if;
    elseif record_new.type = 'summary' then
--         汇总字段
        if record_new.s_id is null or record_new.s_type is null or record_new.s_field_id is null or
           record_new.min_length then
            raise exception 's_id, s_type, s_field_id, min_length cannot be null';
        end if;
        if record_new.min_length < 0 then
            raise exception 'min_length must greater than or equal to 0';
        end if;
        select * into own_obj from hymn.sys_core_b_object where id = record_new.ref_id;
        if not FOUND then
            raise exception 'summary object does not exist';
        end if;
        if own_obj.active = false then
            raise exception 'summary object is not active';
        end if;
        if record_new.s_type in ('max', 'min', 'sum') then
            select *
            into ref_field
            from hymn.sys_core_b_object_field
            where id = record_new.s_field_id
              and object_id = record_new.s_id;
            if not FOUND then
                raise exception 'summary field does not exist';
            end if;
            if ref_field.type not in ('integer', 'float', 'money') then
                raise exception 'the type of summary field must in (''integer'',''float'',''money'')';
            end if;
        elseif record_new.s_type = 'count' then
        else
            raise exception 's_type must in (''count'',''max'',''min'',''sum'')';
        end if;
    else
        RAISE EXCEPTION 'object field type cannot be %',record_new.type;
    end if;
end;
$$;
comment on function hymn.check_field_properties(record_new hymn.sys_core_b_object_field) is '根据业务对象字段类型检查属性是否符合要求';

-- 触发器函数

-- 对象相关触发器
create or replace function hymn.object_trigger_fun_before_insert() returns trigger
    language plpgsql as
$$
declare
    record_new hymn.sys_core_b_object := new;
begin
    --     如果当前新增对象是业务对象则申请可用的数据表
    if record_new.module_api is null then
--         业务对象自动在api后面添加 __co 的后缀
        record_new.api := record_new.api || '__co';
        update hymn.sys_core_table_obj_mapping sctom
        set obj_api= record_new.api
        where table_name = (
            select s.table_name
            from hymn.sys_core_table_obj_mapping s
            where s.obj_api is null
              and s.table_name like 'sys_core_data_table_%'
            limit 1 for update skip locked)
        returning table_name into record_new.source_table;
        if not FOUND then
            raise exception 'number of custom objects cannot exceed 500';
        end if;
    else
--         模块对象不添加后缀，api不能和关键字相同
        perform hymn.throw_if_api_is_illegal('api', record_new.api);
        if record_new.source_table is null then
            raise exception 'create a module object must specify source_table';
        end if;
        perform pn.nspname, pc.relname
        from pg_class pc
                 left join pg_namespace pn on pn.oid = pc.relnamespace
        where pc.relkind = 'r'
          and pn.nspname = 'hymn'
          and pc.relname = record_new.source_table;
        if not FOUND then
            raise exception 'table %.% does not exists','hymn',record_new.source_table;
        end if;
    end if;
    return record_new;
end;
$$;
comment on function hymn.object_trigger_fun_before_insert() is '业务对象 before insert 触发器函数, 申请数据表';
drop trigger if exists c00_object_before_insert on hymn.sys_core_b_object;
create trigger c00_object_before_insert
    before insert
    on hymn.sys_core_b_object
    for each row
execute function hymn.object_trigger_fun_before_insert();

create or replace function hymn.object_trigger_fun_after_insert() returns trigger
    language plpgsql as
$$
declare
    record_new    hymn.sys_core_b_object := new;
    obj_id        text                   := record_new.id;
    name_label    text;
    name_gen_rule text ;
    name_type     text ;
begin
    raise notice 'object_trigger_fun_after_insert';
    raise notice 'new object id is %',obj_id;
    if record_new.module_api is null then
        raise notice 'object_trigger_fun_after_insert custom object';
        name_label := split_part(record_new.remark, E'\n', 1);
        name_gen_rule := split_part(record_new.remark, E'\n', 2);
        if name_label = '' then
            name_label := '名称';
        end if;
        if name_gen_rule = '' then
            name_type := 'text';
            name_gen_rule := null;
        else
            name_type := 'auto';
        end if;
        record_new.remark := substring(record_new.remark,
                                       length(name_label || chr(10) || name_gen_rule || chr(10)) +
                                       1);
        if record_new.remark is not null then
            update hymn.sys_core_b_object
            set remark = record_new.remark
            where id = record_new.id;
        end if;

--         生成标准字段
        insert into hymn.sys_core_b_object_field (source_column, object_id, name, api, type, active,
                                                  default_value, formula, max_length, min_length,
                                                  visible_row, dict_id, master_field_id,
                                                  optional_number,
                                                  ref_id, ref_list_label, ref_delete_policy,
                                                  query_filter,
                                                  s_id, s_field_id, s_type, gen_rule, remark, help,
                                                  tmp,
                                                  standard_type, is_predefined, create_by_id,
                                                  create_by,
                                                  modify_by_id, modify_by, create_date, modify_date)
        values ('name', obj_id, name_label, 'name', name_type, true, null, null, 255, 1, null, null,
                null, null, null, null, null, null, null, null, null, name_gen_rule, null, null,
                null, 'name', true, record_new.create_by_id, record_new.create_by,
                record_new.modify_by_id, record_new.modify_by, now(), now()),
               ('create_date', obj_id, '创建时间', 'create_date', 'date', true, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, 'create_date', true, record_new.create_by_id, record_new.create_by,
                record_new.modify_by_id, record_new.modify_by, now(), now()),
               ('modify_date', obj_id, '修改时间', 'modify_date', 'date', true, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, 'modify_date', true, record_new.create_by_id, record_new.create_by,
                record_new.modify_by_id, record_new.modify_by, now(), now()),
               ('create_by_id', obj_id, '创建人', 'create_by_id', 'reference', true, null, null, null,
                null, null, null, null, null, 'bcf5f00c2e6c494ea2318912a639031a',
                record_new.name || ' 创建人', 'null', null, null, null, null, null, null,
                null, null, 'create_by_id', true, record_new.create_by_id, record_new.create_by,
                record_new.modify_by_id, record_new.modify_by, now(), now()),
               ('modify_by_id', obj_id, '修改人', 'modify_by_id', 'reference', true, null, null,
                null,
                null, null, null, null, null, 'bcf5f00c2e6c494ea2318912a639031a',
                record_new.name || ' 修改人', 'null', null, null, null, null, null, null,
                null, null, 'modify_by_id', true, record_new.create_by_id, record_new.create_by,
                record_new.modify_by_id, record_new.modify_by, now(), now()),
               ('owner_id', obj_id, '所有人', 'owner_id', 'reference', true, null, null,
                null,
                null, null, null, null, null, 'bcf5f00c2e6c494ea2318912a639031a',
                record_new.name || ' 所有人', 'null', null, null, null, null, null, null,
                null, null, 'owner_id', true, record_new.create_by_id, record_new.create_by,
                record_new.modify_by_id, record_new.modify_by, now(), now()),
               ('type_id', obj_id, '业务类型', 'type_id', 'reference', true, null, null,
                null,
                null, null, null, null, null, '09da56a7de514895aea5c596820d0ced',
                record_new.name || ' 业务类型', 'restrict', null, null, null, null, null, null,
                null, null, 'type', true, record_new.create_by_id, record_new.create_by,
                record_new.modify_by_id, record_new.modify_by, now(), now()),
               ('lock_state', obj_id, '锁定状态', 'lock_state', 'check_box', true, '0', null,
                null,
                null, null, '4a6010cceea948d6856aac09e8aa19c0', null, 1, null,
                null, false, null, null, null, null, null, null,
                null, null, 'lock_state', true, record_new.create_by_id, record_new.create_by,
                record_new.modify_by_id, record_new.modify_by, now(), now());
--         生成标准类型
        insert into hymn.sys_core_b_object_type (object_id, name, remark, create_by_id,
                                                 create_by, modify_by_id, modify_by,
                                                 create_date, modify_date)
        values (obj_id, '默认类型', '默认类型', record_new.create_by_id, record_new.create_by,
                record_new.modify_by_id, record_new.modify_by, now(), now());

        perform hymn.reset_increment_seq_of_data_table(record_new.id);
    end if;
--     创建历史记录触发器
    perform hymn.rebuild_data_table_history_trigger(record_new.id);
    return null;
end;
$$;
comment on function hymn.object_trigger_fun_after_insert() is '业务对象 after insert 触发器函数, 重置自动编号字段的递增序列，向 sys_core_b_object_field 表中插入系统默认字段数据';
drop trigger if exists c00_object_after_insert on hymn.sys_core_b_object;
create trigger c00_object_after_insert
    after insert
    on hymn.sys_core_b_object
    for each row
execute function hymn.object_trigger_fun_after_insert();

create or replace function hymn.object_trigger_fun_before_update() returns trigger
    language plpgsql as
$$
declare
    record_new    hymn.sys_core_b_object := new;
    record_old    hymn.sys_core_b_object := old;
    sql_str       text;
    ref_field_arr text;
begin
    if record_old.active = false and record_new.active = false then
        raise exception 'object is inactive, cannot update';
    end if;
    if record_new.source_table <> record_old.source_table then
        raise exception 'cannot modify source_table';
    end if;
    if record_old.active = true and record_new.active = false then
--         停用对象时不能有其他对象引用当前对象
        select array_agg(scbo.api || '.' || scbof.api)::text
        into ref_field_arr
        from hymn.sys_core_b_object scbo
                 inner join sys_core_b_object_field scbof on scbof.object_id = scbo.id
        where scbof.type in ('reference', 'master_slave')
          and scbof.active = true
          and scbo.active = true
          and scbof.ref_id = record_new.id;
        if ref_field_arr is not null then
            raise exception 'the following objects refer to the current object and cannot be deactivated:%',ref_field_arr;
        end if;
        sql_str := format('drop view if exists hymn_view.%I cascade', record_old.api);
        execute sql_str;
    end if;
    if record_old.active = false and record_new.active = true then
        perform hymn.rebuild_object_view(record_old.id);
    end if;
    return record_new;
end;
$$;
comment on function hymn.object_trigger_fun_before_update() is '业务对象 before update 触发器函数, 阻止修改 api 和 source_table ，对象停用时阻止更新';
drop trigger if exists c00_object_before_update on hymn.sys_core_b_object;
create trigger c00_object_before_update
    before update
    on hymn.sys_core_b_object
    for each row
execute function hymn.object_trigger_fun_before_update();

create or replace function hymn.object_trigger_fun_before_delete() returns trigger
    language plpgsql as
$$
declare
    record_old hymn.sys_core_b_object := old;
begin
    if record_old.active = true then
        raise exception 'cannot delete active object';
    end if;
    return record_old;
end;
$$;
comment on function hymn.object_trigger_fun_before_delete() is '业务对象 before delete 触发器函数，阻止删除未停用的对象';
drop trigger if exists c00_object_before_delete on hymn.sys_core_b_object;
create trigger c00_object_before_delete
    before delete
    on hymn.sys_core_b_object
    for each row
execute function hymn.object_trigger_fun_before_delete();

create or replace function hymn.object_trigger_fun_after_delete() returns trigger
    language plpgsql as
$$
declare
    record_old       hymn.sys_core_b_object := old;
    sql_str          text;
    trigger_name     text;
    trigger_fun_name text;
begin
    --     删除视图
    sql_str := format('drop view if exists hymn_view.%I cascade', record_old.api);
    execute sql_str;
    -- 删除数据
    sql_str := format('truncate hymn.%I', record_old.source_table);
    execute sql_str;
    -- 删除历史记录
    sql_str := format('truncate hymn.%I_history', record_old.source_table);
    execute sql_str;
--     删除触发器及触发器函数
    for trigger_fun_name,trigger_name in select pp.proname, pt.tgname
                                         from pg_trigger pt
                                                  left join pg_class pc on pt.tgrelid = pc.oid
                                                  left join pg_namespace pn on pn.oid = pc.relnamespace
                                                  left join pg_proc pp on pt.tgfoid = pp.oid
                                         where pc.relname = 'sys_core_data_table_001'
                                           and pn.nspname = 'hymn'
        loop
            sql_str := format('drop trigger if exists %I on hymn.%I cascade;', trigger_name,
                              old.source_table);
            execute sql_str;
            sql_str := format('drop function if exists %I;', trigger_fun_name);
            execute sql_str;
        end loop;
--     归还字段和表资源
    update hymn.sys_core_table_obj_mapping
    set obj_api=null
    where table_name = record_old.source_table;
    update hymn.sys_core_column_field_mapping
    set field_api=null
    where table_name = record_old.source_table;
    return record_old;
end;
$$;
comment on function hymn.object_trigger_fun_after_delete() is '业务对象 after delete 触发器函数，删除数据、历史记录和视图';
drop trigger if exists c00_object_after_delete on hymn.sys_core_b_object;
create trigger c00_object_after_delete
    after delete
    on hymn.sys_core_b_object
    for each row
execute function hymn.object_trigger_fun_after_delete();


-- 业务对象字段触发器
create or replace function hymn.field_trigger_fun_before_insert() returns trigger
    language plpgsql as
$$
declare
    record_new    hymn.sys_core_b_object_field := new;
    column_prefix text;
    dict_id       text;
begin
    raise notice 'field_trigger_fun_before_insert';

    --     检查字段类型约束
    perform hymn.check_field_properties(record_new);

--     新建字段强制启用
    record_new.active = true;

    if record_new.is_predefined = false then
--     自定义字段末尾加上 __cf
        record_new.api := record_new.api || '__cf';

--     如果字段类型为 选择/下拉/下拉多选 则调用创建字典函数
        if record_new.type in ('check_box', 'select', 'multiple_select') then
            select hymn.create_dict_by_field(record_new) into dict_id;
            record_new.dict_id := dict_id;
        end if;

--     申请列名
        select hymn.field_type_2_column_prefix(record_new.type) into column_prefix;
        update hymn.sys_core_column_field_mapping sccfm
        set field_api= record_new.api
        where (table_name, column_name) = (
            select s.table_name, s.column_name
            from hymn.sys_core_column_field_mapping s
                     inner join hymn.sys_core_b_object scbo on scbo.source_table = s.table_name
            where s.field_api is null
              and starts_with(s.column_name, column_prefix)
              and scbo.id = record_new.object_id
            limit 1 for update skip locked)
        returning column_name into record_new.source_column;
        if not FOUND then
            raise exception 'all fields are used';
        end if;

    end if;
    return record_new;
end;
$$;
comment on function hymn.field_trigger_fun_before_insert() is '字段 before insert 触发器函数，新建字段时执行校验和相关操作';
drop trigger if exists c00_field_before_insert on hymn.sys_core_b_object_field;
create trigger c00_field_before_insert
    before insert
    on hymn.sys_core_b_object_field
    for each row
execute function hymn.field_trigger_fun_before_insert();


create or replace function hymn.field_trigger_fun_before_update() returns trigger
    language plpgsql as
$$
declare
    record_old hymn.sys_core_b_object_field := old;
    record_new hymn.sys_core_b_object_field := new;
begin
    raise notice 'field_trigger_fun_before_update';
    --     修改停用的字段时抛出异常
    if record_new.active = record_old.active and record_new.active = false then
        raise exception 'field is inactive, cannot insert/update';
    end if;
    --     禁止修改api和type
    if record_new.api <> record_old.api then
        raise exception 'cannot change api';
    end if;
    if record_new.type <> record_old.type then
        raise exception 'cannot change type';
    end if;
    -- 停用字段时不改变其他值
    if record_new.active <> record_old.active and record_new.active = false then
        record_old.active = false;
        record_old.modify_by = record_new.modify_by;
        record_old.modify_by_id = record_new.modify_by_id;
        record_old.modify_date = record_new.modify_date;
        return record_old;
    end if;

    --     检查字段类型约束
    perform hymn.check_field_properties(record_new);
    return record_new;
end ;
$$;
comment on function hymn.field_trigger_fun_before_update() is '字段 before update 触发器函数，检查字段属性';
drop trigger if exists c00_field_before_update on hymn.sys_core_b_object_field;
create trigger c00_field_before_update
    before update
    on hymn.sys_core_b_object_field
    for each row
execute function hymn.field_trigger_fun_before_update();

create or replace function hymn.field_trigger_fun_after_insert() returns trigger
    language plpgsql as
$$
declare
    record_new hymn.sys_core_b_object_field := new;
begin
    raise notice 'field_trigger_fun_after_insert';
    raise notice 'field object_id is : %',record_new.object_id;
    raise notice 'field is : %',record_new;
    raise notice 'new var is : %',new;
    raise notice 'old var is : %',old;

    --     构建视图
    perform hymn.rebuild_object_view(record_new.object_id);
--     如果是自动编号字段则重建触发器
    if record_new.type = 'auto' then
        perform hymn.rebuild_auto_numbering_trigger(record_new.object_id);
    end if;
    return record_new;
end;
$$;
comment on function hymn.field_trigger_fun_after_insert() is '字段 after insert 触发器函数，重建视图，如果是自动编号字段则重建触发器';
drop trigger if exists c00_field_after_insert on hymn.sys_core_b_object_field;
create trigger c00_field_after_insert
    after insert
    on hymn.sys_core_b_object_field
    for each row
execute function hymn.field_trigger_fun_after_insert();

create or replace function hymn.field_trigger_fun_after_update() returns trigger
    language plpgsql as
$$
declare
    record_old hymn.sys_core_b_object_field := old;
    record_new hymn.sys_core_b_object_field := new;
begin
    raise notice 'field_trigger_fun_after_update';
    --         启用字段时重新创建视图
    if record_new.active != record_old.active then
        perform hymn.rebuild_object_view(record_new.object_id);
    end if;
    if record_new.history != record_old.history then
        perform hymn.rebuild_data_table_history_trigger(record_new.object_id);
    end if;
    if record_new.type = 'auto' and record_old.gen_rule != record_new.gen_rule then
--          如果是自动编号字段则重建触发器
        perform hymn.rebuild_auto_numbering_trigger(record_new.object_id);
    end if;
    return null;
end;
$$;
comment on function hymn.field_trigger_fun_after_update() is '字段 after update 触发器函数，重建视图，如果是自动编号字段则重建触发器';
drop trigger if exists c00_field_after_update on hymn.sys_core_b_object_field;
create trigger c00_field_after_update
    after update
    on hymn.sys_core_b_object_field
execute function hymn.field_trigger_fun_after_update();

create or replace function hymn.field_trigger_fun_before_delete() returns trigger
    language plpgsql as
$$
declare
    record_old hymn.sys_core_b_object_field := old;
    obj        hymn.sys_core_b_object;
    sql_str    text;
begin
    if record_old.active = true then
        raise exception 'cannot delete active field';
    end if;
    select * into obj from hymn.sys_core_b_object where id = record_old.object_id;
    --     如果found为true说明只删除了字段，需要执行数据清理和归还字段资源
--     如果为false说明执行的是删除对象的流程，相关的操作由对象触发器处理
    if FOUND then
        sql_str :=
                format('update hymn.%I set %I = null', obj.source_table, record_old.source_column);
        execute sql_str;
        update hymn.sys_core_column_field_mapping
        set column_name = null
        where table_name = obj.source_table
          and column_name = record_old.source_column;
    end if;
    return old;
end;
$$;
drop trigger if exists c00_field_before_delete on hymn.sys_core_b_object_field;
create trigger c00_field_before_delete
    before delete
    on hymn.sys_core_b_object_field
execute function hymn.field_trigger_fun_before_delete();
