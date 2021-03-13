require 'mustache'
require 'fileutils'
require_relative '../extensions.rb'
require_relative '../config'

class Gen < Mustache
  self.template_path = __dir__ + '/template'
  attr_accessor :table

  def initialize(table)
    @table = table
  end

  def table_comment
    @table.comment == nil ? [] : @table.comment.split("\n")
  end

  def table_tag
    @table.comment&.split(/[ \n]/)&.first
  end

  def entity_class
    @table.name.sub(/[[:alpha:]]+_/, '').camelize
  end

  def entity_name
    entity_class.l_camelize
  end

  def table_class
    @table.name.camelize + 's'
  end

  def table_name
    @table.name.l_camelize + 's'
  end

  def package
    Config::PACKAGE + '.' + self.class.to_s.underscore.gsub('_', '.')
  end

  def name
    send(self.class.to_s.underscore + '_class')
  end

  def file_name
    send(self.class.to_s.underscore + '_class') + '.kt'
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

  public def respond_to_missing?(symbol, *several_variants)
    if symbol.end_with? '_class'
      true
    elsif symbol.end_with? '_name'
      true
    elsif symbol.end_with? '_package'
      true
    else
      super
    end
  end

  def method_missing(symbol, *args)
    if symbol.end_with? '_class'
      missing_class symbol
    elsif symbol.end_with? '_name'
      missing_name symbol
    elsif symbol.end_with? '_package'
      missing_package symbol
    end
  end

  def missing_name(symbol)
    missing_class(symbol.to_s.sub(/_name$/, '').to_sym)
      .l_camelize
  end

  def missing_class(symbol)
    entity_class +
      symbol
        .to_s.sub(/_class$/, '')
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
    attr_accessor :schema, :name, :comment, :column_arr, :index_arr

    def comment_lines
      comment&.split(/\n/)
    end

    def tag
      comment&.split(/[ \n]/)&.first
    end

    def description
      comment&.split(/[ \n]/)&.[](1..)
    end

    def fields
      @column_arr.filter { |f| !Constant::STANDARD_FIELD.include?(f.column_name) }
    end

    def standard_fields
      @column_arr.filter { |f| Constant::STANDARD_FIELD.include?(f.column_name) && f.column_name != 'id' }
    end
  end

  class Index < FromHash
    attr_accessor :table_name, :name, :is_pk, :is_uk, :column_arr

    def fun_name
      @column_arr.map { |c| c.column_name.camelize }
                 .join('And')
    end

    def is_pk=(i)
      case i
      when 't'
        @is_pk = true
      when 'f'
        @is_pk = false
      else
        @is_pk = nil
      end
    end

    def is_uk=(i)
      case i
      when 't'
        @is_uk = true
      when 'f'
        @is_uk = false
      else
        @is_uk = nil
      end
    end

  end

  class Column < FromHash
    attr_accessor :column_name, :sql_type, :not_null, :comment

    def field_name
      @column_name.l_camelize
    end

    def java_type
      Constant::JAVA_TYPE[@sql_type]
    end

    def ktorm_type
      Constant::KTORM_TYPE[@sql_type]
    end

    def not_null=(i)
      case i
      when 't'
        @not_null = true
      when 'f'
        @not_null = false
      else
        @not_null = nil
      end
    end
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
    "/#{Config::MODULE}/api/{version}/#{entity_class.dasherize}"
  end
end

class Dao < Gen
end

class Service < Gen
end

class ServiceImpl < Gen
end

