-- table constraint
alter table hymn.core_b_object_field
    add unique (object_id,api);
alter table hymn.core_b_object_field_perm
    add unique (role_id,field_id);
alter table hymn.core_b_object_layout
    add unique (object_id,name);
alter table hymn.core_b_object_perm
    add unique (role_id,object_id);
alter table hymn.core_b_object_trigger
    add unique (object_id,api);
alter table hymn.core_b_object_type
    add unique (object_id,name);
alter table hymn.core_b_object_type_layout
    add unique (role_id,object_id,type_id,layout_id);
alter table hymn.core_b_object_type_perm
    add unique (role_id,type_id);
alter table hymn.core_button_perm
    add unique (role_id,button_id);
alter table hymn.core_dict_item
    add unique (dict_id,code);
alter table hymn.core_module_function_perm
    add unique (role_id,module_function_id);


-- column constraint
alter table hymn.core_account
    add foreign key (org_id) references hymn.core_org on delete restrict;
alter table hymn.core_account
    add foreign key (role_id) references hymn.core_role on delete restrict;
alter table hymn.core_account_menu_layout
    add foreign key (account_id) references hymn.core_account on delete cascade;
alter table hymn.core_account_menu_layout
    add check ( client_type in ('browser', 'mobile') );
alter table hymn.core_account_object_view
    add foreign key (account_id) references hymn.core_account on delete cascade;
alter table hymn.core_account_object_view
    add foreign key (object_id) references hymn.core_b_object on delete cascade;
alter table hymn.core_b_object
    add constraint core_b_object_api_uk unique (api);
alter table hymn.core_b_object
    add check ( type in ('custom', 'module', 'remote') );
alter table hymn.core_b_object
    add foreign key (module_api) references hymn.core_module on delete cascade;
alter table hymn.core_b_object_field
    add foreign key (object_id) references hymn.core_b_object on delete cascade;
alter table hymn.core_b_object_field_perm
    add foreign key (role_id) references hymn.core_role on delete cascade;
alter table hymn.core_b_object_field_perm
    add foreign key (object_id) references hymn.core_b_object on delete cascade;
alter table hymn.core_b_object_field_perm
    add foreign key (field_id) references hymn.core_b_object_field on delete cascade;
alter table hymn.core_b_object_layout
    add foreign key (object_id) references hymn.core_b_object on delete cascade;
alter table hymn.core_b_object_mapping
    add foreign key (source_object_id) references hymn.core_b_object on delete cascade;
alter table hymn.core_b_object_mapping
    add foreign key (source_type_id) references hymn.core_b_object_type on delete cascade;
alter table hymn.core_b_object_mapping
    add foreign key (target_object_id) references hymn.core_b_object on delete cascade;
alter table hymn.core_b_object_mapping
    add foreign key (target_type_id) references hymn.core_b_object_type on delete cascade;
alter table hymn.core_b_object_mapping_item
    add foreign key (source_field_id) references hymn.core_b_object_field on delete cascade;
alter table hymn.core_b_object_mapping_item
    add foreign key (target_field_id) references hymn.core_b_object_field on delete cascade;
alter table hymn.core_b_object_mapping_item
    add foreign key (ref_field1_id) references hymn.core_b_object_field on delete cascade;
alter table hymn.core_b_object_mapping_item
    add foreign key (ref_field1_object_id) references hymn.core_b_object on delete cascade;
alter table hymn.core_b_object_mapping_item
    add foreign key (ref_field2_id) references hymn.core_b_object_field on delete cascade;
alter table hymn.core_b_object_mapping_item
    add foreign key (ref_field2_object_id) references hymn.core_b_object on delete cascade;
alter table hymn.core_b_object_mapping_item
    add foreign key (ref_field3_id) references hymn.core_b_object_field on delete cascade;
alter table hymn.core_b_object_mapping_item
    add foreign key (ref_field3_object_id) references hymn.core_b_object on delete cascade;
alter table hymn.core_b_object_mapping_item
    add foreign key (ref_field4_id) references hymn.core_b_object_field on delete cascade;
alter table hymn.core_b_object_mapping_item
    add foreign key (ref_field4_object_id) references hymn.core_b_object on delete cascade;
alter table hymn.core_b_object_perm
    add foreign key (role_id) references hymn.core_role on delete cascade;
alter table hymn.core_b_object_perm
    add foreign key (object_id) references hymn.core_b_object on delete cascade;
alter table hymn.core_b_object_trigger
    add check ( lang in ('javascript') );
alter table hymn.core_b_object_trigger
    add check ( event in ('BEFORE_INSERT', 'BEFORE_UPDATE', 'BEFORE_UPSERT', 'BEFORE_DELETE', 'AFTER_INSERT', 'AFTER_UPDATE', 'AFTER_UPSERT', 'AFTER_DELETE') );
alter table hymn.core_b_object_type
    add foreign key (object_id) references hymn.core_b_object on delete cascade;
alter table hymn.core_b_object_type_available_options
    add foreign key (type_id) references hymn.core_b_object_type on delete cascade;
alter table hymn.core_b_object_type_available_options
    add foreign key (field_id) references hymn.core_b_object_field on delete cascade;
alter table hymn.core_b_object_type_available_options
    add foreign key (dict_item_id) references hymn.core_dict_item on delete cascade;
alter table hymn.core_b_object_type_layout
    add foreign key (role_id) references hymn.core_role on delete cascade;
alter table hymn.core_b_object_type_layout
    add foreign key (object_id) references hymn.core_b_object on delete cascade;
alter table hymn.core_b_object_type_layout
    add foreign key (type_id) references hymn.core_b_object_type on delete cascade;
alter table hymn.core_b_object_type_layout
    add foreign key (layout_id) references hymn.core_b_object_layout on delete cascade;
alter table hymn.core_b_object_type_perm
    add foreign key (role_id) references hymn.core_role on delete cascade;
alter table hymn.core_b_object_type_perm
    add foreign key (type_id) references hymn.core_b_object_type on delete cascade;
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
alter table hymn.core_business_code_ref
    add foreign key (org_id) references hymn.core_org on delete cascade;
alter table hymn.core_business_code_ref
    add foreign key (role_id) references hymn.core_role on delete cascade;
alter table hymn.core_button_perm
    add foreign key (role_id) references hymn.core_role on delete cascade;
create index core_button_perm_role_id_idx
    on hymn.core_button_perm (role_id);
alter table hymn.core_button_perm
    add foreign key (button_id) references hymn.core_custom_button on delete cascade;
create index core_config_key_idx
    on hymn.core_config (key);
alter table hymn.core_cron_job
    add foreign key (shared_code_id) references hymn.core_shared_code on delete restrict;
alter table hymn.core_custom_button
    add constraint core_custom_button_api_uk unique (api);
alter table hymn.core_custom_button
    add check ( client_type in ('browser', 'mobile') );
alter table hymn.core_custom_button
    add check ( action in ('eval', 'open_in_current_tab', 'open_in_new_tab', 'open_in_new_window') );
alter table hymn.core_custom_component
    add constraint core_custom_component_api_uk unique (api);
alter table hymn.core_custom_interface
    add constraint core_custom_interface_api_uk unique (api);
alter table hymn.core_custom_interface
    add check ( lang in ('javascript') );
alter table hymn.core_custom_menu_item
    add check ( path_type in ('path', 'url') );
alter table hymn.core_custom_menu_item
    add check ( action in ('iframe', 'current_tab', 'new_tab') );
alter table hymn.core_custom_menu_item
    add check ( client_type in ('browser', 'mobile') );
alter table hymn.core_custom_page
    add constraint core_custom_page_api_uk unique (api);
alter table hymn.core_dict
    add constraint core_dict_api_uk unique (api);
alter table hymn.core_dict_item
    add foreign key (dict_id) references hymn.core_dict on delete cascade;
alter table hymn.core_menu_item_perm
    add foreign key (role_id) references hymn.core_role on delete cascade;
create index core_menu_item_perm_role_id_idx
    on hymn.core_menu_item_perm (role_id);
alter table hymn.core_menu_item_perm
    add foreign key (menu_item_id) references hymn.core_custom_menu_item on delete cascade;
alter table hymn.core_module_function
    add foreign key (module_api) references hymn.core_module on delete cascade;
alter table hymn.core_module_function
    add constraint core_module_function_api_uk unique (api);
alter table hymn.core_module_function_perm
    add foreign key (role_id) references hymn.core_role on delete cascade;
create index core_module_function_perm_role_id_idx
    on hymn.core_module_function_perm (role_id);
alter table hymn.core_module_function_perm
    add foreign key (module_function_id) references hymn.core_module_function on delete cascade;
create index core_org_parent_id_idx
    on hymn.core_org (parent_id);
alter table hymn.core_shared_code
    add constraint core_shared_code_api_uk unique (api);
alter table hymn.core_shared_code
    add check ( lang in ('javascript') );
alter table hymn.core_table_obj_mapping
    add constraint core_table_obj_mapping_obj_api_uk unique (obj_api);
