require 'mustache'

class Gen < Mustache
  self.template_path = './template'

  def initialize(table)
    @table = table
  end

  def a
    'abc'
  end
end

module Db
  class Table
    attr_accessor :schema, :name, :comment, :column_arr
  end

  class Column
    attr_accessor :name, :sql_type, :not_null, :comment
  end
end

class Table < Gen
end

class Entity < Gen
end

class Dto < Gen
end

class Controller < Gen
end

class Dao < Gen
end

class Service < Gen
end

class ServiceImpl < Gen
end

