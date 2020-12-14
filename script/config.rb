require 'yaml'

module Config
  f = YAML.load_file "#{__dir__}/config.yaml"
  DB = f['db']
  PACKAGE = f['package']
  MODULE = f['module_name']
end