drop table if exists hymn.oss_file_record cascade;
create table hymn.oss_file_record
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    bucket       text        not null,
    file_name    text        not null,
    content_type text,
    path         text        not null,
    object_id    text,
    field_id     text,
    data_id      text,
    size         int,
    remark       text,
    create_by_id text        not null,
    create_by    text        not null,
    modify_by_id text        not null,
    modify_by    text        not null,
    create_date  timestamptz not null,
    modify_date  timestamptz not null
);
comment on table hymn.oss_file_record is '文件存储列表 ;; uk:[[object_id field_id data_id file_name]]';
comment on column hymn.oss_file_record.bucket is 'bucket 名称';
comment on column hymn.oss_file_record.file_name is '文件名';
comment on column hymn.oss_file_record.path is '包含文件名的完整路径';
comment on column hymn.oss_file_record.object_id is '所属自定义对象id';
comment on column hymn.oss_file_record.field_id is '所属自定义对像中的字段的id';
comment on column hymn.oss_file_record.data_id is '所属数据id';
comment on column hymn.oss_file_record.size is '文件大小';



drop table if exists hymn.oss_pre_signed_history cascade;
create table hymn.oss_pre_signed_history
(
    id           text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    file_id      text        not null,
    expiry       int         not null,
    create_date  timestamptz not null,
    create_by_id text        not null,
    create_by    text        not null
);
comment on table hymn.oss_pre_signed_history is '创建文件预签url记录，预签url不需要任何权限验证';
comment on column hymn.oss_pre_signed_history.file_id is '文件id ;; idx';
comment on column hymn.oss_pre_signed_history.expiry is '有效时间，单位：秒'
