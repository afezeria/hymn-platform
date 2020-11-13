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
