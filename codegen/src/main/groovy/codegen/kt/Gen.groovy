package codegen.kt

import codegen.Config
import codegen.Constant
import codegen.Mustache

/**
 * @author afezeria
 */
abstract class Gen extends Mustache {
  abstract def getName()

  DbTable _table
  Config _config = Config.instance


  abstract String getPackageName()

  def getTableTag() {
    _table.comment?.split("[ |\n]")?.getAt(0)
  }

  String getControllerName() {
    controllerClassName.uncapitalize()
  }

  String getControllerClassName() {
    entityClassName + 'Controller'
  }

  String getTableName() {
    tableClassName.uncapitalize()
  }

  String getTableClassName() {
    toClassName() + 's'
  }

  String getDaoName() {
    daoClassName.uncapitalize()
  }

  String getDaoImplClassName() {
    daoClassName + 'Impl'
  }

  String getDaoClassName() {
    entityClassName + 'Dao'
  }

  String getEntityName() {
    entityClassName.uncapitalize()
  }

  String getDtoClassName() {
    'D' + entityClassName
  }

  String getEntityClassName() {
    toClassName().replace('SysCore', '')
  }

  def getEntityPackageName(){
    _config.packageName+'.entity'
  }
  def getControllerPackageName(){
    _config.packageName+'.controller'
  }
  def getDtoPackageName(){
    _config.packageName+'.dto'
  }
  def getServicePackageName(){
    _config.packageName+'.dao'
  }
  def getServiceImplPackageName(){
    _config.packageName+'.dao.impl'
  }
  def getTablePackageName(){
    _config.packageName+'.table'
  }

  String toClassName() {
    _table.name.split("_").collect { it.capitalize() }
        .join("")
  }

  def getFields() {
    _table.fields
  }

  def getLateinitFields() {
    _table.fields.findAll { Constant.standardFieldName.contains(it.columnname) }
  }


  protected def getScript(String name) {
    new String(this.class.classLoader.getResourceAsStream("script/${name}.groovy").readAllBytes())
  }

  @Override
  Writer outWriter() {
    if (_config.systemOutput){
      return super.outWriter()
    }

    def root = System.getProperty('user.dir')
    if (_config.outputPath != null && !_config.outputPath.isBlank()) {
      root = _config.outputPath
    }

    def path = root + '/' + getPackageName().replace('.', '/')
    def parent = new File(path)
    if (!parent.exists()) {
      parent.mkdirs()
    }
    def file=new File("$path/${name}.kt")
    if (file.exists()){
      file.delete()
    }
    file.createNewFile()
    println "render path:${file.path}"
    _fileCount++
    file.newWriter()
  }
  static def _fileCount=0
}
