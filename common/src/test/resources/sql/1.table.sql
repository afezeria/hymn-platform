drop schema if exists hymn cascade;
create schema hymn;

drop schema if exists hymn_view cascade;
create schema hymn_view;

drop table if exists hymn.core_account cascade;
create table hymn.core_account
(
    id           text primary key   default replace(public.uuid_generate_v4()::text, '-', ''),
    lock_time    timestamp not null default make_timestamp(1970, 1, 1, 0, 0, 0),
    name         text      not null,
    username     text      not null,
    password     text      not null,
    online_rule  text      not null,
    active       bool      not null,
    admin        bool      not null,
    root         bool               default false not null,
    leader_id    text,
    org_id       text      not null,
    role_id      text      not null,
    create_by_id text      not null,
    create_by    text      not null,
    modify_by_id text      not null,
    modify_by    text      not null,
    create_date  timestamp not null,
    modify_date  timestamp not null,
    text001      text,
    text002      text,
    text003      text,
    text004      text,
    text005      text,
    text006      text,
    text007      text,
    text008      text,
    text009      text,
    text010      text,
    bigint001    bigint,
    bigint002    bigint,
    bigint003    bigint,
    bigint004    bigint,
    bigint005    bigint,
    double001    double precision,
    double002    double precision,
    double003    double precision,
    double004    double precision,
    double005    double precision,
    decimal001   decimal,
    decimal002   decimal,
    decimal003   decimal,
    decimal004   decimal,
    decimal005   decimal,
    datetime001  timestamptz,
    datetime002  timestamptz,
    datetime003  timestamptz,
    datetime004  timestamptz,
    datetime005  timestamptz
);
comment on table hymn.core_account is '用户';
comment on column hymn.core_account.lock_time is '锁定时间，当前时间小于等于lock_time表示帐号被锁定';
comment on column hymn.core_account.online_rule is '在线规则，限制每客户端在线数量或登录ip等, none为无限制';
comment on column hymn.core_account.active is '是否启用';
comment on column hymn.core_account.admin is '是否是管理员';
comment on column hymn.core_account.leader_id is '直接上级id';
comment on column hymn.core_account.org_id is '所属组织id';
comment on column hymn.core_account.root is '是否是初始帐号';
comment on column hymn.core_account.text001 is '##ignore 预留字段';
comment on column hymn.core_account.text002 is '##ignore 预留字段';
comment on column hymn.core_account.text003 is '##ignore 预留字段';
comment on column hymn.core_account.text004 is '##ignore 预留字段';
comment on column hymn.core_account.text005 is '##ignore 预留字段';
comment on column hymn.core_account.text006 is '##ignore 预留字段';
comment on column hymn.core_account.text007 is '##ignore 预留字段';
comment on column hymn.core_account.text008 is '##ignore 预留字段';
comment on column hymn.core_account.text009 is '##ignore 预留字段';
comment on column hymn.core_account.text010 is '##ignore 预留字段';
comment on column hymn.core_account.bigint001 is '##ignore 预留字段';
comment on column hymn.core_account.bigint002 is '##ignore 预留字段';
comment on column hymn.core_account.bigint003 is '##ignore 预留字段';
comment on column hymn.core_account.bigint004 is '##ignore 预留字段';
comment on column hymn.core_account.bigint005 is '##ignore 预留字段';
comment on column hymn.core_account.double001 is '##ignore 预留字段';
comment on column hymn.core_account.double002 is '##ignore 预留字段';
comment on column hymn.core_account.double003 is '##ignore 预留字段';
comment on column hymn.core_account.double004 is '##ignore 预留字段';
comment on column hymn.core_account.double005 is '##ignore 预留字段';
comment on column hymn.core_account.decimal001 is '##ignore 预留字段';
comment on column hymn.core_account.decimal002 is '##ignore 预留字段';
comment on column hymn.core_account.decimal003 is '##ignore 预留字段';
comment on column hymn.core_account.decimal004 is '##ignore 预留字段';
comment on column hymn.core_account.decimal005 is '##ignore 预留字段';
comment on column hymn.core_account.datetime001 is '##ignore 预留字段';
comment on column hymn.core_account.datetime002 is '##ignore 预留字段';
comment on column hymn.core_account.datetime003 is '##ignore 预留字段';
comment on column hymn.core_account.datetime004 is '##ignore 预留字段';
comment on column hymn.core_account.datetime005 is '##ignore 预留字段';



drop table if exists hymn.core_org cascade;
create table hymn.core_org
(
    id                 text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    name               text      not null,
    director_id        text,
    deputy_director_id text,
    parent_id          text,
    create_by_id       text      not null,
    create_by          text      not null,
    modify_by_id       text      not null,
    modify_by          text      not null,
    create_date        timestamp not null,
    modify_date        timestamp not null,
    text001            text,
    text002            text,
    text003            text,
    text004            text,
    text005            text,
    bigint001          bigint,
    bigint002          bigint,
    bigint003          bigint,
    bigint004          bigint,
    bigint005          bigint
);
comment on table hymn.core_org is '组织';
-- comment on column hymn.core_org.code is '组织代码，数字加小写字母的字符串，父组织的代码为子组织前缀，每个组织最多36个子组织，eg： 总公司：1a 子公司1：1a0 子公司2：1a1';
comment on column hymn.core_org.parent_id is '上级组织id';
comment on column hymn.core_org.director_id is '部门领导id';
comment on column hymn.core_org.deputy_director_id is '部门副领导id';
comment on column hymn.core_org.text001 is '##ignore 预留字段';
comment on column hymn.core_org.text002 is '##ignore 预留字段';
comment on column hymn.core_org.text003 is '##ignore 预留字段';
comment on column hymn.core_org.text004 is '##ignore 预留字段';
comment on column hymn.core_org.text005 is '##ignore 预留字段';
comment on column hymn.core_org.bigint001 is '##ignore 预留字段';
comment on column hymn.core_org.bigint002 is '##ignore 预留字段';
comment on column hymn.core_org.bigint003 is '##ignore 预留字段';
comment on column hymn.core_org.bigint004 is '##ignore 预留字段';
comment on column hymn.core_org.bigint005 is '##ignore 预留字段';

drop table if exists hymn.core_role cascade;
create table hymn.core_role
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    name         text      not null,
--     type         text      not null,
    remark       text,
    create_by_id text      not null,
    create_by    text      not null,
    modify_by_id text      not null,
    modify_by    text      not null,
    create_date  timestamp not null,
    modify_date  timestamp not null
);
comment on table hymn.core_role is '角色';
comment on column hymn.core_role.name is '角色名称';

drop table if exists hymn.core_config cascade;
create table hymn.core_config
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    key          text      not null,
    value        text      not null,
    create_by_id text      not null,
    create_by    text      not null,
    modify_by_id text      not null,
    modify_by    text      not null,
    create_date  timestamp not null,
    modify_date  timestamp not null
);
comment on table hymn.core_config is '系统配置表';

drop table if exists hymn.core_account_menu_layout cascade;
create table hymn.core_account_menu_layout
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    account_id   text      not null,
    client_type  text      not null,
    layout_json  text      not null,
    create_by_id text      not null,
    create_by    text      not null,
    modify_by_id text      not null,
    modify_by    text      not null,
    create_date  timestamp not null,
    modify_date  timestamp not null
);
comment on table hymn.core_account_menu_layout is '用户侧边栏菜单布局';
comment on column hymn.core_account_menu_layout.account_id is '用户id';
comment on column hymn.core_account_menu_layout.client_type is '客户端类型';
comment on column hymn.core_account_menu_layout.layout_json is '布局json字符串';



drop table if exists hymn.core_account_object_view cascade;
create table hymn.core_account_object_view
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    copy_id      text,
    remark       text,
    global_view  bool      not null,
    default_view bool      not null,
    account_id   text      not null,
    object_id    text      not null,
    name         text      not null,
    view_json    text      not null,
    create_by_id text      not null,
    create_by    text      not null,
    modify_by_id text      not null,
    modify_by    text      not null,
    create_date  timestamp not null,
    modify_date  timestamp not null
);
comment on table hymn.core_account_object_view is '用户业务对象列表视图';
comment on column hymn.core_account_object_view.copy_id is '源数据id，修改视图后该字段置空';
comment on column hymn.core_account_object_view.account_id is '所属用户id';
comment on column hymn.core_account_object_view.object_id is '所属对象id';
comment on column hymn.core_account_object_view.name is '视图名称';
comment on column hymn.core_account_object_view.view_json is '视图结构';
comment on column hymn.core_account_object_view.default_view is '是否是默认视图';



drop table if exists hymn.core_custom_button cascade;
create table hymn.core_custom_button
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    remark       text,
    object_id    text,
    name         text      not null,
    api          text      not null,
    client_type  text      not null,
    action       text      not null,
    content      text      not null,
    create_by_id text      not null,
    create_by    text      not null,
    modify_by_id text      not null,
    modify_by    text      not null,
    create_date  timestamp not null,
    modify_date  timestamp not null
);
comment on table hymn.core_custom_button is '自定义按钮';
comment on column hymn.core_custom_button.object_id is '业务对象id，不为空时表示该按钮只能在该对象相关页面中使用';
comment on column hymn.core_custom_button.client_type is '客户端类型，表示只能用在特定类型客户端中';
comment on column hymn.core_custom_button.action is '按钮行为 可选值： eval 执行js代码, open_in_current_tab 在当前页面中打开链接, open_in_new_tab 在新标签页中打开链接, open_in_new_window 在新窗口中打开链接';
comment on column hymn.core_custom_button.content is '按钮内容，当action为eval时为js代码，其他情况为url';



drop table if exists hymn.core_custom_component cascade;
create table hymn.core_custom_component
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    api          text      not null,
    name         text      not null,
    code         text      not null,
    create_by_id text      not null,
    create_by    text      not null,
    modify_by_id text      not null,
    modify_by    text      not null,
    create_date  timestamp not null,
    modify_date  timestamp not null
);
comment on table hymn.core_custom_component is '自定义组件';
comment on column hymn.core_custom_component.api is 'api名称，唯一标识';
comment on column hymn.core_custom_component.name is '组件在页面上的显示名称';
comment on column hymn.core_custom_component.code is '组件html代码';



drop table if exists hymn.core_custom_interface cascade;
create table hymn.core_custom_interface
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    api          text      not null,
    name         text      not null,
    code         text      not null,
    active       bool      not null,
    lang         text      not null,
    option_text  text,
    remark       text,
    create_by_id text      not null,
    create_by    text      not null,
    modify_by_id text      not null,
    modify_by    text      not null,
    create_date  timestamp not null,
    modify_date  timestamp not null
);
comment on table hymn.core_custom_interface is '自定义接口';
comment on column hymn.core_custom_interface.api is '接口api名称，唯一标识';
comment on column hymn.core_custom_interface.name is '接口名称';
comment on column hymn.core_custom_interface.code is '接口代码';
comment on column hymn.core_custom_interface.active is '是否启用';
comment on column hymn.core_custom_interface.lang is '语言';
comment on column hymn.core_custom_interface.option_text is '用于给编译器或其他组件设置参数(格式参照具体实现）';



drop table if exists hymn.core_custom_menu_item cascade;
create table hymn.core_custom_menu_item
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    name         text      not null,
    path         text      not null,
    path_type    text      not null,
    action       text      not null,
    client_type  text      not null,
    icon         text      not null,
    create_by_id text      not null,
    create_by    text      not null,
    modify_by_id text      not null,
    modify_by    text      not null,
    create_date  timestamp not null,
    modify_date  timestamp not null
);
comment on table hymn.core_custom_menu_item is '菜单项';
comment on column hymn.core_custom_menu_item.name is '菜单项名称';
comment on column hymn.core_custom_menu_item.path is 'url path';
comment on column hymn.core_custom_menu_item.path_type is 'path类型 可选值： path 路径, url 外部url';
comment on column hymn.core_custom_menu_item.action is '行为 可选值： iframe 在iframe中打开, current_tab 当前标签页中打开, new_tab 新标签页中打开';

comment on column hymn.core_custom_menu_item.client_type is '客户端类型  可选值： browser 浏览器, android 安卓';
comment on column hymn.core_custom_menu_item.icon is '图标';



drop table if exists hymn.core_custom_page cascade;
create table hymn.core_custom_page
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    api          text      not null,
    name         text      not null,
    template     text      not null,
    static       bool      not null,
    remark       text,
    create_by_id text      not null,
    create_by    text      not null,
    modify_by_id text      not null,
    modify_by    text      not null,
    create_date  timestamp not null,
    modify_date  timestamp not null
);
comment on table hymn.core_custom_page is '自定义页面';
comment on column hymn.core_custom_page.api is 'api名称，唯一标识';
comment on column hymn.core_custom_page.template is '页面模板';
comment on column hymn.core_custom_page.name is '自定义页面名称，用于后台查看';
comment on column hymn.core_custom_page.static is '是否为静态页面';



drop table if exists hymn.core_dict cascade;
create table hymn.core_dict
(
    id             text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    field_id       text,
    parent_dict_id text,
    name           text      not null,
    api            text      not null,
    remark         text,
    create_by_id   text      not null,
    create_by      text      not null,
    modify_by_id   text      not null,
    modify_by      text      not null,
    create_date    timestamp not null,
    modify_date    timestamp not null
);
comment on table hymn.core_dict is '数据字典';
comment on column hymn.core_dict.field_id is '表明当前字典是指定字段的字典，不能通用';
comment on column hymn.core_dict.parent_dict_id is '表明当前字典值依赖与其他字典';
comment on column hymn.core_dict.name is '字典名称';
comment on column hymn.core_dict.api is 'api名称';



drop table if exists hymn.core_dict_item cascade;
create table hymn.core_dict_item
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    dict_id      text      not null,
    name         text      not null,
    code         text      not null,
    parent_code  text,
    create_by_id text      not null,
    create_by    text      not null,
    modify_by_id text      not null,
    modify_by    text      not null,
    create_date  timestamp not null,
    modify_date  timestamp not null
);
comment on table hymn.core_dict_item is '字典项';
comment on column hymn.core_dict_item.dict_id is '所属字典id';
comment on column hymn.core_dict_item.name is '字典项名称';
comment on column hymn.core_dict_item.code is '字典项编码';
comment on column hymn.core_dict_item.parent_code is '父字典中的字典项编码，用于表示多个选项列表的级联关系';



drop table if exists hymn.core_b_object cascade;
create table hymn.core_b_object
(
    id           text primary key   default replace(public.uuid_generate_v4()::text, '-', ''),
    name         text      not null,
    api          text      not null,
    source_table text,
    active       bool               default true not null,
    type         text      not null default 'custom',
    remote_url   text,
    remote_token text,
    module_api   text,
    remark       text      not null default '',
    can_insert   bool,
    can_update   bool,
    can_delete   bool,
    create_by_id text      not null,
    create_by    text      not null,
    modify_by_id text      not null,
    modify_by    text      not null,
    create_date  timestamp not null,
    modify_date  timestamp not null
);
comment on table hymn.core_b_object is '业务对象';
comment on column hymn.core_b_object.name is '业务对象名称，用于页面显示';
comment on column hymn.core_b_object.api is '业务对象api，用于触发器和自定义接口';
comment on column hymn.core_b_object.active is '是否启用，停用后无法进行增删改查等操作';
comment on column hymn.core_b_object.source_table is '实际表名，例： core_data_table_500';
comment on column hymn.core_b_object.module_api is '模块api名称，所有自定义对象该字段都为null，不为null表示该对象属于指定模块，通过添加模块对象的 core_b_object 和 core_b_object_field 数据来支持在触发器中使用DataService提供的通用操作';
comment on column hymn.core_b_object.can_insert is '模块对象及远程对象是否可以新增数据';
comment on column hymn.core_b_object.can_update is '模块对象是及远程对象否可以更新数据';
comment on column hymn.core_b_object.can_update is '模块对象是及远程对象否可以删除数据';
comment on column hymn.core_b_object.type is '对象类型, 可选值： custom (自定义对象), module (模块对象), remote (外部对象)，说明：模块对象不能在系统后台进行新增删除，底层表单和相关数据需要手动创建，外部对象没有底层表，通过url调用外部接口，只能在应用层脚本中使用';
comment on column hymn.core_b_object.module_api is '模块api名称，通过添加模块对象的 core_b_object 和 core_b_object_field 数据来支持在触发器中使用DataService提供的通用操作';
comment on column hymn.core_b_object.remote_url is '远程rest接口地址，系统通过该地址调用远程数据';
comment on column hymn.core_b_object.remote_token is '远程rest验证信息';



drop table if exists hymn.core_b_object_field cascade;
create table hymn.core_b_object_field
(
    id                text primary key   default replace(public.uuid_generate_v4()::text, '-', ''),
    source_column     text      not null,
    object_id         text      not null,
    name              text      not null,
    api               text      not null,
    type              text      not null,
    active            bool               default true,
    history           bool               default false,
    default_value     text,
    formula           text,
    max_length        integer,
    min_length        integer,
    visible_row       integer,
    dict_id           text,
    master_field_id   text,
    optional_number   integer,
    ref_id            text,
    ref_list_label    text,
    ref_delete_policy text,
    query_filter      text,
    s_id              text,
    s_field_id        text,
    s_type            text,
    gen_rule          text,
    remark            text,
    help              text,
    tmp               text,
    standard_type     text,
    is_predefined     bool      not null default false,
    create_by_id      text      not null,
    create_by         text      not null,
    modify_by_id      text      not null,
    modify_by         text      not null,
    create_date       timestamp not null,
    modify_date       timestamp not null
);
comment on table hymn.core_b_object_field is '业务对象字段

字段类型：文本(text),复选框(check_box),复选框组(check_box_group),下拉菜单(select),整型(integer),
浮点型(float),货币(money),日期(date),日期时间(datetime),主详(master_slave),关联关系(reference),
汇总(summary),自动编号(auto),图片(picture);
说明: 多选关联类型通过中间表关联，占用的字段保持为空
公共可选字段：remark （备注，只显示在管理员界面），help （帮助文本，显示在对象详情界面）
通用字段：default_value （默认值，后端处理，字段间不能联动），formula （前端处理）

type: 文本 text
required: min_length （最小长度）, max_length （最大长度）, visible_row （显示行数）
optional: default_value, formula
rule: min_length >= 0, max_length <= 50000 , visible_row > 0, min_length <= max_length, (if api = ''name'' than max_length <=255)

type: 复选框 check_box
required:
optional: default_value

type: 复选框组 check_box_group
required: optional_number （可选个数）, dict_id （引用字典id）
optional: default_value, formula
rule: optional_number > 0, (dict_id is not null) or (tmp is not null)

type: 下拉菜单 select
required: optional_number （可选个数）, dict_id （引用字典id）,visible_row (显示行数）
optional: default_value, formula, master_field_id （依赖字段id，必须是当前对象的字段，且类型为check_box/select/multiple_select）
rule: optional_number > 0, (dict_id is not null) or (tmp is not null)

type: 整型 integer
required: min_length （最小值）, max_length （最大值）
optional: default_value, formula
rule: min_length <= max_length

type: 浮点 float
required: min_length （小数位长度）, max_length （整数位长度）
optional: default_value, formula
rule: min_length >= 0, max_length >= 1, (min_length + max_length) <= 18

type: 货币 money
required: min_length （小数位长度）, max_length （整数位长度）
optional: default_value, formula
rule: min_length >= 0, max_length >= 1

type: 日期 date
required:
optional: default_value, formula

type: 日期时间 datetime
required:
optional: default_value, formula

type: 主详 master_slave
required: ref_id （引用对象id）, ref_list_label （引用对象相关列表显示的标签）
optional: default_value, formula, query_filter
rule:

type: 关联 reference
required: ref_id （引用对象id）, ref_delete_policy （引用对象数据被删除时是否阻止）
optional: default_value, formula, query_filter, ref_list_label （引用对象相关列表显示的标签）

type: 多选关联 mreference
required: ref_id （引用对象id）, ref_delete_policy （引用对象数据被删除时是否阻止）
optional: default_value, formula, query_filter, ref_list_label （引用对象相关列表显示的标签）

type: 汇总 summary
required: s_id （子对象id）, s_field_id （子对象汇总字段id）, s_type （汇总类型）, min_length （小数位长度）
optional: query_filter
rule: min_length >=0, min_length <= 16, s_type in (''count'',''max'',''min'',''sum'')

type: 自动编号 auto
required: gen_rule （编号规则）
optional:
rule: auto_gen_rule SIMILAR TO ''%\{0+\}%''

type: 图片 picture
required: min_length （图片数量）, max_length （图片大小，单位：kb）
optional:
rule: min_length >= 1, max_length > 0
';
comment on column hymn.core_b_object_field.source_column is '字段对应的实际表中的列名,对象为远程对象时该字段填充空字符串';
comment on column hymn.core_b_object_field.object_id is '所属业务对象id';
comment on column hymn.core_b_object_field.api is 'api名称，用于触发器和自定义接口';
comment on column hymn.core_b_object_field.name is '名称，用于页面显示';
comment on column hymn.core_b_object_field.type is '字段类型';
comment on column hymn.core_b_object_field.history is '是否启用历史记录';
comment on column hymn.core_b_object_field.active is '字段启用状态，false表示停用，字段停用时从视图中移除，删除时清空没一行中对应字段数据';
comment on column hymn.core_b_object_field.default_value is '默认值，可选择其他表中的字段，由后端处理，新建时与页面布局一起返回给前端';
comment on column hymn.core_b_object_field.formula is '公式，js代码，由前端处理，新建和编辑时拼接成监听函数与页面布局一起返回给前端';
comment on column hymn.core_b_object_field.max_length is '文本类型最大长度/浮点型整数位长度/整型最大值/图片最大数量';
comment on column hymn.core_b_object_field.min_length is '文本类型最小长度/浮点型小数位长度/整型最小值/图片最小数量';
comment on column hymn.core_b_object_field.visible_row is '文本类型显示行数/下拉选项 （多选）前端显示行数';
comment on column hymn.core_b_object_field.dict_id is '单选/多选/下拉对应的数据字典id';
comment on column hymn.core_b_object_field.master_field_id is '下拉字段依赖的主字段id';
comment on column hymn.core_b_object_field.optional_number is '副选框和下拉多选的可选个数';
comment on column hymn.core_b_object_field.ref_id is '关联的自定义对象id';
comment on column hymn.core_b_object_field.ref_list_label is '相关列表标签，当前对象在被关联对象的相关列表中显示的标签，为空时表示不能显示在被关联对象的相关列表中';
comment on column hymn.core_b_object_field.ref_delete_policy is '当字段为关联字段时，引用数据被删除时的动作。 cascade 级联删除当前对象数据, restrict 阻止删除被引用对象, null 无动作';
comment on column hymn.core_b_object_field.gen_rule is '编号规则，{000} 递增序列，必填，实际序号大小小于0的个数时将会在前面补0 ; {yyyy}/{yy} 年; {mm} 月; {dd} 日';
comment on column hymn.core_b_object_field.s_id is '汇总对象id';
comment on column hymn.core_b_object_field.s_field_id is '汇总字段id';
comment on column hymn.core_b_object_field.s_type is '汇总类型，可选值：sum/count/min/max';
comment on column hymn.core_b_object_field.query_filter is '字段为汇总字段时表示对子表的过滤条件，字段为引用/主从字段时表示在创建当前对象时查找引用对象的过滤条件，sql where表达式';
comment on column hymn.core_b_object_field.help is '说明，显示在页面上的帮助信息';
comment on column hymn.core_b_object_field.remark is '备注';
comment on column hymn.core_b_object_field.tmp is '辅助列，新建与字典相关的字段时存储字典项数据';
comment on column hymn.core_b_object_field.standard_type is '标准类型，可选值：create_by_id 创建人id, create_by 创建人, modify_by_id 修改人id, modify_by 修改人, create_date 创建时间, modify_date 修改时间, org_id 组织id, lock_state 锁定状态, name 名称, type 业务类型, owner_id 所有人 自定义字段不能设置该值，用于处理模块对象和标准对象的特殊字段的类型';
comment on column hymn.core_b_object_field.is_predefined is '是否是预定义字段，区分对象中的自定义字段与预定义字段，预定义字段该值为true且source_column与api相等，后台对象管理界面中不能删除和修改';


drop table if exists hymn.core_b_object_layout cascade;
create table hymn.core_b_object_layout
(
    id                      text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    object_id               text      not null,
    name                    text      not null,
    remark                  text,
    rel_field_json_arr      text      not null,
    pc_read_layout_json     text      not null,
    pc_edit_layout_json     text      not null,
    mobile_read_layout_json text      not null,
    mobile_edit_layout_json text      not null,
    preview_layout_json     text      not null,
    create_by_id            text      not null,
    create_by               text      not null,
    modify_by_id            text      not null,
    modify_by               text      not null,
    create_date             timestamp not null,
    modify_date             timestamp not null
);
comment on table hymn.core_b_object_layout is '业务对象详情页面布局';
comment on column hymn.core_b_object_layout.name is '布局名称';
comment on column hymn.core_b_object_layout.rel_field_json_arr is '引用字段的数据的列表，用于根据权限对字段进行过滤，布局json中不能直接使用字段数据，在需要字段数据的部分通过rel_field_json_arr中的json对象的_id引用，找不到的场合下忽略该字段';
comment on column hymn.core_b_object_layout.pc_read_layout_json is 'pc端查看页面页面布局';
comment on column hymn.core_b_object_layout.pc_edit_layout_json is 'pc端新建、编辑页面页面布局';
comment on column hymn.core_b_object_layout.mobile_read_layout_json is '移动端查看页面页面布局';
comment on column hymn.core_b_object_layout.mobile_edit_layout_json is '移动端新建、编辑页面页面布局';
comment on column hymn.core_b_object_layout.preview_layout_json is '小窗预览界面布局';



drop table if exists hymn.core_b_object_type cascade;
create table hymn.core_b_object_type
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    object_id    text                           not null,
    name         text                           not null,
    active       bool             default false not null,
    remark       text                           not null,
    create_by_id text                           not null,
    create_by    text                           not null,
    modify_by_id text                           not null,
    modify_by    text                           not null,
    create_date  timestamp                      not null,
    modify_date  timestamp                      not null
);
comment on table hymn.core_b_object_type is '业务对象记录类型';
comment on column hymn.core_b_object_type.object_id is '所属业务对象id';
comment on column hymn.core_b_object_type.name is '记录类型名称';
comment on column hymn.core_b_object_type.active is '是否启用';

drop table if exists hymn.core_b_object_type_available_options cascade;
create table hymn.core_b_object_type_available_options
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    type_id      text      not null,
    field_id     text      not null,
    dict_item_id text      not null,
    create_by_id text      not null,
    create_by    text      not null,
    modify_by_id text      not null,
    modify_by    text      not null,
    create_date  timestamp not null,
    modify_date  timestamp not null
);
comment on table hymn.core_b_object_type_available_options is '业务对象记录类型限制
限制指定记录类型时指定字段 （多选/单选）的可用选项';
comment on column hymn.core_b_object_type_available_options.type_id is '记录类型id';
comment on column hymn.core_b_object_type_available_options.dict_item_id is '字段关联的字典项id';
comment on column hymn.core_b_object_type_available_options.field_id is '字段id';


drop table if exists hymn.core_b_object_type_layout cascade;
create table hymn.core_b_object_type_layout
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    role_id      text      not null,
    object_id    text      not null,
    type_id      text      not null,
    layout_id    text      not null,
    create_by_id text      not null,
    create_by    text      not null,
    modify_by_id text      not null,
    modify_by    text      not null,
    create_date  timestamp not null,
    modify_date  timestamp not null
);
comment on table hymn.core_b_object_type_layout is '业务对象页面布局和记录类型映射表和角色';
comment on column hymn.core_b_object_type_layout.object_id is '业务对象id';
comment on column hymn.core_b_object_type_layout.type_id is '记录类型id';
comment on column hymn.core_b_object_type_layout.layout_id is '页面布局id';



drop table if exists hymn.core_b_object_trigger cascade;
create table hymn.core_b_object_trigger
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    active       bool      not null,
    remark       text,
    object_id    text      not null,
    name         text      not null,
    api          text      not null,
    lang         text      not null,
    option_text  text,
    ord          integer   not null,
    event        text      not null,
    code         text      not null,
    create_by_id text      not null,
    create_by    text      not null,
    modify_by_id text      not null,
    modify_by    text      not null,
    create_date  timestamp not null,
    modify_date  timestamp not null
);
comment on table hymn.core_b_object_trigger is '触发器';
comment on column hymn.core_b_object_trigger.active is '是否启用';
comment on column hymn.core_b_object_trigger.object_id is '所属业务对象id';
comment on column hymn.core_b_object_trigger.name is '触发器名称，用于后台显示';
comment on column hymn.core_b_object_trigger.api is 'api名称，用于报错显示和后台查看';
comment on column hymn.core_b_object_trigger.ord is '优先级';
comment on column hymn.core_b_object_trigger.event is '触发时间 BEFORE_INSERT,BEFORE_UPDATE,BEFORE_UPSERT,BEFORE_DELETE,AFTER_INSERT,AFTER_UPDATE,AFTER_UPSERT,AFTER_DELETE;';
comment on column hymn.core_b_object_trigger.code is '触发器代码';
comment on column hymn.core_b_object_trigger.lang is '语言';
comment on column hymn.core_b_object_trigger.option_text is '用于给编译器或其他组件设置参数(格式参照具体实现）';



drop table if exists hymn.core_b_object_mapping cascade;
create table hymn.core_b_object_mapping
(
    id               text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    source_object_id text      not null,
    target_object_id text      not null,
    source_type_id   text      not null,
    target_type_id   text      not null,
    create_by_id     text      not null,
    create_by        text      not null,
    modify_by_id     text      not null,
    modify_by        text      not null,
    create_date      timestamp not null,
    modify_date      timestamp not null
);
comment on table hymn.core_b_object_mapping is '对象映射关系 描述以一个对象为基础新建对象时字段间的映射关系';
comment on column hymn.core_b_object_mapping.source_object_id is '源对象id';
comment on column hymn.core_b_object_mapping.target_object_id is '目标对象id';
comment on column hymn.core_b_object_mapping.source_type_id is '源对象记录类型id';
comment on column hymn.core_b_object_mapping.target_type_id is '目标对象记录类型id';


drop table if exists hymn.core_b_object_mapping_item cascade;
create table hymn.core_b_object_mapping_item
(
    id                    text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    mapping_id            text      not null,
    source_object_api     text      not null,
    target_object_api     text      not null,
    source_field_api      text      not null,
    target_field_api      text      not null,
    ref_field1_api        text,
    ref_field1_object_api text,
    ref_field2_api        text,
    ref_field2_object_api text,
    ref_field3_api        text,
    ref_field3_object_api text,
    ref_field4_api        text,
    ref_field4_object_api text,
    create_by_id          text      not null,
    create_by             text      not null,
    modify_by_id          text      not null,
    modify_by             text      not null,
    create_date           timestamp not null,
    modify_date           timestamp not null
);
comment on table hymn.core_b_object_mapping_item is '对象映射关系表明细 描述映射规则';
comment on column hymn.core_b_object_mapping_item.source_object_api is '源对象api名称';
comment on column hymn.core_b_object_mapping_item.target_object_api is '目标对象api名称';
comment on column hymn.core_b_object_mapping_item.source_field_api is '源字段api，如果直接从源字段映射到目标字段则 ref_field 和 ref_field_object_api 都为空';
comment on column hymn.core_b_object_mapping_item.target_field_api is '目标字段api';
comment on column hymn.core_b_object_mapping_item.ref_field1_api is '引用字段1';
comment on column hymn.core_b_object_mapping_item.ref_field1_object_api is 'ref_field1_api 表示的字段所属的对象api，也是source_field_api关联的对象的api';
comment on column hymn.core_b_object_mapping_item.ref_field2_api is '引用字段2';
comment on column hymn.core_b_object_mapping_item.ref_field2_object_api is 'ref_field2_api 表示的字段所属的对象api，也是 ref_field1_api 关联的对象的api';
comment on column hymn.core_b_object_mapping_item.ref_field3_api is '引用字段3';
comment on column hymn.core_b_object_mapping_item.ref_field3_object_api is 'ref_field3_api 表示的字段所属的对象api，也是 ref_field2_api 关联的对象的api';
comment on column hymn.core_b_object_mapping_item.ref_field4_api is '引用字段4';
comment on column hymn.core_b_object_mapping_item.ref_field4_object_api is 'ref_field4_api 表示的字段所属的对象api，也是 ref_field3_api 关联的对象的api';



drop table if exists hymn.core_module_function;
create table hymn.core_module_function
(
    id          text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    module_name text not null,
    api         text not null,
    name        text not null,
    remark      text not null,
    create_date text             default now()
);
comment on table hymn.core_module_function is '模块功能表，模块中的功能需要根据角色进行权限控制时在该表中添加相关数据';
comment on column hymn.core_module_function.module_name is '模块名称，权限管理界面中功能管理区域根据模块名分组';
comment on column hymn.core_module_function.api is '功能api名称，格式为模块名+功能名，例：wechat.approval';
comment on column hymn.core_module_function.name is '功能名称';



drop table if exists hymn.core_module_function_perm;
create table hymn.core_module_function_perm
(
    id                 text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    role_id            text      not null,
    module_function_id text      not null,
    perm               bool,
    create_by_id       text      not null,
    create_by          text      not null,
    modify_by_id       text      not null,
    modify_by          text      not null,
    create_date        timestamp not null,
    modify_date        timestamp not null
);
comment on table hymn.core_module_function_perm is '模块功能权限表';
comment on column hymn.core_module_function_perm.role_id is '角色id';
comment on column hymn.core_module_function_perm.module_function_id is '功能id';
comment on column hymn.core_module_function_perm.perm is '是否有访问权限';

drop table if exists hymn.core_button_perm cascade;
create table hymn.core_button_perm
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    role_id      text      not null,
    button_id    text      not null,
    visible      bool      not null,
    create_by_id text      not null,
    create_by    text      not null,
    modify_by_id text      not null,
    modify_by    text      not null,
    create_date  timestamp not null,
    modify_date  timestamp not null
);
comment on table hymn.core_button_perm is '按钮权限';
comment on column hymn.core_button_perm.role_id is '角色id';
comment on column hymn.core_button_perm.button_id is '按钮id';
comment on column hymn.core_button_perm.visible is '是否可见';



drop table if exists hymn.core_menu_item_perm cascade;
create table hymn.core_menu_item_perm
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    role_id      text      not null,
    menu_item_id text      not null,
    visible      bool      not null,
    create_by_id text      not null,
    create_by    text      not null,
    modify_by_id text      not null,
    modify_by    text      not null,
    create_date  timestamp not null,
    modify_date  timestamp not null
);
comment on table hymn.core_menu_item_perm is '菜单项权限';



drop table if exists hymn.core_b_object_perm cascade;
create table hymn.core_b_object_perm
(
    id                      text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    role_id                 text      not null,
    object_id               text      not null,
    object_api_name         text      not null,
    ins                     bool      not null,
    upd                     bool      not null,
    del                     bool      not null,
    que                     bool      not null,
    query_with_account_tree bool      not null,
    query_with_dept         bool      not null,
    query_with_dept_tree    bool      not null,
    query_all               bool      not null,
    edit_all                bool      not null,
    create_by_id            text      not null,
    create_by               text      not null,
    modify_by_id            text      not null,
    modify_by               text      not null,
    create_date             timestamp not null,
    modify_date             timestamp not null
);
comment on table hymn.core_b_object_perm is '对象权限';
comment on column hymn.core_b_object_perm.ins is '创建';
comment on column hymn.core_b_object_perm.upd is '更新';
comment on column hymn.core_b_object_perm.del is '删除';
comment on column hymn.core_b_object_perm.que is '查看';
comment on column hymn.core_b_object_perm.query_with_account_tree is '查看本人及直接下属';
comment on column hymn.core_b_object_perm.query_with_dept is '查看本部门';
comment on column hymn.core_b_object_perm.query_with_dept_tree is '查看本部门及下级部门';
comment on column hymn.core_b_object_perm.query_all is '查看全部';
comment on column hymn.core_b_object_perm.edit_all is '编辑全部';



drop table if exists hymn.core_b_object_field_perm cascade;
create table hymn.core_b_object_field_perm
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    role_id      text      not null,
    object_id    text      not null,
    field_id     text      not null,
    p_read       bool      not null,
    p_edit       bool      not null,
    create_by_id text      not null,
    create_by    text      not null,
    modify_by_id text      not null,
    modify_by    text      not null,
    create_date  timestamp not null,
    modify_date  timestamp not null
);
comment on table hymn.core_b_object_field_perm is '字段权限';
comment on column hymn.core_b_object_field_perm.p_read is '可读';
comment on column hymn.core_b_object_field_perm.p_edit is '可编辑';



drop table if exists hymn.core_b_object_type_perm cascade;
create table hymn.core_b_object_type_perm
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    role_id      text      not null,
    object_id    text      not null,
    type_id      text      not null,
    visible      bool      not null,
    create_by_id text      not null,
    create_by    text      not null,
    modify_by_id text      not null,
    modify_by    text      not null,
    create_date  timestamp not null,
    modify_date  timestamp not null
);
comment on table hymn.core_b_object_type_perm is '记录类型权限';
comment on column hymn.core_b_object_type_perm.visible is '创建数据时选择特定记录类型的权限';


-- 数据权限表
drop table if exists hymn.core_data_share cascade;
create table hymn.core_data_share
(
    object_api_name text,
    data_id         text,
    role_id         text,
    org_id          text,
    account_id      text,
    read_only       bool
);
comment on table hymn.core_data_share is '对象权限共享';
comment on column hymn.core_data_share.data_id is '要共享的数据id';
comment on column hymn.core_data_share.object_api_name is '共享数据所属对象api名称';
comment on column hymn.core_data_share.account_id is '共享数据的目标用户id';
comment on column hymn.core_data_share.role_id is '共享数据的目标角色id';
comment on column hymn.core_data_share.org_id is '共享数据目标组织id';


drop table if exists hymn.core_table_obj_mapping cascade;
create table hymn.core_table_obj_mapping
(
    table_name text primary key,
    obj_api    text default null
);
comment on table hymn.core_table_obj_mapping is '数据表与对象映射表，默认业务对象最高500条，表名从 core_data_table_001 到 core_data_table_500, 表示模块对象时 table_name 与 obj_api 相同';
comment on column hymn.core_table_obj_mapping.table_name is '表名称';
comment on column hymn.core_table_obj_mapping.obj_api is '业务对象api名称';


-- 业务对象字段库
drop table if exists hymn.core_column_field_mapping cascade;
create table hymn.core_column_field_mapping
(
    table_name  text not null,
    column_name text not null,
    field_api   text,
    primary key (table_name, column_name)
);
comment on table hymn.core_column_field_mapping is '业务对象字段库';
comment on column hymn.core_column_field_mapping.table_name is '实际的数据表的名称';
comment on column hymn.core_column_field_mapping.column_name is '表中的字段名称';
comment on column hymn.core_column_field_mapping.field_api is '视图中的字段名称';


drop table if exists hymn.core_shared_code;
create table hymn.core_shared_code
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    api          text      not null,
    type         text      not null,
    code         text      not null,
    lang         text      not null,
    option_text  text,
    create_by_id text      not null,
    create_by    text      not null,
    modify_by_id text      not null,
    modify_by    text      not null,
    create_date  timestamp not null,
    modify_date  timestamp not null
);
comment on table hymn.core_shared_code is '共享代码 可以在接口、触发器中调用或使用在定时任务中';
comment on column hymn.core_shared_code.type is '代码类型 可选值 函数代码 function， 任务代码 job';
comment on column hymn.core_shared_code.code is '代码';
comment on column hymn.core_shared_code.api is 'api名称,也是代码中的函数名称';
comment on column hymn.core_shared_code.lang is '语言';
comment on column hymn.core_shared_code.option_text is '用于给编译器或其他组件设置参数(格式参照具体实现）';


drop table if exists hymn.core_business_code_ref;
create table hymn.core_business_code_ref
(
    id             text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    trigger_id     text,
    interface_id   text,
    shared_code_id text,
    object_id      text,
    field_id       text,
    org_id         text,
    role_id        text,
    create_by_id   text      not null,
    create_by      text      not null,
    modify_by_id   text      not null,
    modify_by      text      not null,
    create_date    timestamp not null,
    modify_date    timestamp not null
);
comment on table hymn.core_business_code_ref is '业务代码引用关系表';
comment on column hymn.core_business_code_ref.trigger_id is '触发器id';
comment on column hymn.core_business_code_ref.interface_id is '接口id';
comment on column hymn.core_business_code_ref.shared_code_id is '共享代码id';
comment on column hymn.core_business_code_ref.object_id is '被引用对象id';
comment on column hymn.core_business_code_ref.field_id is '被引用字段id';
comment on column hymn.core_business_code_ref.org_id is '被引用组织id';
comment on column hymn.core_business_code_ref.role_id is '被引用角色id';

drop table if exists hymn.core_cron_job;
create table hymn.core_cron_job
(
    id              text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    active          bool      not null,
    shared_code_id  text      not null,
    cron            text      not null,
    start_date_time timestamp not null,
    end_date_time   timestamp not null,
    create_by_id    text      not null,
    create_by       text      not null,
    modify_by_id    text      not null,
    modify_by       text      not null,
    create_date     timestamp not null,
    modify_date     timestamp not null
);
comment on table hymn.core_cron_job is '定时任务';
comment on column hymn.core_cron_job.start_date_time is '任务开始时间';
comment on column hymn.core_cron_job.end_date_time is '任务结束时间';
comment on column hymn.core_cron_job.active is '是否启用';
comment on column hymn.core_cron_job.cron is '定时规则';
comment on column hymn.core_cron_job.shared_code_id is '任务代码id';


drop table if exists hymn.sql_keyword;
create table hymn.sql_keyword
(
    keyword text primary key
);
