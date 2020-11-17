package codegen.kt.template

import codegen.Config
import codegen.kt.Gen

import static codegen.Constant.standardFieldName

/**
 * @author afezeria
 */
class Entity extends Gen {
  @Override
  def getName() {
    entityClassName
  }

  @Override
  String getPackageName() {
    entityPackageName
  }

  def tableComment() { _table.comment?.split("\n") ?: [] }

  def tableDescription() {
    _table.comment?.with {
      def arr = split("[ |\n]")
      if (arr.length <= 1) {
        null
      } else {
        it - ~/^${arr[0]}/
      }
    }
  }

  def getImports() { Config.instance.lineFile.entityImportList }

  @Override
  def getFields() {
    _table.fields.findAll {!standardFieldName.contains(it.columnname)}
  }



}
