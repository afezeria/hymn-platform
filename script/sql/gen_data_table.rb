require 'stringio'
require_relative '../config'

table_count = 5
io = StringIO.new

def table_name i
  "core_data_table_#{'%03d' % i}"
end

# core_table_obj_mapping data
io.write <<SQL
insert into hymn.core_table_obj_mapping (table_name, obj_api)
values 
#{ (1..table_count).map { |x| "       ('core_data_table_#{'%03d' % x}',null)" }.join ",\n"};
SQL

io.write "\n"
io.write "\n"
# core_column_field_mapping data
io.write <<EOM
insert into hymn.core_column_field_mapping (table_name, column_name, field_api)
values 
EOM
(1..table_count).each do |x|
  table = table_name x
  io.write <<EOM
       ('#{table}','master001',null),
#{(1..5).map { |i| "       ('#{table}','bool#{'%03d' % i}',null)," }.join("\n")}
#{(1..5).map { |i| "       ('#{table}','mref#{'%03d' % i}',null)," }.join("\n")}
#{(1..5).map { |i| "       ('#{table}','pl_summary#{'%03d' % i}',null)," }.join("\n")}
#{(1..100).map { |i| "       ('#{table}','text#{'%03d' % i}',null)," }.join("\n")}
#{(1..50).map { |i| "       ('#{table}','bigint#{'%03d' % i}',null)," }.join("\n")}
#{(1..50).map { |i| "       ('#{table}','double#{'%03d' % i}',null)," }.join("\n")}
#{(1..20).map { |i| "       ('#{table}','decimal#{'%03d' % i}',null)," }.join("\n")}
#{(1..20).map { |i| "       ('#{table}','datetime#{'%03d' % i}',null)" }.join(",\n")}
#{ x != table_count ? ',' : ';'}
EOM
end

io.write "\n"
io.write "\n"
# data table ddl
(1..table_count).each do |x|
  table = table_name x
  io.write <<EOM
create table hymn.#{table}(
    id           text primary key     default replace(public.uuid_generate_v4()::text, '-', ''),
    owner_id     text        not null,
    create_by_id text        not null,
    modify_by_id text        not null,
    create_date  timestamptz not null,
    modify_date  timestamptz not null,
    type_id      text        not null,
    lock_state   bool        not null default false,
    name         text        not null,
    master001    text,
#{(1..5).map { |i| "    bool#{'%03d' % i}      bool," }.join("\n")}
#{(1..5).map { |i| "    mref#{'%03d' % i}      text," }.join("\n")}
#{(1..100).map { |i| "    text#{'%03d' % i}      text," }.join("\n")}
#{(1..50).map { |i| "    bigint#{'%03d' % i}    bigint," }.join("\n")}
#{(1..50).map { |i| "    double#{'%03d' % i}    double precision," }.join("\n")}
#{(1..20).map { |i| "    decimal#{'%03d' % i}   decimal," }.join("\n")}
#{(1..20).map { |i| "    datetime#{'%03d' % i}  timestamptz" }.join(",\n")}
);

create index #{table}_owner_id_idx on hymn.#{table} (owner_id);
create index #{table}_create_by_id_idx on hymn.#{table} (create_by_id);
create index #{table}_modify_by_id_idx on hymn.#{table} (modify_by_id);
create index #{table}_created_date_idx on hymn.#{table} (create_date);
create index #{table}_modify_date_idx on hymn.#{table} (modify_date);
create index #{table}_name_idx on hymn.#{table} (name);
EOM
end

io.rewind
File.open '6.test-data-table.sql', 'w' do |f|
  f.write io.read
end
