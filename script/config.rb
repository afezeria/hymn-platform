require 'yaml'

module Config
  DB = YAML.load_file "#{__dir__}/db.yaml"
end