insert into hymn.sys_core_account (id, name, username, password, online_rule, active, admin, root,
                                   leader_id, org_id, role_id, create_by_id, create_by,
                                   modify_by_id, modify_by, create_date, modify_date)
values ('911c60ea5d62420794d86eeecfddce7c', 'system admin', 'system admin', '', '', true, true,
        true, null, 'b18245e9d690461190172b6cb90c46ac', '301c35c23be449abb5bdf6c80b6878af',
        '911c60ea5d62420794d86eeecfddce7c', 'system admin', '911c60ea5d62420794d86eeecfddce7c',
        'system admin', '2020-01-01 00:00:00', '2020-01-01 00:00:00');
insert into hymn.sys_core_org (id, name, director_id, deputy_director_id, parent_id,
                               create_by_id, create_by, modify_by_id, modify_by, create_date,
                               modify_date)
values ('b18245e9d690461190172b6cb90c46ac', '根组织', null, null, null,
        '911c60ea5d62420794d86eeecfddce7c', 'system admin', '911c60ea5d62420794d86eeecfddce7c',
        'system admin', '2020-01-01 00:00:00', '2020-01-01 00:00:00');
insert into hymn.sys_core_role (id, name, remark, create_by_id, create_by, modify_by_id,
                                modify_by, create_date, modify_date)
values ('301c35c23be449abb5bdf6c80b6878af', '管理员权限', '', '911c60ea5d62420794d86eeecfddce7c',
        'system admin', '911c60ea5d62420794d86eeecfddce7c', 'system admin', '2020-01-01 00:00:00',
        '2020-01-01 00:00:00');

insert into hymn.sys_core_b_object (id, name, api, source_table, active, module_api, can_insert,
                                    can_update, remark,
                                    create_by_id, create_by, modify_by_id, modify_by, create_date,
                                    modify_date)
values ('bcf5f00c2e6c494ea2318912a639031a', '用户', 'account', 'sys_core_account', true, 'core',
        false, true, '用户表',
        '911c60ea5d62420794d86eeecfddce7c', 'system admin', '911c60ea5d62420794d86eeecfddce7c',
        'system admin', '2020-01-01 00:00:00', '2020-01-01 00:00:00'),
       ('53e9c36723dc4e3db1faf396fdb3f1d2', '角色', 'role', 'sys_core_role', true, 'core',
        false, false, '角色表',
        '911c60ea5d62420794d86eeecfddce7c', 'system admin', '911c60ea5d62420794d86eeecfddce7c',
        'system admin', '2020-01-01 00:00:00', '2020-01-01 00:00:00'),
       ('c9c6cc327c614c96b8d6f5af1fee6442', '组织', 'org', 'sys_core_org', true, 'core',
        false, false, '组织表',
        '911c60ea5d62420794d86eeecfddce7c', 'system admin', '911c60ea5d62420794d86eeecfddce7c',
        'system admin', '2020-01-01 00:00:00', '2020-01-01 00:00:00'),
       ('09da56a7de514895aea5c596820d0ced', '业务类型', 'business_type', 'sys_core_b_object_type',
        true, 'core', false, false, '自定义对象业务类型表',
        '911c60ea5d62420794d86eeecfddce7c', 'system admin', '911c60ea5d62420794d86eeecfddce7c',
        'system admin', '2020-01-01 00:00:00', '2020-01-01 00:00:00')
;

insert into hymn.sys_core_dict (id, field_id, parent_dict_id, name, api, remark, create_by_id,
                                create_by, modify_by_id, modify_by, create_date, modify_date)
values ('4a6010cceea948d6856aac09e8aa19c0', null, null, '锁定状态', 'lock_state_dict', '系统标准字段锁定状态',
        '911c60ea5d62420794d86eeecfddce7c', 'system admin', '911c60ea5d62420794d86eeecfddce7c',
        'system admin', '2020-01-01 00:00:00', '2020-01-01 00:00:00');

insert into hymn.sys_core_dict_item (id, dict_id, name, code, parent_code, create_by_id, create_by,
                                     modify_by_id, modify_by, create_date, modify_date)
values ('c31ec080066a432e9d1399b912c417c7', '4a6010cceea948d6856aac09e8aa19c0', '未锁定', '0', null,
        '911c60ea5d62420794d86eeecfddce7c', 'system admin', '911c60ea5d62420794d86eeecfddce7c',
        'system admin', '2020-01-01 00:00:00', '2020-01-01 00:00:00'),
       ('273d19578032400aba689fb0fa40f912', '4a6010cceea948d6856aac09e8aa19c0', '已锁定', '1', null,
        '911c60ea5d62420794d86eeecfddce7c', 'system admin', '911c60ea5d62420794d86eeecfddce7c',
        'system admin', '2020-01-01 00:00:00', '2020-01-01 00:00:00');

insert into hymn.sys_core_b_object_field (id, source_column, object_id, name, api, type, active,
                                          history, default_value, formula, max_length, min_length,
                                          visible_row, dict_id, master_field_id, optional_number,
                                          ref_id, ref_list_label, ref_delete_policy, query_filter,
                                          s_id, s_field_id, s_type, gen_rule, remark, help, tmp,
                                          standard_type, is_predefined, create_by_id, create_by,
                                          modify_by_id, modify_by, create_date, modify_date)
values ('1bad12b79d114b18a9f1b3276992886e', 'name', 'bcf5f00c2e6c494ea2318912a639031a', '姓名',
        'name', 'text', true, false, null, null, 255, 1, 1, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, 'name', true,
        '911c60ea5d62420794d86eeecfddce7c', 'system admin', '911c60ea5d62420794d86eeecfddce7c',
        'system admin', '2020-01-01 00:00:00', '2020-01-01 00:00:00'),
       ('c74acaab671e4045968f0249de2dde56', 'username', 'bcf5f00c2e6c494ea2318912a639031a', '用户名',
        'username', 'text', true, false, null, null, 50, 1, 1, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, false,
        '911c60ea5d62420794d86eeecfddce7c', 'system admin', '911c60ea5d62420794d86eeecfddce7c',
        'system admin', '2020-01-01 00:00:00', '2020-01-01 00:00:00'),
       ('9348a88e57164388b0d623da8e626191', 'active', 'bcf5f00c2e6c494ea2318912a639031a', '启用',
        'active', 'check_box', true, false, null, null, null, null, null, null, null, null, null,
        null, null, null,
        null, null, null, null, null, null, null, null, true,
        '911c60ea5d62420794d86eeecfddce7c', 'system admin', '911c60ea5d62420794d86eeecfddce7c',
        'system admin', '2020-01-01 00:00:00', '2020-01-01 00:00:00'),
       ('7ecb633fd0ce4d1587900de1edf98e6f', 'leader_id', 'bcf5f00c2e6c494ea2318912a639031a', '直属上级',
        'leader_id', 'reference', true, false, null, null, null, null, null, null, null, null,
        'bcf5f00c2e6c494ea2318912a639031a',
        '直属下级', 'null', null, null, null, null, null, null, null, null, null,
        true,
        '911c60ea5d62420794d86eeecfddce7c', 'system admin', '911c60ea5d62420794d86eeecfddce7c',
        'system admin', '2020-01-01 00:00:00', '2020-01-01 00:00:00'),
       ('48cc2b38a0e6468596099a441e207c8e', 'org_id', 'bcf5f00c2e6c494ea2318912a639031a', '组织id',
        'org_id', 'reference', true, false, null, null, null, null, null, null, null, null,
        'c9c6cc327c614c96b8d6f5af1fee6442', '部门人员', 'restrict', null, null, null, null, null, null,
        null, null, null, true,
        '911c60ea5d62420794d86eeecfddce7c', 'system admin', '911c60ea5d62420794d86eeecfddce7c',
        'system admin', '2020-01-01 00:00:00', '2020-01-01 00:00:00'),
       ('eee7e25a37f74d09a441f9829d4a4c69', 'role_id', 'bcf5f00c2e6c494ea2318912a639031a', '角色id',
        'role_id', 'reference', true, false, null, null, null, null, null, null, null, null,
        '53e9c36723dc4e3db1faf396fdb3f1d2', '角色帐号', 'restrict', null, null, null, null, null, null,
        null, null, null, true,
        '911c60ea5d62420794d86eeecfddce7c', 'system admin', '911c60ea5d62420794d86eeecfddce7c',
        'system admin', '2020-01-01 00:00:00', '2020-01-01 00:00:00'),
       ('eb9feabb17254db5a3ad69384a68a2e6', 'create_by_id', 'bcf5f00c2e6c494ea2318912a639031a',
        '创建人id',
        'create_by_id', 'reference', true, false, null, null, null, null, null, null, null, null,
        'bcf5f00c2e6c494ea2318912a639031a',
        '创建用户', 'null', null, null, null, null, null, null, null, null, 'create_by_id',
        true,
        '911c60ea5d62420794d86eeecfddce7c', 'system admin', '911c60ea5d62420794d86eeecfddce7c',
        'system admin', '2020-01-01 00:00:00', '2020-01-01 00:00:00'),
       ('ddfdc095f9fe466790058c5534d334bd', 'modify_by_id', 'bcf5f00c2e6c494ea2318912a639031a',
        '修改人id',
        'modify_by_id', 'reference', true, false, null, null, null, null, null, null, null, null,
        'bcf5f00c2e6c494ea2318912a639031a',
        '修改用户', 'null', null, null, null, null, null, null, null, null, 'modify_by_id',
        true,
        '911c60ea5d62420794d86eeecfddce7c', 'system admin', '911c60ea5d62420794d86eeecfddce7c',
        'system admin', '2020-01-01 00:00:00', '2020-01-01 00:00:00'),
       ('73de03ebaaa14b4794133b1368f0f03c', 'create_date', 'bcf5f00c2e6c494ea2318912a639031a',
        '创建日期', 'create_date', 'datetime', true, false, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null, 'create_date', true,
        '911c60ea5d62420794d86eeecfddce7c', 'system admin', '911c60ea5d62420794d86eeecfddce7c',
        'system admin', '2020-01-01 00:00:00', '2020-01-01 00:00:00'),
       ('010667b10ca24dd9b8ffd85260b4fc8c', 'modify_date', 'bcf5f00c2e6c494ea2318912a639031a',
        '修改日期', 'modify_date', 'datetime', true, false, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null, 'modify_date', true,
        '911c60ea5d62420794d86eeecfddce7c', 'system admin', '911c60ea5d62420794d86eeecfddce7c',
        'system admin', '2020-01-01 00:00:00', '2020-01-01 00:00:00')
;

insert into hymn.sys_core_table_obj_mapping (table_name, obj_api)
values ('sys_core_account', 'account'),
       ('sys_core_org', 'org'),
       ('sys_core_role', 'role');

insert into hymn.sys_core_column_field_mapping (table_name, column_name, field_api)
values ('sys_core_account', 'text001', null),
       ('sys_core_account', 'text002', null),
       ('sys_core_account', 'text003', null),
       ('sys_core_account', 'text004', null),
       ('sys_core_account', 'text005', null),
       ('sys_core_account', 'text006', null),
       ('sys_core_account', 'text007', null),
       ('sys_core_account', 'text008', null),
       ('sys_core_account', 'text009', null),
       ('sys_core_account', 'text010', null),
       ('sys_core_account', 'bigint001', null),
       ('sys_core_account', 'bigint002', null),
       ('sys_core_account', 'bigint003', null),
       ('sys_core_account', 'bigint004', null),
       ('sys_core_account', 'bigint005', null),
       ('sys_core_account', 'double001', null),
       ('sys_core_account', 'double002', null),
       ('sys_core_account', 'double003', null),
       ('sys_core_account', 'double004', null),
       ('sys_core_account', 'double005', null),
       ('sys_core_account', 'decimal001', null),
       ('sys_core_account', 'decimal002', null),
       ('sys_core_account', 'decimal003', null),
       ('sys_core_account', 'decimal004', null),
       ('sys_core_account', 'decimal005', null),
       ('sys_core_account', 'datetime001', null),
       ('sys_core_account', 'datetime002', null),
       ('sys_core_account', 'datetime003', null),
       ('sys_core_account', 'datetime004', null),
       ('sys_core_account', 'datetime005', null),
       ('sys_core_org', 'text001', null),
       ('sys_core_org', 'text002', null),
       ('sys_core_org', 'text003', null),
       ('sys_core_org', 'text004', null),
       ('sys_core_org', 'text005', null),
       ('sys_core_org', 'bigint001', null),
       ('sys_core_org', 'bigint002', null),
       ('sys_core_org', 'bigint003', null),
       ('sys_core_org', 'bigint004', null),
       ('sys_core_org', 'bigint005', null);

insert into hymn.sql_keyword (keyword)
values ('a'),
       ('abort'),
       ('abs'),
       ('absent'),
       ('absolute'),
       ('access'),
       ('according'),
       ('acos'),
       ('action'),
       ('ada'),
       ('add'),
       ('admin'),
       ('after'),
       ('aggregate'),
       ('all'),
       ('allocate'),
       ('also'),
       ('alter'),
       ('always'),
       ('analyse'),
       ('analyze'),
       ('and'),
       ('any'),
       ('are'),
       ('array'),
       ('array_agg'),
       ('array_max_cardinality'),
       ('as'),
       ('asc'),
       ('asensitive'),
       ('asin'),
       ('assertion'),
       ('assignment'),
       ('asymmetric'),
       ('at'),
       ('atan'),
       ('atomic'),
       ('attach'),
       ('attribute'),
       ('attributes'),
       ('authorization'),
       ('avg'),
       ('backward'),
       ('base64'),
       ('before'),
       ('begin'),
       ('begin_frame'),
       ('begin_partition'),
       ('bernoulli'),
       ('between'),
       ('bigint'),
       ('binary'),
       ('bit'),
       ('bit_length'),
       ('blob'),
       ('blocked'),
       ('bom'),
       ('boolean'),
       ('both'),
       ('breadth'),
       ('by'),
       ('c'),
       ('cache'),
       ('call'),
       ('called'),
       ('cardinality'),
       ('cascade'),
       ('cascaded'),
       ('case'),
       ('cast'),
       ('catalog'),
       ('catalog_name'),
       ('ceil'),
       ('ceiling'),
       ('chain'),
       ('chaining'),
       ('char'),
       ('character'),
       ('characteristics'),
       ('characters'),
       ('character_length'),
       ('character_set_catalog'),
       ('character_set_name'),
       ('character_set_schema'),
       ('char_length'),
       ('check'),
       ('checkpoint'),
       ('class'),
       ('classifier'),
       ('class_origin'),
       ('clob'),
       ('close'),
       ('cluster'),
       ('coalesce'),
       ('cobol'),
       ('collate'),
       ('collation'),
       ('collation_catalog'),
       ('collation_name'),
       ('collation_schema'),
       ('collect'),
       ('column'),
       ('columns'),
       ('column_name'),
       ('command_function'),
       ('command_function_code'),
       ('comment'),
       ('comments'),
       ('commit'),
       ('committed'),
       ('concurrently'),
       ('condition'),
       ('conditional'),
       ('condition_number'),
       ('configuration'),
       ('conflict'),
       ('connect'),
       ('connection'),
       ('connection_name'),
       ('constraint'),
       ('constraints'),
       ('constraint_catalog'),
       ('constraint_name'),
       ('constraint_schema'),
       ('constructor'),
       ('contains'),
       ('content'),
       ('continue'),
       ('control'),
       ('conversion'),
       ('convert'),
       ('copy'),
       ('corr'),
       ('corresponding'),
       ('cos'),
       ('cosh'),
       ('cost'),
       ('count'),
       ('covar_pop'),
       ('covar_samp'),
       ('create'),
       ('cross'),
       ('csv'),
       ('cube'),
       ('cume_dist'),
       ('current'),
       ('current_catalog'),
       ('current_date'),
       ('current_default_transform_group'),
       ('current_path'),
       ('current_role'),
       ('current_row'),
       ('current_schema'),
       ('current_time'),
       ('current_timestamp'),
       ('current_transform_group_for_type'),
       ('current_user'),
       ('cursor'),
       ('cursor_name'),
       ('cycle'),
       ('data'),
       ('database'),
       ('datalink'),
       ('date'),
       ('datetime_interval_code'),
       ('datetime_interval_precision'),
       ('day'),
       ('db'),
       ('deallocate'),
       ('dec'),
       ('decfloat'),
       ('decimal'),
       ('declare'),
       ('default'),
       ('defaults'),
       ('deferrable'),
       ('deferred'),
       ('define'),
       ('defined'),
       ('definer'),
       ('degree'),
       ('delete'),
       ('delimiter'),
       ('delimiters'),
       ('dense_rank'),
       ('depends'),
       ('depth'),
       ('deref'),
       ('derived'),
       ('desc'),
       ('describe'),
       ('descriptor'),
       ('detach'),
       ('deterministic'),
       ('diagnostics'),
       ('dictionary'),
       ('disable'),
       ('discard'),
       ('disconnect'),
       ('dispatch'),
       ('distinct'),
       ('dlnewcopy'),
       ('dlpreviouscopy'),
       ('dlurlcomplete'),
       ('dlurlcompleteonly'),
       ('dlurlcompletewrite'),
       ('dlurlpath'),
       ('dlurlpathonly'),
       ('dlurlpathwrite'),
       ('dlurlscheme'),
       ('dlurlserver'),
       ('dlvalue'),
       ('do'),
       ('document'),
       ('domain'),
       ('double'),
       ('drop'),
       ('dynamic'),
       ('dynamic_function'),
       ('dynamic_function_code'),
       ('each'),
       ('element'),
       ('else'),
       ('empty'),
       ('enable'),
       ('encoding'),
       ('encrypted'),
       ('end'),
       ('end-exec'),
       ('end_frame'),
       ('end_partition'),
       ('enforced'),
       ('enum'),
       ('equals'),
       ('error'),
       ('escape'),
       ('event'),
       ('every'),
       ('except'),
       ('exception'),
       ('exclude'),
       ('excluding'),
       ('exclusive'),
       ('exec'),
       ('execute'),
       ('exists'),
       ('exp'),
       ('explain'),
       ('expression'),
       ('extension'),
       ('external'),
       ('extract'),
       ('false'),
       ('family'),
       ('fetch'),
       ('file'),
       ('filter'),
       ('final'),
       ('finish'),
       ('first'),
       ('first_value'),
       ('flag'),
       ('float'),
       ('floor'),
       ('following'),
       ('for'),
       ('force'),
       ('foreign'),
       ('format'),
       ('fortran'),
       ('forward'),
       ('found'),
       ('frame_row'),
       ('free'),
       ('freeze'),
       ('from'),
       ('fs'),
       ('fulfill'),
       ('full'),
       ('function'),
       ('functions'),
       ('fusion'),
       ('g'),
       ('general'),
       ('generated'),
       ('get'),
       ('global'),
       ('go'),
       ('goto'),
       ('grant'),
       ('granted'),
       ('greatest'),
       ('group'),
       ('grouping'),
       ('groups'),
       ('handler'),
       ('having'),
       ('header'),
       ('hex'),
       ('hierarchy'),
       ('hold'),
       ('hour'),
       ('id'),
       ('identity'),
       ('if'),
       ('ignore'),
       ('ilike'),
       ('immediate'),
       ('immediately'),
       ('immutable'),
       ('implementation'),
       ('implicit'),
       ('import'),
       ('in'),
       ('include'),
       ('including'),
       ('increment'),
       ('indent'),
       ('index'),
       ('indexes'),
       ('indicator'),
       ('inherit'),
       ('inherits'),
       ('initial'),
       ('initially'),
       ('inline'),
       ('inner'),
       ('inout'),
       ('input'),
       ('insensitive'),
       ('insert'),
       ('instance'),
       ('instantiable'),
       ('instead'),
       ('int'),
       ('integer'),
       ('integrity'),
       ('intersect'),
       ('intersection'),
       ('interval'),
       ('into'),
       ('invoker'),
       ('is'),
       ('isnull'),
       ('isolation'),
       ('join'),
       ('json'),
       ('json_array'),
       ('json_arrayagg'),
       ('json_exists'),
       ('json_object'),
       ('json_objectagg'),
       ('json_query'),
       ('json_table'),
       ('json_table_primitive'),
       ('json_value'),
       ('k'),
       ('keep'),
       ('key'),
       ('keys'),
       ('key_member'),
       ('key_type'),
       ('label'),
       ('lag'),
       ('language'),
       ('large'),
       ('last'),
       ('last_value'),
       ('lateral'),
       ('lead'),
       ('leading'),
       ('leakproof'),
       ('least'),
       ('left'),
       ('length'),
       ('level'),
       ('library'),
       ('like'),
       ('like_regex'),
       ('limit'),
       ('link'),
       ('listagg'),
       ('listen'),
       ('ln'),
       ('load'),
       ('local'),
       ('localtime'),
       ('localtimestamp'),
       ('location'),
       ('locator'),
       ('lock'),
       ('locked'),
       ('log'),
       ('log10'),
       ('logged'),
       ('lower'),
       ('m'),
       ('map'),
       ('mapping'),
       ('match'),
       ('matched'),
       ('matches'),
       ('match_number'),
       ('match_recognize'),
       ('materialized'),
       ('max'),
       ('maxvalue'),
       ('measures'),
       ('member'),
       ('merge'),
       ('message_length'),
       ('message_octet_length'),
       ('message_text'),
       ('method'),
       ('min'),
       ('minute'),
       ('minvalue'),
       ('mod'),
       ('mode'),
       ('modifies'),
       ('module'),
       ('month'),
       ('more'),
       ('move'),
       ('multiset'),
       ('mumps'),
       ('name'),
       ('names'),
       ('namespace'),
       ('national'),
       ('natural'),
       ('nchar'),
       ('nclob'),
       ('nested'),
       ('nesting'),
       ('new'),
       ('next'),
       ('nfc'),
       ('nfd'),
       ('nfkc'),
       ('nfkd'),
       ('nil'),
       ('no'),
       ('none'),
       ('normalize'),
       ('normalized'),
       ('not'),
       ('nothing'),
       ('notify'),
       ('notnull'),
       ('nowait'),
       ('nth_value'),
       ('ntile'),
       ('null'),
       ('nullable'),
       ('nullif'),
       ('nulls'),
       ('number'),
       ('numeric'),
       ('object'),
       ('occurrences_regex'),
       ('octets'),
       ('octet_length'),
       ('of'),
       ('off'),
       ('offset'),
       ('oids'),
       ('old'),
       ('omit'),
       ('on'),
       ('one'),
       ('only'),
       ('open'),
       ('operator'),
       ('option'),
       ('options'),
       ('or'),
       ('order'),
       ('ordering'),
       ('ordinality'),
       ('others'),
       ('out'),
       ('outer'),
       ('output'),
       ('over'),
       ('overflow'),
       ('overlaps'),
       ('overlay'),
       ('overriding'),
       ('owned'),
       ('owner'),
       ('p'),
       ('pad'),
       ('parallel'),
       ('parameter'),
       ('parameter_mode'),
       ('parameter_name'),
       ('parameter_ordinal_position'),
       ('parameter_specific_catalog'),
       ('parameter_specific_name'),
       ('parameter_specific_schema'),
       ('parser'),
       ('partial'),
       ('partition'),
       ('pascal'),
       ('pass'),
       ('passing'),
       ('passthrough'),
       ('password'),
       ('past'),
       ('path'),
       ('pattern'),
       ('per'),
       ('percent'),
       ('percentile_cont'),
       ('percentile_disc'),
       ('percent_rank'),
       ('period'),
       ('permission'),
       ('permute'),
       ('placing'),
       ('plan'),
       ('plans'),
       ('pli'),
       ('policy'),
       ('portion'),
       ('position'),
       ('position_regex'),
       ('power'),
       ('precedes'),
       ('preceding'),
       ('precision'),
       ('prepare'),
       ('prepared'),
       ('preserve'),
       ('primary'),
       ('prior'),
       ('private'),
       ('privileges'),
       ('procedural'),
       ('procedure'),
       ('procedures'),
       ('program'),
       ('prune'),
       ('ptf'),
       ('public'),
       ('publication'),
       ('quote'),
       ('quotes'),
       ('range'),
       ('rank'),
       ('read'),
       ('reads'),
       ('real'),
       ('reassign'),
       ('recheck'),
       ('recovery'),
       ('recursive'),
       ('ref'),
       ('references'),
       ('referencing'),
       ('refresh'),
       ('regr_avgx'),
       ('regr_avgy'),
       ('regr_count'),
       ('regr_intercept'),
       ('regr_r2'),
       ('regr_slope'),
       ('regr_sxx'),
       ('regr_sxy'),
       ('regr_syy'),
       ('reindex'),
       ('relative'),
       ('release'),
       ('rename'),
       ('repeatable'),
       ('replace'),
       ('replica'),
       ('requiring'),
       ('reset'),
       ('respect'),
       ('restart'),
       ('restore'),
       ('restrict'),
       ('result'),
       ('return'),
       ('returned_cardinality'),
       ('returned_length'),
       ('returned_octet_length'),
       ('returned_sqlstate'),
       ('returning'),
       ('returns'),
       ('revoke'),
       ('right'),
       ('role'),
       ('rollback'),
       ('rollup'),
       ('routine'),
       ('routines'),
       ('routine_catalog'),
       ('routine_name'),
       ('routine_schema'),
       ('row'),
       ('rows'),
       ('row_count'),
       ('row_number'),
       ('rule'),
       ('running'),
       ('savepoint'),
       ('scalar'),
       ('scale'),
       ('schema'),
       ('schemas'),
       ('schema_name'),
       ('scope'),
       ('scope_catalog'),
       ('scope_name'),
       ('scope_schema'),
       ('scroll'),
       ('search'),
       ('second'),
       ('section'),
       ('security'),
       ('seek'),
       ('select'),
       ('selective'),
       ('self'),
       ('sensitive'),
       ('sequence'),
       ('sequences'),
       ('serializable'),
       ('server'),
       ('server_name'),
       ('session'),
       ('session_user'),
       ('set'),
       ('setof'),
       ('sets'),
       ('share'),
       ('show'),
       ('similar'),
       ('simple'),
       ('sin'),
       ('sinh'),
       ('size'),
       ('skip'),
       ('smallint'),
       ('snapshot'),
       ('some'),
       ('source'),
       ('space'),
       ('specific'),
       ('specifictype'),
       ('specific_name'),
       ('sql'),
       ('sqlcode'),
       ('sqlerror'),
       ('sqlexception'),
       ('sqlstate'),
       ('sqlwarning'),
       ('sqrt'),
       ('stable'),
       ('standalone'),
       ('start'),
       ('state'),
       ('statement'),
       ('static'),
       ('statistics'),
       ('stddev_pop'),
       ('stddev_samp'),
       ('stdin'),
       ('stdout'),
       ('storage'),
       ('stored'),
       ('strict'),
       ('string'),
       ('strip'),
       ('structure'),
       ('style'),
       ('subclass_origin'),
       ('submultiset'),
       ('subscription'),
       ('subset'),
       ('substring'),
       ('substring_regex'),
       ('succeeds'),
       ('sum'),
       ('support'),
       ('symmetric'),
       ('sysid'),
       ('system'),
       ('system_time'),
       ('system_user'),
       ('t'),
       ('table'),
       ('tables'),
       ('tablesample'),
       ('tablespace'),
       ('table_name'),
       ('tan'),
       ('tanh'),
       ('temp'),
       ('template'),
       ('temporary'),
       ('text'),
       ('then'),
       ('through'),
       ('ties'),
       ('time'),
       ('timestamp'),
       ('timezone_hour'),
       ('timezone_minute'),
       ('to'),
       ('token'),
       ('top_level_count'),
       ('trailing'),
       ('transaction'),
       ('transactions_committed'),
       ('transactions_rolled_back'),
       ('transaction_active'),
       ('transform'),
       ('transforms'),
       ('translate'),
       ('translate_regex'),
       ('translation'),
       ('treat'),
       ('trigger'),
       ('trigger_catalog'),
       ('trigger_name'),
       ('trigger_schema'),
       ('trim'),
       ('trim_array'),
       ('true'),
       ('truncate'),
       ('trusted'),
       ('type'),
       ('types'),
       ('uescape'),
       ('unbounded'),
       ('uncommitted'),
       ('unconditional'),
       ('under'),
       ('unencrypted'),
       ('union'),
       ('unique'),
       ('unknown'),
       ('unlink'),
       ('unlisten'),
       ('unlogged'),
       ('unmatched'),
       ('unnamed'),
       ('unnest'),
       ('until'),
       ('untyped'),
       ('update'),
       ('upper'),
       ('uri'),
       ('usage'),
       ('user'),
       ('user_defined_type_catalog'),
       ('user_defined_type_code'),
       ('user_defined_type_name'),
       ('user_defined_type_schema'),
       ('using'),
       ('utf16'),
       ('utf32'),
       ('utf8'),
       ('vacuum'),
       ('valid'),
       ('validate'),
       ('validator'),
       ('value'),
       ('values'),
       ('value_of'),
       ('varbinary'),
       ('varchar'),
       ('variadic'),
       ('varying'),
       ('var_pop'),
       ('var_samp'),
       ('verbose'),
       ('version'),
       ('versioning'),
       ('view'),
       ('views'),
       ('volatile'),
       ('when'),
       ('whenever'),
       ('where'),
       ('whitespace'),
       ('width_bucket'),
       ('window'),
       ('with'),
       ('within'),
       ('without'),
       ('work'),
       ('wrapper'),
       ('write'),
       ('xml'),
       ('xmlagg'),
       ('xmlattributes'),
       ('xmlbinary'),
       ('xmlcast'),
       ('xmlcomment'),
       ('xmlconcat'),
       ('xmldeclaration'),
       ('xmldocument'),
       ('xmlelement'),
       ('xmlexists'),
       ('xmlforest'),
       ('xmliterate'),
       ('xmlnamespaces'),
       ('xmlparse'),
       ('xmlpi'),
       ('xmlquery'),
       ('xmlroot'),
       ('xmlschema'),
       ('xmlserialize'),
       ('xmltable'),
       ('xmltext'),
       ('xmlvalidate'),
       ('year'),
       ('yes'),
       ('zone');