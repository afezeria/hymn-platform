-- 业务对象停用时组织修改相关数据
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
drop trigger if exists a00_cannot_change_object_id on hymn.sys_core_b_object_record_type;
create trigger a00_cannot_change_object_id
    before update
    on hymn.sys_core_b_object_record_type
    for each row
execute function hymn.cannot_change_object_id();
drop trigger if exists a00_cannot_change_object_id on hymn.sys_core_b_object_record_layout;
create trigger a00_cannot_change_object_id
    before update
    on hymn.sys_core_b_object_record_layout
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



create or replace function hymn.throw_if_object_is_inactive_upsert() returns trigger
    language plpgsql as
$$
declare
    obj hymn.sys_core_b_object;
begin
    select * into obj from hymn.sys_core_b_object where id = new.object_id;
    if not obj.active then
        raise exception 'object is inactive, cannot insert/update/delete related data';
    end if;
    return new;
end;
$$;
comment on function hymn.throw_if_object_is_inactive_upsert() is '检查当前数据所属的对象是否已停用，对象处于停用状态时不能修改相关数据（insert，update触发器）';
drop trigger if exists a11_check_object_active_status_upsert on hymn.sys_core_b_object_field;
create trigger a11_check_object_active_status_upsert
    before insert or update
    on hymn.sys_core_b_object_field
    for each row
execute function hymn.throw_if_object_is_inactive_upsert();
drop trigger if exists a11_check_object_active_status_upsert on hymn.sys_core_b_object_layout;
create trigger a11_check_object_active_status_upsert
    before insert or update
    on hymn.sys_core_b_object_layout
    for each row
execute function hymn.throw_if_object_is_inactive_upsert();
drop trigger if exists a11_check_object_active_status_upsert on hymn.sys_core_b_object_record_layout;
create trigger a11_check_object_active_status_upsert
    before insert or update
    on hymn.sys_core_b_object_record_layout
    for each row
execute function hymn.throw_if_object_is_inactive_upsert();
drop trigger if exists a11_check_object_active_status_upsert on hymn.sys_core_b_object_record_type;
create trigger a11_check_object_active_status_upsert
    before insert or update
    on hymn.sys_core_b_object_record_type
    for each row
execute function hymn.throw_if_object_is_inactive_upsert();
drop trigger if exists a11_check_object_active_status_upsert on hymn.sys_core_b_object_trigger;
create trigger a11_check_object_active_status_upsert
    before insert or update
    on hymn.sys_core_b_object_trigger
    for each row
execute function hymn.throw_if_object_is_inactive_upsert();

create or replace function hymn.throw_if_object_is_inactive_delete() returns trigger
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
comment on function hymn.throw_if_object_is_inactive_delete() is '检查当前数据所属的对象是否已停用，对象处于停用状态时不能修改相关数据（delete触发器）';
drop trigger if exists a11_check_object_active_status_delete on hymn.sys_core_b_object_field;
create trigger a11_check_object_active_status_delete
    before delete
    on hymn.sys_core_b_object_field
    for each row
execute function hymn.throw_if_object_is_inactive_delete();
drop trigger if exists a11_check_object_active_status_delete on hymn.sys_core_b_object_layout;
create trigger a11_check_object_active_status_delete
    before delete
    on hymn.sys_core_b_object_layout
    for each row
execute function hymn.throw_if_object_is_inactive_delete();
drop trigger if exists a11_check_object_active_status_delete on hymn.sys_core_b_object_record_layout;
create trigger a11_check_object_active_status_delete
    before delete
    on hymn.sys_core_b_object_record_layout
    for each row
execute function hymn.throw_if_object_is_inactive_delete();
drop trigger if exists a11_check_object_active_status_delete on hymn.sys_core_b_object_record_type;
create trigger a11_check_object_active_status_delete
    before delete
    on hymn.sys_core_b_object_record_type
    for each row
execute function hymn.throw_if_object_is_inactive_delete();
drop trigger if exists a11_check_object_active_status_delete on hymn.sys_core_b_object_trigger;
create trigger a11_check_object_active_status_delete
    before delete
    on hymn.sys_core_b_object_trigger
    for each row
execute function hymn.throw_if_object_is_inactive_delete();

-- 工具方法
create or replace function hymn.throw_if_api_is_illegal(api_name text, api_value text) returns void
    language plpgsql as
$$
declare
begin
    if api_value ~ '^[a-zA-Z0-9_]{2,40}$' then
        raise exception 'invalid %, must match ^[a-zA-Z0-9_]{2,40}$',api_name;
    end if;
    perform keyword from hymn.sql_keyword where keyword = lower(api_value);
    if FOUND then
        raise exception 'invalid %, % is sql keyword',api_name,api_value;
    end if;
end;
$$;
comment on function hymn.throw_if_api_is_illegal(api_name text, api_value text) is '检查api是否合法，不合法时抛出异常，api必须匹配 ^[a-zA-Z0-9_]{2,40}$ ，且不能为数据库关键字';

create or replace function hymn.reset_increment_seq_of_data_table(obj_id text) returns void
    language plpgsql as
$$
declare
    seq_name text;
    obj      hymn.sys_core_b_object;
begin
    select * into obj from hymn.sys_core_b_object scbo where id = obj_id;
    if not FOUND then
        raise exception 'sys_core_b_object [id:%] not found',obj_id;
    end if;
    if obj.source_table not like 'sys_core_data_table_%' then
        raise exception 'only the increment sequence of business objects can be reset';
    end if;
    seq_name := obj.source_table || '_seq';
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
    select * into obj from hymn.sys_core_b_object where id = obj_id;
    if not FOUND then
        raise exception 'object [id:%] does not exist',obj_id;
    end if;
    for field in select * from hymn.sys_core_b_object_field where object_id = obj_id
        loop
            columns := format(' %I as %I,', field.source_column, field.api) || columns;
        end loop;
    execute format('drop view if exists hymn_view.%I', obj.api);
    execute format('create view hymn_view.%I as select %s from hymn_view.%I', obj.api, columns,
                   obj.source_table);
end;
$$;
comment on function hymn.rebuild_object_view(obj_id text) is '重建对象视图';

-- 触发器函数

-- 对象相关触发器
create or replace function hymn.object_trigger_fun_before_insert() returns trigger
    language plpgsql as
$$
declare
    record_new hymn.sys_core_b_object := new;
begin
    perform hymn.throw_if_api_is_illegal('api', record_new.api);
    --     如果当前新增对象是业务对象则申请可用的数据表
    if record_new.module_api is null then
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
    end if;
end;
$$;
comment on function hymn.object_trigger_fun_before_insert() is '业务对象 before insert 触发器函数, 申请数据表';
drop trigger if exists object_before_insert_trigger on hymn.sys_core_b_object;
create trigger object_before_insert_trigger
    before insert 
    on hymn.sys_core_b_object
    for each row
    execute function hymn.object_trigger_fun_before_insert();
    

create or replace function hymn.object_trigger_fun_after_insert() returns trigger
    language plpgsql as
$$
declare
    record_new hymn.sys_core_b_object := new;
begin
    if record_new.module_api is null then

--         todo 插入默认字段，包括
        perform hymn.reset_increment_seq_of_data_table(record_new.id);
    end if;

end;
$$;
comment on function hymn.object_trigger_fun_after_insert() is '业务对象 after insert 触发器函数, 重置自动编号字段的递增序列，向 sys_core_b_object_field 表中插入系统默认字段数据';
drop trigger if exists object_after_insert_trigger on hymn.sys_core_b_object;
create trigger object_after_insert_trigger
    after insert
    on hymn.sys_core_b_object
    for each row
execute function hymn.object_trigger_fun_after_insert();
