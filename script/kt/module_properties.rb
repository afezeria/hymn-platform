require 'mustache'
require 'fileutils'
require_relative '../extensions.rb'
require_relative '../config'

class Gen < Mustache
  self.template_path = __dir__ + '/template'

  def initialize(table)
    @table = table
  end

  def field_arr
    @table.column_arr
  end

  def table_tag
    @table.comment&.split(/[ \n]/)&.first
  end

  def entity_class_name
    @table.name.sub(/[[:alpha:]]+_/, '').camelize
  end

  def entity_name
    entity_class_name.l_camelize
  end

  def table_class_name
    @table.name.camelize + 's'
  end

  def table_name
    @table.name.l_camelize + 's'
  end

  def package
    Config::PACKAGE + '.' + self.class.to_s.underscore
  end

  def name
    send(self.class.to_s.underscore + '_class_name')
  end

  def file_name
    send(self.class.to_s.underscore + '_class_name') + '.kt'
  end

  def write_file
    path = __dir__ + '/' + package.gsub('.', '/')
    unless File.exist? path
      FileUtils.mkdir_p path
    end
    File.open(path + '/' + file_name, 'w') do |f|
      f.write render
    end
  end

  private

  def method_missing(symbol, *args)
    if symbol.end_with? '_class_name'
      missing_class_name symbol
    elsif symbol.end_with? '_name'
      missing_name symbol
    elsif symbol.end_with? '_package'
      missing_package symbol
    end
  end

  def missing_name(symbol)
    missing_class_name(symbol.to_s.sub(/_name$/, '').to_sym)
      .l_camelize
  end

  def missing_class_name(symbol)
    entity_class_name +
      symbol
        .to_s.sub(/_class_name$/, '')
        .camelize
  end

  def missing_package(symbol)
    Config::PACKAGE +
      '.' +
      symbol.to_s.sub(/_package$/, '')
            .gsub(/_/, '.')
  end

end

module Db
  class Table < FromHash
    attr_accessor :schema, :name, :comment, :column_arr
  end

  class Column < FromHash
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
  def router
    "/module/#{Config::MODULE}/api/#{entity_class_name.dasherize}"
  end
end

class Dao < Gen
end

class Service < Gen
end

class ServiceImpl < Gen
end

