require 'pg'
require_relative './module_properties'
require_relative '../config'
require_relative '../constant'

conn = PG.connect Config::DB
table_regex = /^#{Config::MODULE}(?!_data_table).*(?<!history)$/

conn.exec Constant::QUERY_TABLE do |r|
  columns = conn.exec(Constant::QUERY_COLUMN).filter do |c|
    c['comment'] == nil || !c['comment'].start_with?('##ignore')
  end
  tables = r.select { |i| i['name'] =~ table_regex }
            .map { |i|
              t = Db::Table.new i
              t.column_arr = columns.filter_map { |c| Db::Column.new c if c['table_name'] == t.name }
              t
            }
  tables=tables.filter do |x|
    x.name == 'core_biz_object_field'
  end
  tables.each { |t|
    Controller.new(t).write_file
    Service.new(t).write_file
    # ServiceImpl.new(t).write_file
    # Dao.new(t).write_file
    Entity.new(t).write_file
    # Table.new(t).write_file
    # Dto.new(t).write_file
  }
end
