-- check
alter table hymn.core_b_object
    add check ( type in ('custom', 'module', 'remote') );
alter table hymn.core_custom_button
    add check ( action in
                ('eval', 'open_in_current_tab', 'open_in_new_tab', 'open_in_new_window') );
alter table hymn.core_custom_button
    add check ( client_type in ('browser', 'android') );

alter table hymn.core_b_object_field
    add check ( ref_delete_policy in ('cascade', 'restrict', 'null'));
alter table hymn.core_b_object_field
    add check ( type in ('text', 'check_box', 'check_box_group', 'select',
                         'integer', 'float', 'money', 'date', 'datetime', 'master_slave',
                         'reference', 'mreference', 'summary', 'auto', 'picture'));
alter table hymn.core_b_object_field
    add check (
            standard_type in (
                              'create_by_id', 'create_by', 'modify_by_id', 'modify_by',
                              'create_date',
                              'modify_date', 'org_id', 'lock_state', 'name', 'type_id', 'owner_id'
            ));
alter table hymn.core_b_object_field
    add check ( s_type in ('sum', 'count', 'min', 'max'));

alter table hymn.core_b_object_trigger
    add check ( event in
                ('before_insert', 'before_update', 'before_upsert', 'before_delete', 'after_insert',
                 'after_update', 'after_upsert', 'after_delete') );
alter table hymn.core_b_object_trigger
    add constraint core_b_object_trigger_api_key unique (api);

alter table hymn.core_custom_menu_item
    add check ( path_type in ('path', 'url'));
alter table hymn.core_custom_menu_item
    add check ( action in ('iframe', 'current_tab', 'new_tab'));
alter table hymn.core_custom_menu_item
    add check ( client_type in ('browser', 'android'));

alter table hymn.core_account_menu_layout
    add check ( client_type in ('browser', 'android') );



-- constraint
-- unique
alter table hymn.core_custom_interface
    add constraint core_custom_interface_api_uk unique (api);
alter table hymn.core_dict
    add constraint core_dict_api_uk unique (api);
alter table hymn.core_b_object_field
    add constraint core_b_object_field_api_uk unique (object_id, api);


-- foreign key
alter table hymn.core_account
    add foreign key (role_id) references hymn.core_role on delete restrict;
alter table hymn.core_account
    add foreign key (org_id) references hymn.core_org on delete restrict;

alter table hymn.core_account_object_view
    add foreign key (object_id) references hymn.core_b_object on delete cascade;
alter table hymn.core_account_object_view
    add foreign key (account_id) references hymn.core_account on delete cascade;


alter table hymn.core_b_object_field
    add foreign key (object_id) references hymn.core_b_object on delete cascade;

alter table hymn.core_account_menu_layout
    add foreign key (account_id) references hymn.core_account on delete cascade;

alter table hymn.core_b_object_type
    add foreign key (object_id) references hymn.core_b_object on delete cascade;

alter table hymn.core_b_object_layout
    add foreign key (object_id) references hymn.core_b_object on delete cascade;

alter table hymn.core_b_object_trigger
    add foreign key (object_id) references hymn.core_b_object on delete cascade;

alter table hymn.core_b_object_field_perm
    add foreign key (role_id) references hymn.core_role on delete cascade;
alter table hymn.core_b_object_field_perm
    add foreign key (object_id) references hymn.core_b_object on delete cascade;
alter table hymn.core_b_object_field_perm
    add foreign key (field_id) references hymn.core_b_object_field on delete cascade;

alter table hymn.core_b_object_perm
    add foreign key (role_id) references hymn.core_role on delete cascade;
alter table hymn.core_b_object_perm
    add foreign key (object_id) references hymn.core_b_object on delete cascade;


alter table hymn.core_b_object_type_layout
    add foreign key (role_id) references hymn.core_role on delete cascade;
alter table hymn.core_b_object_type_layout
    add foreign key (layout_id) references hymn.core_b_object_layout on delete cascade;
alter table hymn.core_b_object_type_layout
    add foreign key (type_id) references hymn.core_b_object_type on delete cascade;

alter table hymn.core_b_object_type_perm
    add foreign key (role_id) references hymn.core_role on delete cascade;
alter table hymn.core_b_object_type_perm
    add foreign key (object_id) references hymn.core_b_object on delete cascade;
alter table hymn.core_b_object_type_perm
    add foreign key (type_id) references hymn.core_b_object_type on delete cascade;

alter table hymn.core_business_code_ref
    add foreign key (role_id) references hymn.core_role on delete cascade;
alter table hymn.core_business_code_ref
    add foreign key (org_id) references hymn.core_org on delete cascade;
alter table hymn.core_business_code_ref
    add foreign key (trigger_id) references hymn.core_b_object_trigger on delete cascade;
alter table hymn.core_business_code_ref
    add foreign key (interface_id) references hymn.core_custom_interface on delete cascade;
alter table hymn.core_business_code_ref
    add foreign key (shared_code_id) references hymn.core_shared_code on delete cascade;
alter table hymn.core_business_code_ref
    add foreign key (object_id) references hymn.core_b_object on delete cascade;
alter table hymn.core_business_code_ref
    add foreign key (field_id) references hymn.core_b_object_field on delete cascade;

alter table hymn.core_button_perm
    add foreign key (role_id) references hymn.core_role on delete cascade;
alter table hymn.core_button_perm
    add foreign key (button_id) references hymn.core_custom_button on delete cascade;


alter table hymn.core_data_share
    add foreign key (role_id) references hymn.core_role on delete cascade;
alter table hymn.core_data_share
    add foreign key (org_id) references hymn.core_org on delete cascade;
alter table hymn.core_data_share
    add foreign key (account_id) references hymn.core_role on delete cascade;


alter table hymn.core_dict_item
    add foreign key (dict_id) references hymn.core_dict on delete cascade;

alter table hymn.core_menu_item_perm
    add foreign key (role_id) references hymn.core_role on delete cascade;
alter table hymn.core_menu_item_perm
    add foreign key (menu_item_id) references hymn.core_custom_menu_item on delete cascade;

alter table hymn.core_b_object_type_available_options
    add foreign key (type_id) references hymn.core_b_object_type on delete cascade;
alter table hymn.core_b_object_type_available_options
    add foreign key (field_id) references hymn.core_b_object_field on delete cascade;
alter table hymn.core_b_object_type_available_options
    add foreign key (dict_item_id) references hymn.core_dict_item on delete cascade;
alter table hymn.core_b_object_type_available_options
    add foreign key (object_id) references hymn.core_b_object on delete cascade;

alter table hymn.core_module_function_perm
    add foreign key (role_id) references hymn.core_role on delete cascade;
alter table hymn.core_module_function_perm
    add foreign key (module_function_id) references hymn.core_module_function on delete cascade;

alter table hymn.core_b_object_mapping
    add foreign key (source_object_id) references hymn.core_b_object on delete cascade;
alter table hymn.core_b_object_mapping
    add foreign key (target_object_id) references hymn.core_b_object on delete cascade;
alter table hymn.core_b_object_mapping
    add foreign key (source_type_id) references hymn.core_b_object_type on delete cascade;
alter table hymn.core_b_object_mapping
    add foreign key (target_type_id) references hymn.core_b_object_type on delete cascade;
alter table hymn.core_b_object_mapping_item
    add foreign key (source_object_api) references hymn.core_b_object on delete cascade;
alter table hymn.core_b_object_mapping_item
    add foreign key (target_object_api) references hymn.core_b_object on delete cascade;
alter table hymn.core_b_object_mapping_item
    add foreign key (source_field_api) references hymn.core_b_object_field on delete cascade;
alter table hymn.core_b_object_mapping_item
    add foreign key (target_field_api) references hymn.core_b_object_field on delete cascade;


-- index

create index account_create_by_id_idx on hymn.core_account (create_by_id);
create index account_modify_by_id_idx on hymn.core_account (modify_by_id);


create index account_menu_layout_create_by_id_idx on hymn.core_account_menu_layout (create_by_id);
create index account_menu_layout_modify_by_id_idx on hymn.core_account_menu_layout (modify_by_id);


create index account_object_view_create_by_id_idx on hymn.core_account_object_view (create_by_id);
create index account_object_view_modify_by_id_idx on hymn.core_account_object_view (modify_by_id);


create index b_object_create_by_id_idx on hymn.core_b_object (create_by_id);
create index b_object_modify_by_id_idx on hymn.core_b_object (modify_by_id);


create index b_object_field_create_by_id_idx on hymn.core_b_object_field (create_by_id);
create index b_object_field_modify_by_id_idx on hymn.core_b_object_field (modify_by_id);


create index b_object_field_perm_create_by_id_idx on hymn.core_b_object_field_perm (create_by_id);
create index b_object_field_perm_modify_by_id_idx on hymn.core_b_object_field_perm (modify_by_id);


create index b_object_layout_create_by_id_idx on hymn.core_b_object_layout (create_by_id);
create index b_object_layout_modify_by_id_idx on hymn.core_b_object_layout (modify_by_id);


create index b_object_mapping_create_by_id_idx on hymn.core_b_object_mapping (create_by_id);
create index b_object_mapping_modify_by_id_idx on hymn.core_b_object_mapping (modify_by_id);


create index b_object_mapping_item_create_by_id_idx on hymn.core_b_object_mapping_item (create_by_id);
create index b_object_mapping_item_modify_by_id_idx on hymn.core_b_object_mapping_item (modify_by_id);


create index b_object_perm_create_by_id_idx on hymn.core_b_object_perm (create_by_id);
create index b_object_perm_modify_by_id_idx on hymn.core_b_object_perm (modify_by_id);


create index b_object_type_layout_create_by_id_idx on hymn.core_b_object_type_layout (create_by_id);
create index b_object_type_layout_modify_by_id_idx on hymn.core_b_object_type_layout (modify_by_id);


create index b_object_type_create_by_id_idx on hymn.core_b_object_type (create_by_id);
create index b_object_type_modify_by_id_idx on hymn.core_b_object_type (modify_by_id);


create index b_object_type_available_options_create_by_id_idx on hymn.core_b_object_type_available_options (create_by_id);
create index b_object_type_available_options_modify_by_id_idx on hymn.core_b_object_type_available_options (modify_by_id);


create index b_object_type_perm_create_by_id_idx on hymn.core_b_object_type_perm (create_by_id);
create index b_object_type_perm_modify_by_id_idx on hymn.core_b_object_type_perm (modify_by_id);


create index b_object_trigger_create_by_id_idx on hymn.core_b_object_trigger (create_by_id);
create index b_object_trigger_modify_by_id_idx on hymn.core_b_object_trigger (modify_by_id);


create index business_code_ref_create_by_id_idx on hymn.core_business_code_ref (create_by_id);
create index business_code_ref_modify_by_id_idx on hymn.core_business_code_ref (modify_by_id);


create index button_perm_create_by_id_idx on hymn.core_button_perm (create_by_id);
create index button_perm_modify_by_id_idx on hymn.core_button_perm (modify_by_id);


create index config_create_by_id_idx on hymn.core_config (create_by_id);
create index config_modify_by_id_idx on hymn.core_config (modify_by_id);


create index cron_job_create_by_id_idx on hymn.core_cron_job (create_by_id);
create index cron_job_modify_by_id_idx on hymn.core_cron_job (modify_by_id);


create index custom_button_create_by_id_idx on hymn.core_custom_button (create_by_id);
create index custom_button_modify_by_id_idx on hymn.core_custom_button (modify_by_id);


create index custom_component_create_by_id_idx on hymn.core_custom_component (create_by_id);
create index custom_component_modify_by_id_idx on hymn.core_custom_component (modify_by_id);


create index custom_interface_create_by_id_idx on hymn.core_custom_interface (create_by_id);
create index custom_interface_modify_by_id_idx on hymn.core_custom_interface (modify_by_id);


create index custom_menu_item_create_by_id_idx on hymn.core_custom_menu_item (create_by_id);
create index custom_menu_item_modify_by_id_idx on hymn.core_custom_menu_item (modify_by_id);


create index custom_page_create_by_id_idx on hymn.core_custom_page (create_by_id);
create index custom_page_modify_by_id_idx on hymn.core_custom_page (modify_by_id);


create index dict_create_by_id_idx on hymn.core_dict (create_by_id);
create index dict_modify_by_id_idx on hymn.core_dict (modify_by_id);


create index dict_item_create_by_id_idx on hymn.core_dict_item (create_by_id);
create index dict_item_modify_by_id_idx on hymn.core_dict_item (modify_by_id);


create index menu_item_perm_create_by_id_idx on hymn.core_menu_item_perm (create_by_id);
create index menu_item_perm_modify_by_id_idx on hymn.core_menu_item_perm (modify_by_id);


create index module_function_perm_create_by_id_idx on hymn.core_module_function_perm (create_by_id);
create index module_function_perm_modify_by_id_idx on hymn.core_module_function_perm (modify_by_id);


create index org_create_by_id_idx on hymn.core_org (create_by_id);
create index org_modify_by_id_idx on hymn.core_org (modify_by_id);


create index role_create_by_id_idx on hymn.core_role (create_by_id);
create index role_modify_by_id_idx on hymn.core_role (modify_by_id);


create index shared_code_create_by_id_idx on hymn.core_shared_code (create_by_id);
create index shared_code_modify_by_id_idx on hymn.core_shared_code (modify_by_id);
