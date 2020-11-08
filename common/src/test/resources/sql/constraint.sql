alter table hymn.sys_core_account
    add foreign key (org_id) references hymn.sys_core_org;
alter table hymn.sys_core_account
    add foreign key (role_id) references hymn.sys_core_role;
alter table hymn.sys_core_account
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_account
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_role
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_role
    add foreign key (modify_by_id) references hymn.sys_core_account;


alter table hymn.sys_core_org
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_org
    add foreign key (modify_by_id) references hymn.sys_core_account;


alter table hymn.sys_core_account_menu_layout
    add foreign key (account_id) references hymn.sys_core_account;
alter table hymn.sys_core_account_menu_layout
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_account_menu_layout
    add foreign key (modify_by_id) references hymn.sys_core_account;


alter table hymn.sys_core_account_object_view
    add foreign key (account_id) references hymn.sys_core_account;
alter table hymn.sys_core_account_object_view
    add foreign key (object_id) references hymn.sys_core_b_object;
alter table hymn.sys_core_account_object_view
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_account_object_view
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_custom_button
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_custom_button
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_custom_component
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_custom_component
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_custom_interface
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_custom_interface
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_custom_menu_item
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_custom_menu_item
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_custom_page
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_custom_page
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_dict
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_dict
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_dict_item
    add foreign key (dict_id) references hymn.sys_core_dict;
alter table hymn.sys_core_dict_item
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_dict_item
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_b_object
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_b_object
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_b_object_field
    add foreign key (object_id) references hymn.sys_core_b_object;
alter table hymn.sys_core_b_object_field
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_b_object_field
    add foreign key (modify_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_b_object_field
    add constraint object_field_type_check check ( type in ('text', 'check_box', 'select',
                                                            'multiple_select', 'integer', 'float',
                                                            'money', 'date', 'datetime', 'picture',
                                                            'percentage', 'relation'));
alter table hymn.sys_core_b_object_field
    add constraint object_field_standard_type_check check (
            standard_type in (
                              'create_by_id',
                              'create_by',
                              'modify_by_id',
                              'modify_by',
                              'create_date',
                              'modify_date',
                              'org_id'
            )
        );


alter table hymn.sys_core_b_object_field_perm
    add foreign key (role_id) references hymn.sys_core_role;
alter table hymn.sys_core_b_object_field_perm
    add foreign key (object_id) references hymn.sys_core_b_object;
alter table hymn.sys_core_b_object_field_perm
    add foreign key (field_id) references hymn.sys_core_b_object_field;
alter table hymn.sys_core_b_object_field_perm
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_b_object_field_perm
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_b_object_layout
    add foreign key (object_id) references hymn.sys_core_b_object;
alter table hymn.sys_core_b_object_layout
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_b_object_layout
    add foreign key (modify_by_id) references hymn.sys_core_account;


alter table hymn.sys_core_b_object_mapping
    add foreign key (source_object_id) references hymn.sys_core_b_object;
alter table hymn.sys_core_b_object_mapping
    add foreign key (target_object_id) references hymn.sys_core_b_object;
alter table hymn.sys_core_b_object_mapping
    add foreign key (source_record_type_id) references hymn.sys_core_b_object_record_type;
alter table hymn.sys_core_b_object_mapping
    add foreign key (target_record_type_id) references hymn.sys_core_b_object_record_type;
alter table hymn.sys_core_b_object_mapping
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_b_object_mapping
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_b_object_mapping_item
    add foreign key (source_object_api) references hymn.sys_core_b_object;
alter table hymn.sys_core_b_object_mapping_item
    add foreign key (target_object_api) references hymn.sys_core_b_object;
alter table hymn.sys_core_b_object_mapping_item
    add foreign key (source_field_api) references hymn.sys_core_b_object_field;
alter table hymn.sys_core_b_object_mapping_item
    add foreign key (target_field_api) references hymn.sys_core_b_object_field;
alter table hymn.sys_core_b_object_mapping_item
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_b_object_mapping_item
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_b_object_perm
    add foreign key (role_id) references hymn.sys_core_role;
alter table hymn.sys_core_b_object_perm
    add foreign key (object_id) references hymn.sys_core_b_object;
alter table hymn.sys_core_b_object_perm
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_b_object_perm
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_b_object_record_layout
    add foreign key (role_id) references hymn.sys_core_role;
alter table hymn.sys_core_b_object_record_layout
    add foreign key (record_type_id) references hymn.sys_core_b_object_record_type;
alter table hymn.sys_core_b_object_record_layout
    add foreign key (layout_id) references hymn.sys_core_b_object_layout;
alter table hymn.sys_core_b_object_record_layout
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_b_object_record_layout
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_b_object_record_type
    add foreign key (object_id) references hymn.sys_core_b_object;
alter table hymn.sys_core_b_object_record_type
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_b_object_record_type
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_b_object_record_type_available_options
    add foreign key (record_type_id) references hymn.sys_core_b_object_record_type;
alter table hymn.sys_core_b_object_record_type_available_options
    add foreign key (field_id) references hymn.sys_core_b_object_field;
alter table hymn.sys_core_b_object_record_type_available_options
    add foreign key (dict_item_id) references hymn.sys_core_dict_item;
alter table hymn.sys_core_b_object_record_type_available_options
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_b_object_record_type_available_options
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_b_object_record_type_perm
    add foreign key (role_id) references hymn.sys_core_role;
alter table hymn.sys_core_b_object_record_type_perm
    add foreign key (object_id) references hymn.sys_core_b_object;
alter table hymn.sys_core_b_object_record_type_perm
    add foreign key (record_type_id) references hymn.sys_core_b_object_record_type;
alter table hymn.sys_core_b_object_record_type_perm
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_b_object_record_type_perm
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_b_object_trigger
    add foreign key (object_id) references hymn.sys_core_b_object;
alter table hymn.sys_core_b_object_trigger
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_b_object_trigger
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_business_code_ref
    add foreign key (trigger_id) references hymn.sys_core_b_object_trigger;
alter table hymn.sys_core_business_code_ref
    add foreign key (interface_id) references hymn.sys_core_custom_interface;
alter table hymn.sys_core_business_code_ref
    add foreign key (shared_code_id) references hymn.sys_core_shared_code;
alter table hymn.sys_core_business_code_ref
    add foreign key (object_id) references hymn.sys_core_b_object;
alter table hymn.sys_core_business_code_ref
    add foreign key (field_id) references hymn.sys_core_b_object_field;
alter table hymn.sys_core_business_code_ref
    add foreign key (org_id) references hymn.sys_core_org;
alter table hymn.sys_core_business_code_ref
    add foreign key (role_id) references hymn.sys_core_role;
alter table hymn.sys_core_business_code_ref
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_business_code_ref
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_button_perm
    add foreign key (role_id) references hymn.sys_core_role;
alter table hymn.sys_core_button_perm
    add foreign key (button_id) references hymn.sys_core_custom_button;
alter table hymn.sys_core_button_perm
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_button_perm
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_config
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_config
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_cron_job
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_cron_job
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_menu_item_perm
    add foreign key (role_id) references hymn.sys_core_role;
alter table hymn.sys_core_menu_item_perm
    add foreign key (menu_item_id) references hymn.sys_core_custom_menu_item;
alter table hymn.sys_core_menu_item_perm
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_menu_item_perm
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_module_function_perm
    add foreign key (role_id) references hymn.sys_core_role;
alter table hymn.sys_core_module_function_perm
    add foreign key (module_function_id) references hymn.sys_core_module_function;
alter table hymn.sys_core_module_function_perm
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_module_function_perm
    add foreign key (modify_by_id) references hymn.sys_core_account;

alter table hymn.sys_core_shared_code
    add foreign key (create_by_id) references hymn.sys_core_account;
alter table hymn.sys_core_shared_code
    add foreign key (modify_by_id) references hymn.sys_core_account;
