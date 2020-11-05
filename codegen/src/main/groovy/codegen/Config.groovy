package codegen

import groovy.yaml.YamlSlurper

/**
 * @author afezeria
 */
@Singleton
class Config {

  LineFile lineFile
  def db
  String schema
  String packageName
  String moduleName
  String outputPath
  Boolean systemOutput
  List<String> templates

  String tableRegex

  class LineFile {
    List<String> entityImportList
    List<String> ignoreTableList
    List<String> serviceFunList
  }

  Config init() {
    def yaml = new YamlSlurper().parse(getStream('config.yaml'))
    schema = yaml.schema
    packageName = yaml.packageName
    moduleName = yaml.moduleName
    tableRegex = yaml.tableRegex
    outputPath = yaml.outputPath
    systemOutput = yaml.systemOutput
    templates = yaml.templates
    db = yaml.db
    lineFile = new LineFile().tap { l ->
      l.metaPropertyValues.findAll { it.name != 'class' }
          .each {
            it.value = getStream(it.name.replaceAll(/[A-Z]/) {
              '-' + it.toLowerCase()
            })
                .readLines()
          }
    }

    instance
  }

  InputStream getStream(String path) {
    this.class.classLoader.getResourceAsStream(path)
  }

}
