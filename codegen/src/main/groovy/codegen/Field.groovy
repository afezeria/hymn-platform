package codegen

import groovy.transform.Canonical
import static codegen.Constant.*
import static codegen.Constant.sqlType2JavaType
import static codegen.Constant.sqlType2KtormType

/**
 * @author afezeria
 */
@Canonical
class Field {
  String columnname
  String sqltype
  boolean notnull
  String comment

  def getFieldName() {
    columnname.split('_')
        .collect { it.capitalize() }
        .join('')
        .uncapitalize()
  }

  def getJavaType(){
    sqlType2JavaType[sqltype]
  }
  def getKtormType(){
    sqlType2KtormType[sqltype]
  }


}
