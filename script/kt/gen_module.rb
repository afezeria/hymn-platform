require 'pg'
require_relative './module_properties'
require_relative '../config'

conn = PG.connect Config::DB
table_regex = /^#{Config::MODULE}(?!_data_table).*(?<!history)$/

conn.exec Constant::QUERY_TABLE
