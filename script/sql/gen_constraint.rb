require 'pg'
require 'stringio'
require '../constant'
require '../config'

conn = PG.connect Config::DB
table_regex = /^core(?!_data_table).*(?<!history)$/
io = StringIO.new

def index_name(postfix, table, *column)
  n = table +'_' + column.join('_') + '_' + postfix
  return n if n.length < 63
  l = 63 - n.length - postfix.length

  (table.split('_') + column.map { |s| s.split '_' })
    .flatten
    .reverse!
    .map! { |s|
      next s if l == 0
      if s.length > l
        l = 0
        m = l
        s[0..s.length - m]
      else
        l = l - s.length - 1
        s[0]
      end
    }
end

io.write "-- table constraint\n"
conn.exec Constant::QUERY_TABLE do |r|
  r.select { |i| i['name'] =~ table_regex }
   .each { |i|
     if i['comment'] =~ /;;(.*)/
       declare = $1
       declare.match(/uk:\[((\[[^\[\]]+\],?)+)\]/)&.[](1)&.scan(/\[([^\]]+)\]/) do |g|
         io.write <<~SQL
           alter table hymn.#{i['name']}
               add unique (#{g[0].gsub /\s+/, ','});
         SQL
       end
       declare.match(/idx:\[((\[[^\[\]]+\],?)+)\]/)&.[](1)&.scan(/\[([^\]]+)\]/) do |g|
         io.write <<~SQL
           create index #{index_name 'idx', i['name'], *g[0].split(' ')} 
               on hymn.#{i['name']} (#{g[0].gsub /\s+/, ','});
         SQL
       end
     end
   }
end

io.write "\n"
io.write "\n"
io.write "-- column constraint\n"
conn.exec Constant::QUERY_COLUMN do |res|
  res.each do |i|
    if i['comment'] =~ /;;(.*)/
      declare = $1
      if declare.sub! /fk:\[(\w+)\s+([^\]]+)\]/, ''
        io.write <<~SQL
          alter table hymn.#{i['table_name']}
              add foreign key (#{i['column_name']}) references hymn.#{$1} on delete #{$2};
        SQL
      end
      if declare.sub! /optional_value:\[([^\]]+)\]/, ''
        io.write <<~SQL
          alter table hymn.#{i['table_name']}
              add check ( #{i['column_name']} in ('#{$1.scan(/(\w+)(?:\([^)]+\),?)?/).flatten.join '\', \''}') );
        SQL
      end
      if declare.include? 'uk'
        io.write <<~SQL
          alter table hymn.#{i['table_name']}
              add constraint #{index_name 'uk', i['table_name'], i['column_name']} unique (#{i['column_name']});
        SQL
      end
      if declare.include? 'idx'
        io.write <<~SQL
          create index #{index_name 'idx', i['table_name'], i['column_name']}
              on hymn.#{i['table_name']} (#{i['column_name']});
        SQL
      end
    end
  end
end

io.rewind
File.open '5.constraint.sql', 'w' do |f|
  f.write io.read
end
