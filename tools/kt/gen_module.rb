require 'pg'
require_relative './module_properties'
require_relative '../config'
require_relative '../constant'

conn = PG.connect Config::DB
table_regex = /^#{Config::MODULE}(?!_data_table).*(?<!history)$/
# table_regex = /^#{Config::MODULE}(?!_data_table).*$/

conn.exec Constant::QUERY_TABLE do |r|
  columns = conn.exec(Constant::QUERY_COLUMN).filter do |c|
    c['comment'] == nil || !c['comment'].start_with?('##ignore')
  end
  indexes = conn.exec(Constant::QUERY_INDEX).to_a
  tables = r.select { |i| i['name'] =~ table_regex }
            .map { |t|
              t = Db::Table.new t
              t.column_arr = columns.filter_map { |c| Db::Column.new c if c['table_name'] == t.name }
              t.index_arr = indexes.filter { |i| i['table_name'] == t.name }
                                   .group_by { |i| i['name'] }
                                   .map { |_, v|
                                     idx = Db::Index.new v[0]
                                     sorted = v.sort_by { |i| i['ord'] }
                                     idx.column_arr = sorted.map { |i|
                                       t.column_arr.find { |c| c.column_name == i['column_name'] }
                                     }
                                     idx
                                   }
              t
            }
  tables = tables.filter do |x|
    x.column_arr.find { |c| c.column_name == 'id' }
  end
  tables.each { |t|
    t.column_arr.each { |i|
      p i.to_json
    }
    # Controller.new(t).write_file
    # Service.new(t).write_file
    # ServiceImpl.new(t).write_file
    Dao.new(t).write_file
    # Entity.new(t).write_file
    # Table.new(t).write_file
    # Dto.new(t).write_file
  }
end

