require 'stringio'
require 'pg'
require_relative '../constant'
require_relative '../config'

conn = PG.connect Config::DB
module_name = 'core'
table_regex = /^#{module_name}(?!_data_table).*(?<!history)$/
io = StringIO.new

conn.exec Constant::QUERY_TABLE do |r|
  columns = conn.exec(Constant::QUERY_COLUMN)

  r.filter { |i| (i['name'] =~ table_regex) && !(i['comment'] =~ /;;.*no_history(?![\w ]*\]).*/) }
   .each do |t|
    table_name = t['name']
    io.write <<EOM
drop table if exists hymn.#{table_name}_history cascade;
create table hymn.#{table_name}_history
(
    operation text,
    stamp timestamp,
#{columns.filter { |i| i['table_name'] == table_name }
         .map { |c| "    #{c['column_name']} #{c['sql_type']}" }
         .join ",\n"
    }
);
create or replace function hymn.#{table_name}_history_ins() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.#{table_name}_history select 'i',now(),new.*;
    return null;
end
$$;
create or replace function hymn.#{table_name}_history_upd() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.#{table_name}_history select 'u',now(),new.*;
    return null;
end
$$;
create or replace function hymn.#{table_name}_history_del() returns trigger
    language plpgsql as
$$
begin
    insert into hymn.#{table_name}_history select 'd',now(),old.*;
    return null;
end
$$;
drop trigger if exists #{table_name}_history_ins on hymn.#{table_name};
create trigger #{table_name}_history_ins
    after insert
    on hymn.#{table_name}
    for each row
execute function hymn.#{table_name}_history_ins();
drop trigger if exists #{table_name}_history_upd on hymn.#{table_name};
create trigger #{table_name}_history_upd
    after update
    on hymn.#{table_name}
    for each row
execute function hymn.#{table_name}_history_upd();
drop trigger if exists #{table_name}_history_del on hymn.#{table_name};
create trigger #{table_name}_history_del
    after delete
    on hymn.#{table_name}
    for each row
execute function hymn.#{table_name}_history_del();
EOM

  end
end

io.rewind
File.open '2.history-table-and-trigger.sql', 'w' do |f|
  f.write io.read
end
