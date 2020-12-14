drop table if exists hymn.core_account_history cascade;
create table hymn.core_account_history
(
    operation text,
    stamp timestamp,
    id text,
    lock_time timestamp,
    name text,
    username text,
    password text,
    online_rule text,
    active bool,
    admin bool,
    root bool,
    leader_id text,
    org_id text,
    role_id text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp,
    text001 text,
    text002 text,
    text003 text,
    text004 text,
    text005 text,
    text006 text,
    text007 text,
    text008 text,
    text009 text,
    text010 text,
    bigint001 int8,
    bigint002 int8,
    bigint003 int8,
    bigint004 int8,
    bigint005 int8,
    double001 float8,
    double002 float8,
    double003 float8,
    double004 float8,
    double005 float8,
    decimal001 numeric,
    decimal002 numeric,
    decimal003 numeric,
    decimal004 numeric,
    decimal005 numeric,
    datetime001 timestamptz,
    datetime002 timestamptz,
    datetime003 timestamptz,
    datetime004 timestamptz,
    datetime005 timestamptz
);
create or replace function hymn.core_account_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_account_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_account_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_account_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_account_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_account_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_account_history_ins on hymn.core_account;
create trigger core_account_history_ins
    after insert
    on hymn.core_account
    for each row
execute function hymn.core_account_history_ins();
drop trigger if exists core_account_history_upd on hymn.core_account;
create trigger core_account_history_upd
    after update
    on hymn.core_account
    for each row
execute function hymn.core_account_history_upd();
drop trigger if exists core_account_history_del on hymn.core_account;
create trigger core_account_history_del
    after delete
    on hymn.core_account
    for each row
execute function hymn.core_account_history_del();
drop table if exists hymn.core_account_menu_layout_history cascade;
create table hymn.core_account_menu_layout_history
(
    operation text,
    stamp timestamp,
    id text,
    account_id text,
    client_type text,
    layout_json text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_account_menu_layout_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_account_menu_layout_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_account_menu_layout_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_account_menu_layout_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_account_menu_layout_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_account_menu_layout_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_account_menu_layout_history_ins on hymn.core_account_menu_layout;
create trigger core_account_menu_layout_history_ins
    after insert
    on hymn.core_account_menu_layout
    for each row
execute function hymn.core_account_menu_layout_history_ins();
drop trigger if exists core_account_menu_layout_history_upd on hymn.core_account_menu_layout;
create trigger core_account_menu_layout_history_upd
    after update
    on hymn.core_account_menu_layout
    for each row
execute function hymn.core_account_menu_layout_history_upd();
drop trigger if exists core_account_menu_layout_history_del on hymn.core_account_menu_layout;
create trigger core_account_menu_layout_history_del
    after delete
    on hymn.core_account_menu_layout
    for each row
execute function hymn.core_account_menu_layout_history_del();
drop table if exists hymn.core_account_object_view_history cascade;
create table hymn.core_account_object_view_history
(
    operation text,
    stamp timestamp,
    id text,
    copy_id text,
    remark text,
    global_view bool,
    default_view bool,
    account_id text,
    biz_object_id text,
    name text,
    view_json text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_account_object_view_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_account_object_view_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_account_object_view_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_account_object_view_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_account_object_view_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_account_object_view_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_account_object_view_history_ins on hymn.core_account_object_view;
create trigger core_account_object_view_history_ins
    after insert
    on hymn.core_account_object_view
    for each row
execute function hymn.core_account_object_view_history_ins();
drop trigger if exists core_account_object_view_history_upd on hymn.core_account_object_view;
create trigger core_account_object_view_history_upd
    after update
    on hymn.core_account_object_view
    for each row
execute function hymn.core_account_object_view_history_upd();
drop trigger if exists core_account_object_view_history_del on hymn.core_account_object_view;
create trigger core_account_object_view_history_del
    after delete
    on hymn.core_account_object_view
    for each row
execute function hymn.core_account_object_view_history_del();
drop table if exists hymn.core_biz_object_history cascade;
create table hymn.core_biz_object_history
(
    operation text,
    stamp timestamp,
    id text,
    name text,
    api text,
    source_table text,
    active bool,
    type text,
    remote_url text,
    remote_token text,
    module_api text,
    remark text,
    can_insert bool,
    can_update bool,
    can_delete bool,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_biz_object_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_biz_object_history_ins on hymn.core_biz_object;
create trigger core_biz_object_history_ins
    after insert
    on hymn.core_biz_object
    for each row
execute function hymn.core_biz_object_history_ins();
drop trigger if exists core_biz_object_history_upd on hymn.core_biz_object;
create trigger core_biz_object_history_upd
    after update
    on hymn.core_biz_object
    for each row
execute function hymn.core_biz_object_history_upd();
drop trigger if exists core_biz_object_history_del on hymn.core_biz_object;
create trigger core_biz_object_history_del
    after delete
    on hymn.core_biz_object
    for each row
execute function hymn.core_biz_object_history_del();
drop table if exists hymn.core_biz_object_field_history cascade;
create table hymn.core_biz_object_field_history
(
    operation text,
    stamp timestamp,
    id text,
    source_column text,
    biz_object_id text,
    name text,
    api text,
    type text,
    active bool,
    history bool,
    default_value text,
    formula text,
    max_length int4,
    min_length int4,
    visible_row int4,
    dict_id text,
    master_field_id text,
    optional_number int4,
    ref_id text,
    ref_list_label text,
    ref_delete_policy text,
    query_filter text,
    s_id text,
    s_field_id text,
    s_type text,
    gen_rule text,
    remark text,
    help text,
    tmp text,
    join_view_name text,
    standard_type text,
    is_predefined bool,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_biz_object_field_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_field_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_field_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_field_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_field_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_field_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_biz_object_field_history_ins on hymn.core_biz_object_field;
create trigger core_biz_object_field_history_ins
    after insert
    on hymn.core_biz_object_field
    for each row
execute function hymn.core_biz_object_field_history_ins();
drop trigger if exists core_biz_object_field_history_upd on hymn.core_biz_object_field;
create trigger core_biz_object_field_history_upd
    after update
    on hymn.core_biz_object_field
    for each row
execute function hymn.core_biz_object_field_history_upd();
drop trigger if exists core_biz_object_field_history_del on hymn.core_biz_object_field;
create trigger core_biz_object_field_history_del
    after delete
    on hymn.core_biz_object_field
    for each row
execute function hymn.core_biz_object_field_history_del();
drop table if exists hymn.core_biz_object_field_perm_history cascade;
create table hymn.core_biz_object_field_perm_history
(
    operation text,
    stamp timestamp,
    id text,
    role_id text,
    biz_object_id text,
    field_id text,
    p_read bool,
    p_edit bool,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_biz_object_field_perm_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_field_perm_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_field_perm_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_field_perm_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_field_perm_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_field_perm_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_biz_object_field_perm_history_ins on hymn.core_biz_object_field_perm;
create trigger core_biz_object_field_perm_history_ins
    after insert
    on hymn.core_biz_object_field_perm
    for each row
execute function hymn.core_biz_object_field_perm_history_ins();
drop trigger if exists core_biz_object_field_perm_history_upd on hymn.core_biz_object_field_perm;
create trigger core_biz_object_field_perm_history_upd
    after update
    on hymn.core_biz_object_field_perm
    for each row
execute function hymn.core_biz_object_field_perm_history_upd();
drop trigger if exists core_biz_object_field_perm_history_del on hymn.core_biz_object_field_perm;
create trigger core_biz_object_field_perm_history_del
    after delete
    on hymn.core_biz_object_field_perm
    for each row
execute function hymn.core_biz_object_field_perm_history_del();
drop table if exists hymn.core_biz_object_layout_history cascade;
create table hymn.core_biz_object_layout_history
(
    operation text,
    stamp timestamp,
    id text,
    biz_object_id text,
    name text,
    remark text,
    rel_field_json_arr text,
    pc_read_layout_json text,
    pc_edit_layout_json text,
    mobile_read_layout_json text,
    mobile_edit_layout_json text,
    preview_layout_json text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_biz_object_layout_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_layout_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_layout_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_layout_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_layout_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_layout_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_biz_object_layout_history_ins on hymn.core_biz_object_layout;
create trigger core_biz_object_layout_history_ins
    after insert
    on hymn.core_biz_object_layout
    for each row
execute function hymn.core_biz_object_layout_history_ins();
drop trigger if exists core_biz_object_layout_history_upd on hymn.core_biz_object_layout;
create trigger core_biz_object_layout_history_upd
    after update
    on hymn.core_biz_object_layout
    for each row
execute function hymn.core_biz_object_layout_history_upd();
drop trigger if exists core_biz_object_layout_history_del on hymn.core_biz_object_layout;
create trigger core_biz_object_layout_history_del
    after delete
    on hymn.core_biz_object_layout
    for each row
execute function hymn.core_biz_object_layout_history_del();
drop table if exists hymn.core_biz_object_mapping_history cascade;
create table hymn.core_biz_object_mapping_history
(
    operation text,
    stamp timestamp,
    id text,
    source_biz_object_id text,
    source_type_id text,
    target_biz_object_id text,
    target_type_id text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_biz_object_mapping_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_mapping_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_mapping_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_mapping_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_mapping_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_mapping_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_biz_object_mapping_history_ins on hymn.core_biz_object_mapping;
create trigger core_biz_object_mapping_history_ins
    after insert
    on hymn.core_biz_object_mapping
    for each row
execute function hymn.core_biz_object_mapping_history_ins();
drop trigger if exists core_biz_object_mapping_history_upd on hymn.core_biz_object_mapping;
create trigger core_biz_object_mapping_history_upd
    after update
    on hymn.core_biz_object_mapping
    for each row
execute function hymn.core_biz_object_mapping_history_upd();
drop trigger if exists core_biz_object_mapping_history_del on hymn.core_biz_object_mapping;
create trigger core_biz_object_mapping_history_del
    after delete
    on hymn.core_biz_object_mapping
    for each row
execute function hymn.core_biz_object_mapping_history_del();
drop table if exists hymn.core_biz_object_mapping_item_history cascade;
create table hymn.core_biz_object_mapping_item_history
(
    operation text,
    stamp timestamp,
    id text,
    mapping_id text,
    source_field_id text,
    target_field_id text,
    ref_field1_id text,
    ref_field1_biz_object_id text,
    ref_field2_id text,
    ref_field2_biz_object_id text,
    ref_field3_id text,
    ref_field3_biz_object_id text,
    ref_field4_id text,
    ref_field4_biz_object_id text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_biz_object_mapping_item_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_mapping_item_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_mapping_item_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_mapping_item_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_mapping_item_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_mapping_item_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_biz_object_mapping_item_history_ins on hymn.core_biz_object_mapping_item;
create trigger core_biz_object_mapping_item_history_ins
    after insert
    on hymn.core_biz_object_mapping_item
    for each row
execute function hymn.core_biz_object_mapping_item_history_ins();
drop trigger if exists core_biz_object_mapping_item_history_upd on hymn.core_biz_object_mapping_item;
create trigger core_biz_object_mapping_item_history_upd
    after update
    on hymn.core_biz_object_mapping_item
    for each row
execute function hymn.core_biz_object_mapping_item_history_upd();
drop trigger if exists core_biz_object_mapping_item_history_del on hymn.core_biz_object_mapping_item;
create trigger core_biz_object_mapping_item_history_del
    after delete
    on hymn.core_biz_object_mapping_item
    for each row
execute function hymn.core_biz_object_mapping_item_history_del();
drop table if exists hymn.core_biz_object_perm_history cascade;
create table hymn.core_biz_object_perm_history
(
    operation text,
    stamp timestamp,
    id text,
    role_id text,
    biz_object_id text,
    ins bool,
    upd bool,
    del bool,
    que bool,
    query_with_account_tree bool,
    query_with_dept bool,
    query_with_dept_tree bool,
    query_all bool,
    edit_all bool,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_biz_object_perm_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_perm_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_perm_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_perm_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_perm_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_perm_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_biz_object_perm_history_ins on hymn.core_biz_object_perm;
create trigger core_biz_object_perm_history_ins
    after insert
    on hymn.core_biz_object_perm
    for each row
execute function hymn.core_biz_object_perm_history_ins();
drop trigger if exists core_biz_object_perm_history_upd on hymn.core_biz_object_perm;
create trigger core_biz_object_perm_history_upd
    after update
    on hymn.core_biz_object_perm
    for each row
execute function hymn.core_biz_object_perm_history_upd();
drop trigger if exists core_biz_object_perm_history_del on hymn.core_biz_object_perm;
create trigger core_biz_object_perm_history_del
    after delete
    on hymn.core_biz_object_perm
    for each row
execute function hymn.core_biz_object_perm_history_del();
drop table if exists hymn.core_biz_object_trigger_history cascade;
create table hymn.core_biz_object_trigger_history
(
    operation text,
    stamp timestamp,
    id text,
    active bool,
    remark text,
    biz_object_id text,
    name text,
    api text,
    lang text,
    option_text text,
    ord int4,
    event text,
    code text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_biz_object_trigger_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_trigger_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_trigger_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_trigger_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_trigger_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_trigger_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_biz_object_trigger_history_ins on hymn.core_biz_object_trigger;
create trigger core_biz_object_trigger_history_ins
    after insert
    on hymn.core_biz_object_trigger
    for each row
execute function hymn.core_biz_object_trigger_history_ins();
drop trigger if exists core_biz_object_trigger_history_upd on hymn.core_biz_object_trigger;
create trigger core_biz_object_trigger_history_upd
    after update
    on hymn.core_biz_object_trigger
    for each row
execute function hymn.core_biz_object_trigger_history_upd();
drop trigger if exists core_biz_object_trigger_history_del on hymn.core_biz_object_trigger;
create trigger core_biz_object_trigger_history_del
    after delete
    on hymn.core_biz_object_trigger
    for each row
execute function hymn.core_biz_object_trigger_history_del();
drop table if exists hymn.core_biz_object_type_history cascade;
create table hymn.core_biz_object_type_history
(
    operation text,
    stamp timestamp,
    id text,
    biz_object_id text,
    name text,
    active bool,
    remark text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_biz_object_type_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_type_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_type_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_type_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_type_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_type_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_biz_object_type_history_ins on hymn.core_biz_object_type;
create trigger core_biz_object_type_history_ins
    after insert
    on hymn.core_biz_object_type
    for each row
execute function hymn.core_biz_object_type_history_ins();
drop trigger if exists core_biz_object_type_history_upd on hymn.core_biz_object_type;
create trigger core_biz_object_type_history_upd
    after update
    on hymn.core_biz_object_type
    for each row
execute function hymn.core_biz_object_type_history_upd();
drop trigger if exists core_biz_object_type_history_del on hymn.core_biz_object_type;
create trigger core_biz_object_type_history_del
    after delete
    on hymn.core_biz_object_type
    for each row
execute function hymn.core_biz_object_type_history_del();
drop table if exists hymn.core_biz_object_type_available_options_history cascade;
create table hymn.core_biz_object_type_available_options_history
(
    operation text,
    stamp timestamp,
    id text,
    biz_object_id text,
    type_id text,
    field_id text,
    dict_item_id text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_biz_object_type_available_options_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_type_available_options_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_type_available_options_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_type_available_options_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_type_available_options_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_type_available_options_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_biz_object_type_available_options_history_ins on hymn.core_biz_object_type_available_options;
create trigger core_biz_object_type_available_options_history_ins
    after insert
    on hymn.core_biz_object_type_available_options
    for each row
execute function hymn.core_biz_object_type_available_options_history_ins();
drop trigger if exists core_biz_object_type_available_options_history_upd on hymn.core_biz_object_type_available_options;
create trigger core_biz_object_type_available_options_history_upd
    after update
    on hymn.core_biz_object_type_available_options
    for each row
execute function hymn.core_biz_object_type_available_options_history_upd();
drop trigger if exists core_biz_object_type_available_options_history_del on hymn.core_biz_object_type_available_options;
create trigger core_biz_object_type_available_options_history_del
    after delete
    on hymn.core_biz_object_type_available_options
    for each row
execute function hymn.core_biz_object_type_available_options_history_del();
drop table if exists hymn.core_biz_object_type_layout_history cascade;
create table hymn.core_biz_object_type_layout_history
(
    operation text,
    stamp timestamp,
    id text,
    role_id text,
    biz_object_id text,
    type_id text,
    layout_id text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_biz_object_type_layout_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_type_layout_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_type_layout_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_type_layout_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_type_layout_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_type_layout_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_biz_object_type_layout_history_ins on hymn.core_biz_object_type_layout;
create trigger core_biz_object_type_layout_history_ins
    after insert
    on hymn.core_biz_object_type_layout
    for each row
execute function hymn.core_biz_object_type_layout_history_ins();
drop trigger if exists core_biz_object_type_layout_history_upd on hymn.core_biz_object_type_layout;
create trigger core_biz_object_type_layout_history_upd
    after update
    on hymn.core_biz_object_type_layout
    for each row
execute function hymn.core_biz_object_type_layout_history_upd();
drop trigger if exists core_biz_object_type_layout_history_del on hymn.core_biz_object_type_layout;
create trigger core_biz_object_type_layout_history_del
    after delete
    on hymn.core_biz_object_type_layout
    for each row
execute function hymn.core_biz_object_type_layout_history_del();
drop table if exists hymn.core_biz_object_type_perm_history cascade;
create table hymn.core_biz_object_type_perm_history
(
    operation text,
    stamp timestamp,
    id text,
    role_id text,
    biz_object_id text,
    type_id text,
    visible bool,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_biz_object_type_perm_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_type_perm_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_type_perm_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_type_perm_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_biz_object_type_perm_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_biz_object_type_perm_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_biz_object_type_perm_history_ins on hymn.core_biz_object_type_perm;
create trigger core_biz_object_type_perm_history_ins
    after insert
    on hymn.core_biz_object_type_perm
    for each row
execute function hymn.core_biz_object_type_perm_history_ins();
drop trigger if exists core_biz_object_type_perm_history_upd on hymn.core_biz_object_type_perm;
create trigger core_biz_object_type_perm_history_upd
    after update
    on hymn.core_biz_object_type_perm
    for each row
execute function hymn.core_biz_object_type_perm_history_upd();
drop trigger if exists core_biz_object_type_perm_history_del on hymn.core_biz_object_type_perm;
create trigger core_biz_object_type_perm_history_del
    after delete
    on hymn.core_biz_object_type_perm
    for each row
execute function hymn.core_biz_object_type_perm_history_del();
drop table if exists hymn.core_business_code_ref_history cascade;
create table hymn.core_business_code_ref_history
(
    operation text,
    stamp timestamp,
    id text,
    trigger_id text,
    interface_id text,
    shared_code_id text,
    biz_object_id text,
    field_id text,
    org_id text,
    role_id text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_business_code_ref_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_business_code_ref_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_business_code_ref_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_business_code_ref_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_business_code_ref_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_business_code_ref_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_business_code_ref_history_ins on hymn.core_business_code_ref;
create trigger core_business_code_ref_history_ins
    after insert
    on hymn.core_business_code_ref
    for each row
execute function hymn.core_business_code_ref_history_ins();
drop trigger if exists core_business_code_ref_history_upd on hymn.core_business_code_ref;
create trigger core_business_code_ref_history_upd
    after update
    on hymn.core_business_code_ref
    for each row
execute function hymn.core_business_code_ref_history_upd();
drop trigger if exists core_business_code_ref_history_del on hymn.core_business_code_ref;
create trigger core_business_code_ref_history_del
    after delete
    on hymn.core_business_code_ref
    for each row
execute function hymn.core_business_code_ref_history_del();
drop table if exists hymn.core_button_perm_history cascade;
create table hymn.core_button_perm_history
(
    operation text,
    stamp timestamp,
    id text,
    role_id text,
    button_id text,
    visible bool,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_button_perm_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_button_perm_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_button_perm_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_button_perm_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_button_perm_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_button_perm_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_button_perm_history_ins on hymn.core_button_perm;
create trigger core_button_perm_history_ins
    after insert
    on hymn.core_button_perm
    for each row
execute function hymn.core_button_perm_history_ins();
drop trigger if exists core_button_perm_history_upd on hymn.core_button_perm;
create trigger core_button_perm_history_upd
    after update
    on hymn.core_button_perm
    for each row
execute function hymn.core_button_perm_history_upd();
drop trigger if exists core_button_perm_history_del on hymn.core_button_perm;
create trigger core_button_perm_history_del
    after delete
    on hymn.core_button_perm
    for each row
execute function hymn.core_button_perm_history_del();
drop table if exists hymn.core_column_field_mapping_history cascade;
create table hymn.core_column_field_mapping_history
(
    operation text,
    stamp timestamp,
    table_name text,
    column_name text,
    field_api text
);
create or replace function hymn.core_column_field_mapping_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_column_field_mapping_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_column_field_mapping_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_column_field_mapping_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_column_field_mapping_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_column_field_mapping_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_column_field_mapping_history_ins on hymn.core_column_field_mapping;
create trigger core_column_field_mapping_history_ins
    after insert
    on hymn.core_column_field_mapping
    for each row
execute function hymn.core_column_field_mapping_history_ins();
drop trigger if exists core_column_field_mapping_history_upd on hymn.core_column_field_mapping;
create trigger core_column_field_mapping_history_upd
    after update
    on hymn.core_column_field_mapping
    for each row
execute function hymn.core_column_field_mapping_history_upd();
drop trigger if exists core_column_field_mapping_history_del on hymn.core_column_field_mapping;
create trigger core_column_field_mapping_history_del
    after delete
    on hymn.core_column_field_mapping
    for each row
execute function hymn.core_column_field_mapping_history_del();
drop table if exists hymn.core_config_history cascade;
create table hymn.core_config_history
(
    operation text,
    stamp timestamp,
    id text,
    key text,
    value text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_config_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_config_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_config_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_config_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_config_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_config_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_config_history_ins on hymn.core_config;
create trigger core_config_history_ins
    after insert
    on hymn.core_config
    for each row
execute function hymn.core_config_history_ins();
drop trigger if exists core_config_history_upd on hymn.core_config;
create trigger core_config_history_upd
    after update
    on hymn.core_config
    for each row
execute function hymn.core_config_history_upd();
drop trigger if exists core_config_history_del on hymn.core_config;
create trigger core_config_history_del
    after delete
    on hymn.core_config
    for each row
execute function hymn.core_config_history_del();
drop table if exists hymn.core_cron_job_history cascade;
create table hymn.core_cron_job_history
(
    operation text,
    stamp timestamp,
    id text,
    active bool,
    shared_code_id text,
    cron text,
    start_date_time timestamp,
    end_date_time timestamp,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_cron_job_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_cron_job_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_cron_job_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_cron_job_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_cron_job_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_cron_job_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_cron_job_history_ins on hymn.core_cron_job;
create trigger core_cron_job_history_ins
    after insert
    on hymn.core_cron_job
    for each row
execute function hymn.core_cron_job_history_ins();
drop trigger if exists core_cron_job_history_upd on hymn.core_cron_job;
create trigger core_cron_job_history_upd
    after update
    on hymn.core_cron_job
    for each row
execute function hymn.core_cron_job_history_upd();
drop trigger if exists core_cron_job_history_del on hymn.core_cron_job;
create trigger core_cron_job_history_del
    after delete
    on hymn.core_cron_job
    for each row
execute function hymn.core_cron_job_history_del();
drop table if exists hymn.core_custom_button_history cascade;
create table hymn.core_custom_button_history
(
    operation text,
    stamp timestamp,
    id text,
    remark text,
    biz_object_id text,
    name text,
    api text,
    client_type text,
    action text,
    content text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_custom_button_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_custom_button_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_custom_button_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_custom_button_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_custom_button_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_custom_button_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_custom_button_history_ins on hymn.core_custom_button;
create trigger core_custom_button_history_ins
    after insert
    on hymn.core_custom_button
    for each row
execute function hymn.core_custom_button_history_ins();
drop trigger if exists core_custom_button_history_upd on hymn.core_custom_button;
create trigger core_custom_button_history_upd
    after update
    on hymn.core_custom_button
    for each row
execute function hymn.core_custom_button_history_upd();
drop trigger if exists core_custom_button_history_del on hymn.core_custom_button;
create trigger core_custom_button_history_del
    after delete
    on hymn.core_custom_button
    for each row
execute function hymn.core_custom_button_history_del();
drop table if exists hymn.core_custom_component_history cascade;
create table hymn.core_custom_component_history
(
    operation text,
    stamp timestamp,
    id text,
    api text,
    name text,
    code text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_custom_component_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_custom_component_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_custom_component_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_custom_component_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_custom_component_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_custom_component_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_custom_component_history_ins on hymn.core_custom_component;
create trigger core_custom_component_history_ins
    after insert
    on hymn.core_custom_component
    for each row
execute function hymn.core_custom_component_history_ins();
drop trigger if exists core_custom_component_history_upd on hymn.core_custom_component;
create trigger core_custom_component_history_upd
    after update
    on hymn.core_custom_component
    for each row
execute function hymn.core_custom_component_history_upd();
drop trigger if exists core_custom_component_history_del on hymn.core_custom_component;
create trigger core_custom_component_history_del
    after delete
    on hymn.core_custom_component
    for each row
execute function hymn.core_custom_component_history_del();
drop table if exists hymn.core_custom_interface_history cascade;
create table hymn.core_custom_interface_history
(
    operation text,
    stamp timestamp,
    id text,
    api text,
    name text,
    code text,
    active bool,
    lang text,
    option_text text,
    remark text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_custom_interface_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_custom_interface_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_custom_interface_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_custom_interface_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_custom_interface_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_custom_interface_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_custom_interface_history_ins on hymn.core_custom_interface;
create trigger core_custom_interface_history_ins
    after insert
    on hymn.core_custom_interface
    for each row
execute function hymn.core_custom_interface_history_ins();
drop trigger if exists core_custom_interface_history_upd on hymn.core_custom_interface;
create trigger core_custom_interface_history_upd
    after update
    on hymn.core_custom_interface
    for each row
execute function hymn.core_custom_interface_history_upd();
drop trigger if exists core_custom_interface_history_del on hymn.core_custom_interface;
create trigger core_custom_interface_history_del
    after delete
    on hymn.core_custom_interface
    for each row
execute function hymn.core_custom_interface_history_del();
drop table if exists hymn.core_custom_menu_item_history cascade;
create table hymn.core_custom_menu_item_history
(
    operation text,
    stamp timestamp,
    id text,
    name text,
    path text,
    path_type text,
    action text,
    client_type text,
    icon text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_custom_menu_item_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_custom_menu_item_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_custom_menu_item_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_custom_menu_item_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_custom_menu_item_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_custom_menu_item_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_custom_menu_item_history_ins on hymn.core_custom_menu_item;
create trigger core_custom_menu_item_history_ins
    after insert
    on hymn.core_custom_menu_item
    for each row
execute function hymn.core_custom_menu_item_history_ins();
drop trigger if exists core_custom_menu_item_history_upd on hymn.core_custom_menu_item;
create trigger core_custom_menu_item_history_upd
    after update
    on hymn.core_custom_menu_item
    for each row
execute function hymn.core_custom_menu_item_history_upd();
drop trigger if exists core_custom_menu_item_history_del on hymn.core_custom_menu_item;
create trigger core_custom_menu_item_history_del
    after delete
    on hymn.core_custom_menu_item
    for each row
execute function hymn.core_custom_menu_item_history_del();
drop table if exists hymn.core_custom_page_history cascade;
create table hymn.core_custom_page_history
(
    operation text,
    stamp timestamp,
    id text,
    api text,
    name text,
    template text,
    static bool,
    remark text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_custom_page_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_custom_page_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_custom_page_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_custom_page_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_custom_page_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_custom_page_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_custom_page_history_ins on hymn.core_custom_page;
create trigger core_custom_page_history_ins
    after insert
    on hymn.core_custom_page
    for each row
execute function hymn.core_custom_page_history_ins();
drop trigger if exists core_custom_page_history_upd on hymn.core_custom_page;
create trigger core_custom_page_history_upd
    after update
    on hymn.core_custom_page
    for each row
execute function hymn.core_custom_page_history_upd();
drop trigger if exists core_custom_page_history_del on hymn.core_custom_page;
create trigger core_custom_page_history_del
    after delete
    on hymn.core_custom_page
    for each row
execute function hymn.core_custom_page_history_del();
drop table if exists hymn.core_dict_history cascade;
create table hymn.core_dict_history
(
    operation text,
    stamp timestamp,
    id text,
    field_id text,
    parent_dict_id text,
    name text,
    api text,
    remark text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_dict_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_dict_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_dict_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_dict_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_dict_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_dict_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_dict_history_ins on hymn.core_dict;
create trigger core_dict_history_ins
    after insert
    on hymn.core_dict
    for each row
execute function hymn.core_dict_history_ins();
drop trigger if exists core_dict_history_upd on hymn.core_dict;
create trigger core_dict_history_upd
    after update
    on hymn.core_dict
    for each row
execute function hymn.core_dict_history_upd();
drop trigger if exists core_dict_history_del on hymn.core_dict;
create trigger core_dict_history_del
    after delete
    on hymn.core_dict
    for each row
execute function hymn.core_dict_history_del();
drop table if exists hymn.core_dict_item_history cascade;
create table hymn.core_dict_item_history
(
    operation text,
    stamp timestamp,
    id text,
    dict_id text,
    name text,
    code text,
    parent_code text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_dict_item_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_dict_item_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_dict_item_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_dict_item_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_dict_item_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_dict_item_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_dict_item_history_ins on hymn.core_dict_item;
create trigger core_dict_item_history_ins
    after insert
    on hymn.core_dict_item
    for each row
execute function hymn.core_dict_item_history_ins();
drop trigger if exists core_dict_item_history_upd on hymn.core_dict_item;
create trigger core_dict_item_history_upd
    after update
    on hymn.core_dict_item
    for each row
execute function hymn.core_dict_item_history_upd();
drop trigger if exists core_dict_item_history_del on hymn.core_dict_item;
create trigger core_dict_item_history_del
    after delete
    on hymn.core_dict_item
    for each row
execute function hymn.core_dict_item_history_del();
drop table if exists hymn.core_menu_item_perm_history cascade;
create table hymn.core_menu_item_perm_history
(
    operation text,
    stamp timestamp,
    id text,
    role_id text,
    menu_item_id text,
    visible bool,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_menu_item_perm_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_menu_item_perm_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_menu_item_perm_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_menu_item_perm_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_menu_item_perm_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_menu_item_perm_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_menu_item_perm_history_ins on hymn.core_menu_item_perm;
create trigger core_menu_item_perm_history_ins
    after insert
    on hymn.core_menu_item_perm
    for each row
execute function hymn.core_menu_item_perm_history_ins();
drop trigger if exists core_menu_item_perm_history_upd on hymn.core_menu_item_perm;
create trigger core_menu_item_perm_history_upd
    after update
    on hymn.core_menu_item_perm
    for each row
execute function hymn.core_menu_item_perm_history_upd();
drop trigger if exists core_menu_item_perm_history_del on hymn.core_menu_item_perm;
create trigger core_menu_item_perm_history_del
    after delete
    on hymn.core_menu_item_perm
    for each row
execute function hymn.core_menu_item_perm_history_del();
drop table if exists hymn.core_module_history cascade;
create table hymn.core_module_history
(
    operation text,
    stamp timestamp,
    api text,
    name text,
    remark text,
    version text,
    create_date timestamptz
);
create or replace function hymn.core_module_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_module_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_module_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_module_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_module_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_module_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_module_history_ins on hymn.core_module;
create trigger core_module_history_ins
    after insert
    on hymn.core_module
    for each row
execute function hymn.core_module_history_ins();
drop trigger if exists core_module_history_upd on hymn.core_module;
create trigger core_module_history_upd
    after update
    on hymn.core_module
    for each row
execute function hymn.core_module_history_upd();
drop trigger if exists core_module_history_del on hymn.core_module;
create trigger core_module_history_del
    after delete
    on hymn.core_module
    for each row
execute function hymn.core_module_history_del();
drop table if exists hymn.core_module_function_history cascade;
create table hymn.core_module_function_history
(
    operation text,
    stamp timestamp,
    id text,
    module_api text,
    api text,
    name text,
    remark text,
    create_date text
);
create or replace function hymn.core_module_function_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_module_function_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_module_function_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_module_function_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_module_function_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_module_function_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_module_function_history_ins on hymn.core_module_function;
create trigger core_module_function_history_ins
    after insert
    on hymn.core_module_function
    for each row
execute function hymn.core_module_function_history_ins();
drop trigger if exists core_module_function_history_upd on hymn.core_module_function;
create trigger core_module_function_history_upd
    after update
    on hymn.core_module_function
    for each row
execute function hymn.core_module_function_history_upd();
drop trigger if exists core_module_function_history_del on hymn.core_module_function;
create trigger core_module_function_history_del
    after delete
    on hymn.core_module_function
    for each row
execute function hymn.core_module_function_history_del();
drop table if exists hymn.core_module_function_perm_history cascade;
create table hymn.core_module_function_perm_history
(
    operation text,
    stamp timestamp,
    id text,
    role_id text,
    module_function_id text,
    perm bool,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_module_function_perm_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_module_function_perm_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_module_function_perm_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_module_function_perm_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_module_function_perm_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_module_function_perm_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_module_function_perm_history_ins on hymn.core_module_function_perm;
create trigger core_module_function_perm_history_ins
    after insert
    on hymn.core_module_function_perm
    for each row
execute function hymn.core_module_function_perm_history_ins();
drop trigger if exists core_module_function_perm_history_upd on hymn.core_module_function_perm;
create trigger core_module_function_perm_history_upd
    after update
    on hymn.core_module_function_perm
    for each row
execute function hymn.core_module_function_perm_history_upd();
drop trigger if exists core_module_function_perm_history_del on hymn.core_module_function_perm;
create trigger core_module_function_perm_history_del
    after delete
    on hymn.core_module_function_perm
    for each row
execute function hymn.core_module_function_perm_history_del();
drop table if exists hymn.core_org_history cascade;
create table hymn.core_org_history
(
    operation text,
    stamp timestamp,
    id text,
    name text,
    director_id text,
    deputy_director_id text,
    parent_id text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp,
    text001 text,
    text002 text,
    text003 text,
    text004 text,
    text005 text,
    bigint001 int8,
    bigint002 int8,
    bigint003 int8,
    bigint004 int8,
    bigint005 int8
);
create or replace function hymn.core_org_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_org_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_org_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_org_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_org_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_org_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_org_history_ins on hymn.core_org;
create trigger core_org_history_ins
    after insert
    on hymn.core_org
    for each row
execute function hymn.core_org_history_ins();
drop trigger if exists core_org_history_upd on hymn.core_org;
create trigger core_org_history_upd
    after update
    on hymn.core_org
    for each row
execute function hymn.core_org_history_upd();
drop trigger if exists core_org_history_del on hymn.core_org;
create trigger core_org_history_del
    after delete
    on hymn.core_org
    for each row
execute function hymn.core_org_history_del();
drop table if exists hymn.core_role_history cascade;
create table hymn.core_role_history
(
    operation text,
    stamp timestamp,
    id text,
    name text,
    remark text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_role_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_role_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_role_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_role_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_role_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_role_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_role_history_ins on hymn.core_role;
create trigger core_role_history_ins
    after insert
    on hymn.core_role
    for each row
execute function hymn.core_role_history_ins();
drop trigger if exists core_role_history_upd on hymn.core_role;
create trigger core_role_history_upd
    after update
    on hymn.core_role
    for each row
execute function hymn.core_role_history_upd();
drop trigger if exists core_role_history_del on hymn.core_role;
create trigger core_role_history_del
    after delete
    on hymn.core_role
    for each row
execute function hymn.core_role_history_del();
drop table if exists hymn.core_shared_code_history cascade;
create table hymn.core_shared_code_history
(
    operation text,
    stamp timestamp,
    id text,
    api text,
    type text,
    code text,
    lang text,
    option_text text,
    create_by_id text,
    create_by text,
    modify_by_id text,
    modify_by text,
    create_date timestamp,
    modify_date timestamp
);
create or replace function hymn.core_shared_code_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_shared_code_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_shared_code_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_shared_code_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_shared_code_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_shared_code_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_shared_code_history_ins on hymn.core_shared_code;
create trigger core_shared_code_history_ins
    after insert
    on hymn.core_shared_code
    for each row
execute function hymn.core_shared_code_history_ins();
drop trigger if exists core_shared_code_history_upd on hymn.core_shared_code;
create trigger core_shared_code_history_upd
    after update
    on hymn.core_shared_code
    for each row
execute function hymn.core_shared_code_history_upd();
drop trigger if exists core_shared_code_history_del on hymn.core_shared_code;
create trigger core_shared_code_history_del
    after delete
    on hymn.core_shared_code
    for each row
execute function hymn.core_shared_code_history_del();
drop table if exists hymn.core_table_obj_mapping_history cascade;
create table hymn.core_table_obj_mapping_history
(
    operation text,
    stamp timestamp,
    table_name text,
    obj_api text
);
create or replace function hymn.core_table_obj_mapping_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_table_obj_mapping_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_table_obj_mapping_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_table_obj_mapping_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.core_table_obj_mapping_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.core_table_obj_mapping_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists core_table_obj_mapping_history_ins on hymn.core_table_obj_mapping;
create trigger core_table_obj_mapping_history_ins
    after insert
    on hymn.core_table_obj_mapping
    for each row
execute function hymn.core_table_obj_mapping_history_ins();
drop trigger if exists core_table_obj_mapping_history_upd on hymn.core_table_obj_mapping;
create trigger core_table_obj_mapping_history_upd
    after update
    on hymn.core_table_obj_mapping
    for each row
execute function hymn.core_table_obj_mapping_history_upd();
drop trigger if exists core_table_obj_mapping_history_del on hymn.core_table_obj_mapping;
create trigger core_table_obj_mapping_history_del
    after delete
    on hymn.core_table_obj_mapping
    for each row
execute function hymn.core_table_obj_mapping_history_del();
