-- drop schema hymn cascade;
-- create schema hymn;


drop table if exists hymn.sys_core_account_history cascade;
create table hymn.sys_core_account_history
(
    operation    text,
    stamp        timestamp,
    id           text,
    lock_time    timestamp,
    name         text,
    username     text,
    password     text,
    online_rule  text,
    active       boolean,
    admin        boolean,
    root         boolean,
    leader_id    text,
    org_id       text,
    role_id      text,
    create_by_id text,
    create_by    text,
    modify_by_id text,
    modify_by    text,
    create_date  timestamp,
    modify_date  timestamp
);



drop table if exists hymn.sys_core_org_history cascade;
create table hymn.sys_core_org_history
(
    operation          text,
    stamp              timestamp,
    id                 text,
    name               text,
    director_id        text,
    deputy_director_id text,
    parent_id          uuid,
    create_by_id       text,
    create_by          text,
    modify_by_id       text,
    modify_by          text,
    create_date        timestamp,
    modify_date        timestamp
);

drop table if exists hymn.sys_core_role_history cascade;
create table hymn.sys_core_role_history
(
    operation    text,
    stamp        timestamp,
    id           text,
    name         text,
    remark       text,
    create_by_id text,
    create_by    text,
    modify_by_id text,
    modify_by    text,
    create_date  timestamp,
    modify_date  timestamp
);

drop table if exists hymn.sys_core_config_history cascade;
create table hymn.sys_core_config_history
(
    operation    text,
    stamp        timestamp,
    id           text,
    key          text,
    value        text,
    create_by_id text,
    create_by    text,
    modify_by_id text,
    modify_by    text,
    create_date  timestamp,
    modify_date  timestamp
);

drop table if exists hymn.sys_core_account_menu_layout_history cascade;
create table hymn.sys_core_account_menu_layout_history
(
    operation    text,
    stamp        timestamp,
    id           text,
    account_id   text,
    client_type  text,
    layout_json  text,
    create_by_id text,
    create_by    text,
    modify_by_id text,
    modify_by    text,
    create_date  timestamp,
    modify_date  timestamp
);



drop table if exists hymn.sys_core_account_object_view_history cascade;
create table hymn.sys_core_account_object_view_history
(
    operation    text,
    stamp        timestamp,
    id           text,
    copy_id      uuid,
    remark       text,
    global_view  boolean,
    account_id   text,
    object_id    text,
    name         text,
    view_json    text,
    create_by_id text,
    create_by    text,
    modify_by_id text,
    modify_by    text,
    create_date  timestamp,
    modify_date  timestamp
);



drop table if exists hymn.sys_core_custom_button_history cascade;
create table hymn.sys_core_custom_button_history
(
    operation    text,
    stamp        timestamp,
    id           text,
    remark       text,
    object_id    uuid,
    name         text,
    api          text,
    client_type  text,
    action       text,
    content      text,
    create_by_id text,
    create_by    text,
    modify_by_id text,
    modify_by    text,
    create_date  timestamp,
    modify_date  timestamp
);



drop table if exists hymn.sys_core_custom_component_history cascade;
create table hymn.sys_core_custom_component_history
(
    operation    text,
    stamp        timestamp,
    id           text,
    api          text,
    name         text,
    code         text,
    create_by_id text,
    create_by    text,
    modify_by_id text,
    modify_by    text,
    create_date  timestamp,
    modify_date  timestamp
);



drop table if exists hymn.sys_core_custom_interface_history cascade;
create table hymn.sys_core_custom_interface_history
(
    operation    text,
    stamp        timestamp,
    id           text,
    api          text,
    name         text,
    code         text,
    active       boolean,
    lang         text,
    option_text  text,
    remark       text,
    create_by_id text,
    create_by    text,
    modify_by_id text,
    modify_by    text,
    create_date  timestamp,
    modify_date  timestamp
);



drop table if exists hymn.sys_core_custom_menu_item_history cascade;
create table hymn.sys_core_custom_menu_item_history
(
    operation    text,
    stamp        timestamp,
    id           text,
    name         text,
    path         text,
    path_type    text,
    action       text,
    client_type  text,
    icon         text,
    create_by_id text,
    create_by    text,
    modify_by_id text,
    modify_by    text,
    create_date  timestamp,
    modify_date  timestamp
);



drop table if exists hymn.sys_core_custom_page_history cascade;
create table hymn.sys_core_custom_page_history
(
    operation    text,
    stamp        timestamp,
    id           text,
    api          text,
    name         text,
    template     text,
    static       boolean,
    remark       text,
    create_by_id text,
    create_by    text,
    modify_by_id text,
    modify_by    text,
    create_date  timestamp,
    modify_date  timestamp
);



drop table if exists hymn.sys_core_dict_history cascade;
create table hymn.sys_core_dict_history
(
    operation      text,
    stamp          timestamp,
    id             text,
    field_id       uuid,
    parent_dict_id uuid,
    name           text,
    api            text,
    remark         text,
    create_by_id   text,
    create_by      text,
    modify_by_id   text,
    modify_by      text,
    create_date    timestamp,
    modify_date    timestamp
);



drop table if exists hymn.sys_core_dict_item_history cascade;
create table hymn.sys_core_dict_item_history
(
    operation    text,
    stamp        timestamp,
    id           text,
    dict_id      text,
    name         text,
    code         text,
    parent_code  text,
    create_by_id text,
    create_by    text,
    modify_by_id text,
    modify_by    text,
    create_date  timestamp,
    modify_date  timestamp
);



drop table if exists hymn.sys_core_b_object_history cascade;
create table hymn.sys_core_b_object_history
(
    operation    text,
    stamp        timestamp,
    id           text,
    name         text,
    api          text,
    code         text,
    active       boolean,
    module_api   text,
    remark       text,
    create_by_id text,
    create_by    text,
    modify_by_id text,
    modify_by    text,
    create_date  timestamp,
    modify_date  timestamp
);


drop table if exists hymn.sys_core_b_object_field_history cascade;
create table hymn.sys_core_b_object_field_history
(
    operation        text,
    stamp            timestamp,
    id               text,
    source_column    text,
    object_id        text,
    name             text,
    api              text,
    type             text,
    active           boolean,
    formula          text,
    max_length       integer,
    min_length       integer,
    visible_row      integer,
    dict_id          uuid,
    master_field_id  uuid,
    optional_number  integer,
    ref_id           uuid,
    ref_list_label   text,
    ref_allow_delete boolean,
    query_filter     text,
    s_id             uuid,
    s_field_id       uuid,
    s_type           text,
    gen_rule         text,
    remark           text,
    help             text,
    tmp              text,
    standard_type    text,
    create_by_id     text,
    create_by        text,
    modify_by_id     text,
    modify_by        text,
    create_date      timestamp,
    modify_date      timestamp
);


drop table if exists hymn.sys_core_b_object_layout_history cascade;
create table hymn.sys_core_b_object_layout_history
(
    operation               text,
    stamp                   timestamp,
    id                      text,
    object_id               text,
    name                    text,
    api                     text,
    remark                  text,
    rel_field_json_arr      text,
    pc_read_layout_json     text,
    pc_edit_layout_json     text,
    mobile_read_layout_json text,
    mobile_edit_layout_json text,
    preview_layout_json     text,
    create_by_id            text,
    create_by               text,
    modify_by_id            text,
    modify_by               text,
    create_date             timestamp,
    modify_date             timestamp
);



drop table if exists hymn.sys_core_b_object_record_type_history cascade;
create table hymn.sys_core_b_object_record_type_history
(
    operation    text,
    stamp        timestamp,
    id           text,
    object_id    text,
    name         text,
    active       boolean,
    remark       text,
    create_by_id text,
    create_by    text,
    modify_by_id text,
    modify_by    text,
    create_date  timestamp,
    modify_date  timestamp
);

drop table if exists hymn.sys_core_b_object_record_type_available_options_history cascade;
create table hymn.sys_core_b_object_record_type_available_options_history
(
    operation      text,
    stamp          timestamp,
    id             text,
    record_type_id text,
    field_id       text,
    dict_item_id   text,
    create_by_id   text,
    create_by      text,
    modify_by_id   text,
    modify_by      text,
    create_date    timestamp,
    modify_date    timestamp
);


drop table if exists hymn.sys_core_b_object_record_layout_history cascade;
create table hymn.sys_core_b_object_record_layout_history
(
    operation      text,
    stamp          timestamp,
    id             text,
    role_id        text,
    object_id      text,
    record_type_id text,
    layout_id      text,
    create_by_id   text,
    create_by      text,
    modify_by_id   text,
    modify_by      text,
    create_date    timestamp,
    modify_date    timestamp
);



drop table if exists hymn.sys_core_b_object_trigger_history cascade;
create table hymn.sys_core_b_object_trigger_history
(
    operation    text,
    stamp        timestamp,
    id           text,
    active       boolean,
    remark       text,
    object_id    text,
    name         text,
    api          text,
    lang         text,
    option_text  text,
    ord          integer,
    event        integer,
    code         text,
    create_by_id text,
    create_by    text,
    modify_by_id text,
    modify_by    text,
    create_date  timestamp,
    modify_date  timestamp
);



drop table if exists hymn.sys_core_b_object_mapping_history cascade;
create table hymn.sys_core_b_object_mapping_history
(
    operation             text,
    stamp                 timestamp,
    id                    text,
    source_object_id      text,
    target_object_id      text,
    source_record_type_id text,
    target_record_type_id text,
    create_by_id          text,
    create_by             text,
    modify_by_id          text,
    modify_by             text,
    create_date           timestamp,
    modify_date           timestamp
);


drop table if exists hymn.sys_core_b_object_mapping_item_history cascade;
create table hymn.sys_core_b_object_mapping_item_history
(
    operation             text,
    stamp                 timestamp,
    id                    text,
    mapping_id            text,
    source_object_api     text,
    target_object_api     text,
    source_field_api      text,
    target_field_api      text,
    ref_field1_api        text,
    ref_field1_object_api text,
    ref_field2_api        text,
    ref_field2_object_api text,
    ref_field3_api        text,
    ref_field3_object_api text,
    ref_field4_api        text,
    ref_field4_object_api text,
    create_by_id          text,
    create_by             text,
    modify_by_id          text,
    modify_by             text,
    create_date           timestamp,
    modify_date           timestamp
);



drop table if exists hymn.sys_core_module_function_history;
create table hymn.sys_core_module_function_history
(
    operation   text,
    stamp       timestamp,
    id          text,
    module_name text,
    api         text,
    name        text,
    remark      text,
    create_date text
);



drop table if exists hymn.sys_core_module_function_perm_history;
create table hymn.sys_core_module_function_perm_history
(
    operation          text,
    stamp              timestamp,
    id                 text,
    role_id            text,
    module_function_id text,
    perm               boolean,
    create_by_id       text,
    create_by          text,
    modify_by_id       text,
    modify_by          text,
    create_date        timestamp,
    modify_date        timestamp
);

drop table if exists hymn.sys_core_button_perm_history cascade;
create table hymn.sys_core_button_perm_history
(
    operation    text,
    stamp        timestamp,
    id           text,
    role_id      text,
    button_id    text,
    visible      boolean,
    create_by_id text,
    create_by    text,
    modify_by_id text,
    modify_by    text,
    create_date  timestamp,
    modify_date  timestamp
);



drop table if exists hymn.sys_core_menu_item_perm_history cascade;
create table hymn.sys_core_menu_item_perm_history
(
    operation    text,
    stamp        timestamp,
    id           text,
    role_id      text,
    menu_item_id text,
    visible      boolean,
    create_by_id text,
    create_by    text,
    modify_by_id text,
    modify_by    text,
    create_date  timestamp,
    modify_date  timestamp
);



drop table if exists hymn.sys_core_b_object_perm_history cascade;
create table hymn.sys_core_b_object_perm_history
(
    operation               text,
    stamp                   timestamp,
    id                      text,
    role_id                 text,
    object_id               text,
    object_api_name         text,
    ins                     bool,
    upd                     bool,
    del                     bool,
    que                     bool,
    query_with_account_tree bool,
    query_with_dept         bool,
    query_with_dept_tree    bool,
    query_all               bool,
    edit_all                bool,
    create_by_id            text,
    create_by               text,
    modify_by_id            text,
    modify_by               text,
    create_date             timestamp,
    modify_date             timestamp
);



drop table if exists hymn.sys_core_b_object_field_perm_history cascade;
create table hymn.sys_core_b_object_field_perm_history
(
    operation    text,
    stamp        timestamp,
    id           text,
    role_id      text,
    object_id    text,
    field_id     text,
    p_read       bool,
    p_edit       bool,
    create_by_id text,
    create_by    text,
    modify_by_id text,
    modify_by    text,
    create_date  timestamp,
    modify_date  timestamp
);



drop table if exists hymn.sys_core_b_object_record_type_perm_history cascade;
create table hymn.sys_core_b_object_record_type_perm_history
(
    operation      text,
    stamp          timestamp,
    id             text,
    role_id        text,
    object_id      text,
    record_type_id text,
    visible        boolean,
    create_by_id   text,
    create_by      text,
    modify_by_id   text,
    modify_by      text,
    create_date    timestamp,
    modify_date    timestamp
);


drop table if exists hymn.sys_core_data_share_history cascade;
create table hymn.sys_core_data_share_history
(
    operation       text,
    stamp           timestamp,
    object_api_name text,
    data_id         text,
    role_id         text,
    org_code        text,
    user_id         uuid,
    read_only       boolean
);



drop table if exists hymn.sys_core_shared_code_history;
create table hymn.sys_core_shared_code_history
(
    operation    text,
    stamp        timestamp,
    id           text,
    api          text,
    type         text,
    code         text,
    lang         text,
    option_text  text,
    create_by_id text,
    create_by    text,
    modify_by_id text,
    modify_by    text,
    create_date  timestamp,
    modify_date  timestamp
);


drop table if exists hymn.sys_core_business_code_ref_history;
create table hymn.sys_core_business_code_ref_history
(
    operation      text,
    stamp          timestamp,
    id             text,
    trigger_id     text,
    interface_id   text,
    shared_code_id text,
    object_id      text,
    field_id       text,
    org_id         text,
    role_id        text,
    create_by_id   text,
    create_by      text,
    modify_by_id   text,
    modify_by      text,
    create_date    timestamp,
    modify_date    timestamp
);

drop table if exists hymn.sys_core_cron_job_history;
create table hymn.sys_core_cron_job_history
(
    operation       text,
    stamp           timestamp,
    id              text,
    active          boolean,
    shared_code_id  text,
    cron            text,
    start_date_time timestamp,
    end_date_time   timestamp,
    create_by_id    text,
    create_by       text,
    modify_by_id    text,
    modify_by       text,
    create_date     timestamp,
    modify_date     timestamp
);


