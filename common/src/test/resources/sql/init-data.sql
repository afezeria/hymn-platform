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