create or replace function hymn.sys_core_account_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_account_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_account_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_account_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_account_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_account_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_account_history_ins
    after insert
    on hymn.sys_core_account
    for each row
execute function hymn.sys_core_account_history_ins();
create trigger sys_core_account_history_upd
    after update
    on hymn.sys_core_account
    for each row
execute function hymn.sys_core_account_history_upd();
create trigger sys_core_account_history_del
    after delete
    on hymn.sys_core_account
    for each row
execute function hymn.sys_core_account_history_del();



create or replace function hymn.sys_core_account_menu_layout_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_account_menu_layout_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_account_menu_layout_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_account_menu_layout_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_account_menu_layout_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_account_menu_layout_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_account_menu_layout_history_ins
    after insert
    on hymn.sys_core_account_menu_layout
    for each row
execute function hymn.sys_core_account_menu_layout_history_ins();
create trigger sys_core_account_menu_layout_history_upd
    after update
    on hymn.sys_core_account_menu_layout
    for each row
execute function hymn.sys_core_account_menu_layout_history_upd();
create trigger sys_core_account_menu_layout_history_del
    after delete
    on hymn.sys_core_account_menu_layout
    for each row
execute function hymn.sys_core_account_menu_layout_history_del();



create or replace function hymn.sys_core_account_object_view_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_account_object_view_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_account_object_view_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_account_object_view_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_account_object_view_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_account_object_view_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_account_object_view_history_ins
    after insert
    on hymn.sys_core_account_object_view
    for each row
execute function hymn.sys_core_account_object_view_history_ins();
create trigger sys_core_account_object_view_history_upd
    after update
    on hymn.sys_core_account_object_view
    for each row
execute function hymn.sys_core_account_object_view_history_upd();
create trigger sys_core_account_object_view_history_del
    after delete
    on hymn.sys_core_account_object_view
    for each row
execute function hymn.sys_core_account_object_view_history_del();



create or replace function hymn.sys_core_b_object_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_b_object_history_ins
    after insert
    on hymn.sys_core_b_object
    for each row
execute function hymn.sys_core_b_object_history_ins();
create trigger sys_core_b_object_history_upd
    after update
    on hymn.sys_core_b_object
    for each row
execute function hymn.sys_core_b_object_history_upd();
create trigger sys_core_b_object_history_del
    after delete
    on hymn.sys_core_b_object
    for each row
execute function hymn.sys_core_b_object_history_del();



create or replace function hymn.sys_core_b_object_field_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_field_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_field_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_field_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_field_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_field_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_b_object_field_history_ins
    after insert
    on hymn.sys_core_b_object_field
    for each row
execute function hymn.sys_core_b_object_field_history_ins();
create trigger sys_core_b_object_field_history_upd
    after update
    on hymn.sys_core_b_object_field
    for each row
execute function hymn.sys_core_b_object_field_history_upd();
create trigger sys_core_b_object_field_history_del
    after delete
    on hymn.sys_core_b_object_field
    for each row
execute function hymn.sys_core_b_object_field_history_del();



create or replace function hymn.sys_core_b_object_field_perm_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_field_perm_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_field_perm_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_field_perm_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_field_perm_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_field_perm_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_b_object_field_perm_history_ins
    after insert
    on hymn.sys_core_b_object_field_perm
    for each row
execute function hymn.sys_core_b_object_field_perm_history_ins();
create trigger sys_core_b_object_field_perm_history_upd
    after update
    on hymn.sys_core_b_object_field_perm
    for each row
execute function hymn.sys_core_b_object_field_perm_history_upd();
create trigger sys_core_b_object_field_perm_history_del
    after delete
    on hymn.sys_core_b_object_field_perm
    for each row
execute function hymn.sys_core_b_object_field_perm_history_del();



create or replace function hymn.sys_core_b_object_layout_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_layout_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_layout_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_layout_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_layout_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_layout_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_b_object_layout_history_ins
    after insert
    on hymn.sys_core_b_object_layout
    for each row
execute function hymn.sys_core_b_object_layout_history_ins();
create trigger sys_core_b_object_layout_history_upd
    after update
    on hymn.sys_core_b_object_layout
    for each row
execute function hymn.sys_core_b_object_layout_history_upd();
create trigger sys_core_b_object_layout_history_del
    after delete
    on hymn.sys_core_b_object_layout
    for each row
execute function hymn.sys_core_b_object_layout_history_del();



create or replace function hymn.sys_core_b_object_mapping_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_mapping_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_mapping_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_mapping_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_mapping_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_mapping_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_b_object_mapping_history_ins
    after insert
    on hymn.sys_core_b_object_mapping
    for each row
execute function hymn.sys_core_b_object_mapping_history_ins();
create trigger sys_core_b_object_mapping_history_upd
    after update
    on hymn.sys_core_b_object_mapping
    for each row
execute function hymn.sys_core_b_object_mapping_history_upd();
create trigger sys_core_b_object_mapping_history_del
    after delete
    on hymn.sys_core_b_object_mapping
    for each row
execute function hymn.sys_core_b_object_mapping_history_del();



create or replace function hymn.sys_core_b_object_mapping_item_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_mapping_item_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_mapping_item_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_mapping_item_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_mapping_item_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_mapping_item_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_b_object_mapping_item_history_ins
    after insert
    on hymn.sys_core_b_object_mapping_item
    for each row
execute function hymn.sys_core_b_object_mapping_item_history_ins();
create trigger sys_core_b_object_mapping_item_history_upd
    after update
    on hymn.sys_core_b_object_mapping_item
    for each row
execute function hymn.sys_core_b_object_mapping_item_history_upd();
create trigger sys_core_b_object_mapping_item_history_del
    after delete
    on hymn.sys_core_b_object_mapping_item
    for each row
execute function hymn.sys_core_b_object_mapping_item_history_del();



create or replace function hymn.sys_core_b_object_perm_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_perm_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_perm_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_perm_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_perm_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_perm_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_b_object_perm_history_ins
    after insert
    on hymn.sys_core_b_object_perm
    for each row
execute function hymn.sys_core_b_object_perm_history_ins();
create trigger sys_core_b_object_perm_history_upd
    after update
    on hymn.sys_core_b_object_perm
    for each row
execute function hymn.sys_core_b_object_perm_history_upd();
create trigger sys_core_b_object_perm_history_del
    after delete
    on hymn.sys_core_b_object_perm
    for each row
execute function hymn.sys_core_b_object_perm_history_del();



create or replace function hymn.sys_core_b_object_record_layout_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_record_layout_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_record_layout_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_record_layout_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_record_layout_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_record_layout_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_b_object_record_layout_history_ins
    after insert
    on hymn.sys_core_b_object_record_layout
    for each row
execute function hymn.sys_core_b_object_record_layout_history_ins();
create trigger sys_core_b_object_record_layout_history_upd
    after update
    on hymn.sys_core_b_object_record_layout
    for each row
execute function hymn.sys_core_b_object_record_layout_history_upd();
create trigger sys_core_b_object_record_layout_history_del
    after delete
    on hymn.sys_core_b_object_record_layout
    for each row
execute function hymn.sys_core_b_object_record_layout_history_del();



create or replace function hymn.sys_core_b_object_record_type_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_record_type_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_record_type_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_record_type_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_record_type_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_record_type_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_b_object_record_type_history_ins
    after insert
    on hymn.sys_core_b_object_record_type
    for each row
execute function hymn.sys_core_b_object_record_type_history_ins();
create trigger sys_core_b_object_record_type_history_upd
    after update
    on hymn.sys_core_b_object_record_type
    for each row
execute function hymn.sys_core_b_object_record_type_history_upd();
create trigger sys_core_b_object_record_type_history_del
    after delete
    on hymn.sys_core_b_object_record_type
    for each row
execute function hymn.sys_core_b_object_record_type_history_del();



create or replace function hymn.sys_core_b_object_record_type_available_options_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_record_type_available_options_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_record_type_available_options_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_record_type_available_options_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_record_type_available_options_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_record_type_available_options_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_b_object_record_type_available_options_history_ins
    after insert
    on hymn.sys_core_b_object_record_type_available_options
    for each row
execute function hymn.sys_core_b_object_record_type_available_options_history_ins();
create trigger sys_core_b_object_record_type_available_options_history_upd
    after update
    on hymn.sys_core_b_object_record_type_available_options
    for each row
execute function hymn.sys_core_b_object_record_type_available_options_history_upd();
create trigger sys_core_b_object_record_type_available_options_history_del
    after delete
    on hymn.sys_core_b_object_record_type_available_options
    for each row
execute function hymn.sys_core_b_object_record_type_available_options_history_del();



create or replace function hymn.sys_core_b_object_record_type_perm_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_record_type_perm_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_record_type_perm_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_record_type_perm_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_record_type_perm_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_record_type_perm_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_b_object_record_type_perm_history_ins
    after insert
    on hymn.sys_core_b_object_record_type_perm
    for each row
execute function hymn.sys_core_b_object_record_type_perm_history_ins();
create trigger sys_core_b_object_record_type_perm_history_upd
    after update
    on hymn.sys_core_b_object_record_type_perm
    for each row
execute function hymn.sys_core_b_object_record_type_perm_history_upd();
create trigger sys_core_b_object_record_type_perm_history_del
    after delete
    on hymn.sys_core_b_object_record_type_perm
    for each row
execute function hymn.sys_core_b_object_record_type_perm_history_del();



create or replace function hymn.sys_core_b_object_trigger_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_trigger_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_trigger_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_trigger_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_b_object_trigger_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_b_object_trigger_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_b_object_trigger_history_ins
    after insert
    on hymn.sys_core_b_object_trigger
    for each row
execute function hymn.sys_core_b_object_trigger_history_ins();
create trigger sys_core_b_object_trigger_history_upd
    after update
    on hymn.sys_core_b_object_trigger
    for each row
execute function hymn.sys_core_b_object_trigger_history_upd();
create trigger sys_core_b_object_trigger_history_del
    after delete
    on hymn.sys_core_b_object_trigger
    for each row
execute function hymn.sys_core_b_object_trigger_history_del();



create or replace function hymn.sys_core_business_code_ref_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_business_code_ref_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_business_code_ref_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_business_code_ref_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_business_code_ref_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_business_code_ref_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_business_code_ref_history_ins
    after insert
    on hymn.sys_core_business_code_ref
    for each row
execute function hymn.sys_core_business_code_ref_history_ins();
create trigger sys_core_business_code_ref_history_upd
    after update
    on hymn.sys_core_business_code_ref
    for each row
execute function hymn.sys_core_business_code_ref_history_upd();
create trigger sys_core_business_code_ref_history_del
    after delete
    on hymn.sys_core_business_code_ref
    for each row
execute function hymn.sys_core_business_code_ref_history_del();



create or replace function hymn.sys_core_button_perm_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_button_perm_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_button_perm_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_button_perm_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_button_perm_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_button_perm_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_button_perm_history_ins
    after insert
    on hymn.sys_core_button_perm
    for each row
execute function hymn.sys_core_button_perm_history_ins();
create trigger sys_core_button_perm_history_upd
    after update
    on hymn.sys_core_button_perm
    for each row
execute function hymn.sys_core_button_perm_history_upd();
create trigger sys_core_button_perm_history_del
    after delete
    on hymn.sys_core_button_perm
    for each row
execute function hymn.sys_core_button_perm_history_del();



create or replace function hymn.sys_core_config_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_config_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_config_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_config_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_config_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_config_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_config_history_ins
    after insert
    on hymn.sys_core_config
    for each row
execute function hymn.sys_core_config_history_ins();
create trigger sys_core_config_history_upd
    after update
    on hymn.sys_core_config
    for each row
execute function hymn.sys_core_config_history_upd();
create trigger sys_core_config_history_del
    after delete
    on hymn.sys_core_config
    for each row
execute function hymn.sys_core_config_history_del();



create or replace function hymn.sys_core_cron_job_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_cron_job_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_cron_job_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_cron_job_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_cron_job_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_cron_job_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_cron_job_history_ins
    after insert
    on hymn.sys_core_cron_job
    for each row
execute function hymn.sys_core_cron_job_history_ins();
create trigger sys_core_cron_job_history_upd
    after update
    on hymn.sys_core_cron_job
    for each row
execute function hymn.sys_core_cron_job_history_upd();
create trigger sys_core_cron_job_history_del
    after delete
    on hymn.sys_core_cron_job
    for each row
execute function hymn.sys_core_cron_job_history_del();



create or replace function hymn.sys_core_custom_button_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_custom_button_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_custom_button_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_custom_button_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_custom_button_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_custom_button_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_custom_button_history_ins
    after insert
    on hymn.sys_core_custom_button
    for each row
execute function hymn.sys_core_custom_button_history_ins();
create trigger sys_core_custom_button_history_upd
    after update
    on hymn.sys_core_custom_button
    for each row
execute function hymn.sys_core_custom_button_history_upd();
create trigger sys_core_custom_button_history_del
    after delete
    on hymn.sys_core_custom_button
    for each row
execute function hymn.sys_core_custom_button_history_del();



create or replace function hymn.sys_core_custom_component_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_custom_component_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_custom_component_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_custom_component_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_custom_component_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_custom_component_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_custom_component_history_ins
    after insert
    on hymn.sys_core_custom_component
    for each row
execute function hymn.sys_core_custom_component_history_ins();
create trigger sys_core_custom_component_history_upd
    after update
    on hymn.sys_core_custom_component
    for each row
execute function hymn.sys_core_custom_component_history_upd();
create trigger sys_core_custom_component_history_del
    after delete
    on hymn.sys_core_custom_component
    for each row
execute function hymn.sys_core_custom_component_history_del();



create or replace function hymn.sys_core_custom_interface_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_custom_interface_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_custom_interface_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_custom_interface_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_custom_interface_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_custom_interface_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_custom_interface_history_ins
    after insert
    on hymn.sys_core_custom_interface
    for each row
execute function hymn.sys_core_custom_interface_history_ins();
create trigger sys_core_custom_interface_history_upd
    after update
    on hymn.sys_core_custom_interface
    for each row
execute function hymn.sys_core_custom_interface_history_upd();
create trigger sys_core_custom_interface_history_del
    after delete
    on hymn.sys_core_custom_interface
    for each row
execute function hymn.sys_core_custom_interface_history_del();



create or replace function hymn.sys_core_custom_menu_item_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_custom_menu_item_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_custom_menu_item_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_custom_menu_item_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_custom_menu_item_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_custom_menu_item_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_custom_menu_item_history_ins
    after insert
    on hymn.sys_core_custom_menu_item
    for each row
execute function hymn.sys_core_custom_menu_item_history_ins();
create trigger sys_core_custom_menu_item_history_upd
    after update
    on hymn.sys_core_custom_menu_item
    for each row
execute function hymn.sys_core_custom_menu_item_history_upd();
create trigger sys_core_custom_menu_item_history_del
    after delete
    on hymn.sys_core_custom_menu_item
    for each row
execute function hymn.sys_core_custom_menu_item_history_del();



create or replace function hymn.sys_core_custom_page_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_custom_page_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_custom_page_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_custom_page_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_custom_page_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_custom_page_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_custom_page_history_ins
    after insert
    on hymn.sys_core_custom_page
    for each row
execute function hymn.sys_core_custom_page_history_ins();
create trigger sys_core_custom_page_history_upd
    after update
    on hymn.sys_core_custom_page
    for each row
execute function hymn.sys_core_custom_page_history_upd();
create trigger sys_core_custom_page_history_del
    after delete
    on hymn.sys_core_custom_page
    for each row
execute function hymn.sys_core_custom_page_history_del();



create or replace function hymn.sys_core_dict_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_dict_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_dict_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_dict_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_dict_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_dict_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_dict_history_ins
    after insert
    on hymn.sys_core_dict
    for each row
execute function hymn.sys_core_dict_history_ins();
create trigger sys_core_dict_history_upd
    after update
    on hymn.sys_core_dict
    for each row
execute function hymn.sys_core_dict_history_upd();
create trigger sys_core_dict_history_del
    after delete
    on hymn.sys_core_dict
    for each row
execute function hymn.sys_core_dict_history_del();



create or replace function hymn.sys_core_dict_item_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_dict_item_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_dict_item_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_dict_item_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_dict_item_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_dict_item_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_dict_item_history_ins
    after insert
    on hymn.sys_core_dict_item
    for each row
execute function hymn.sys_core_dict_item_history_ins();
create trigger sys_core_dict_item_history_upd
    after update
    on hymn.sys_core_dict_item
    for each row
execute function hymn.sys_core_dict_item_history_upd();
create trigger sys_core_dict_item_history_del
    after delete
    on hymn.sys_core_dict_item
    for each row
execute function hymn.sys_core_dict_item_history_del();



create or replace function hymn.sys_core_menu_item_perm_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_menu_item_perm_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_menu_item_perm_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_menu_item_perm_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_menu_item_perm_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_menu_item_perm_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_menu_item_perm_history_ins
    after insert
    on hymn.sys_core_menu_item_perm
    for each row
execute function hymn.sys_core_menu_item_perm_history_ins();
create trigger sys_core_menu_item_perm_history_upd
    after update
    on hymn.sys_core_menu_item_perm
    for each row
execute function hymn.sys_core_menu_item_perm_history_upd();
create trigger sys_core_menu_item_perm_history_del
    after delete
    on hymn.sys_core_menu_item_perm
    for each row
execute function hymn.sys_core_menu_item_perm_history_del();



create or replace function hymn.sys_core_module_function_perm_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_module_function_perm_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_module_function_perm_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_module_function_perm_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_module_function_perm_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_module_function_perm_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_module_function_perm_history_ins
    after insert
    on hymn.sys_core_module_function_perm
    for each row
execute function hymn.sys_core_module_function_perm_history_ins();
create trigger sys_core_module_function_perm_history_upd
    after update
    on hymn.sys_core_module_function_perm
    for each row
execute function hymn.sys_core_module_function_perm_history_upd();
create trigger sys_core_module_function_perm_history_del
    after delete
    on hymn.sys_core_module_function_perm
    for each row
execute function hymn.sys_core_module_function_perm_history_del();



create or replace function hymn.sys_core_org_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_org_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_org_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_org_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_org_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_org_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_org_history_ins
    after insert
    on hymn.sys_core_org
    for each row
execute function hymn.sys_core_org_history_ins();
create trigger sys_core_org_history_upd
    after update
    on hymn.sys_core_org
    for each row
execute function hymn.sys_core_org_history_upd();
create trigger sys_core_org_history_del
    after delete
    on hymn.sys_core_org
    for each row
execute function hymn.sys_core_org_history_del();



create or replace function hymn.sys_core_role_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_role_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_role_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_role_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_role_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_role_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_role_history_ins
    after insert
    on hymn.sys_core_role
    for each row
execute function hymn.sys_core_role_history_ins();
create trigger sys_core_role_history_upd
    after update
    on hymn.sys_core_role
    for each row
execute function hymn.sys_core_role_history_upd();
create trigger sys_core_role_history_del
    after delete
    on hymn.sys_core_role
    for each row
execute function hymn.sys_core_role_history_del();



create or replace function hymn.sys_core_shared_code_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_shared_code_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_shared_code_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_shared_code_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.sys_core_shared_code_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.sys_core_shared_code_history select 'd',now(),old.*;
    return null;
end
$$;
create trigger sys_core_shared_code_history_ins
    after insert
    on hymn.sys_core_shared_code
    for each row
execute function hymn.sys_core_shared_code_history_ins();
create trigger sys_core_shared_code_history_upd
    after update
    on hymn.sys_core_shared_code
    for each row
execute function hymn.sys_core_shared_code_history_upd();
create trigger sys_core_shared_code_history_del
    after delete
    on hymn.sys_core_shared_code
    for each row
execute function hymn.sys_core_shared_code_history_del();


