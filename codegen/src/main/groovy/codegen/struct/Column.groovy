package codegen.struct


import groovy.transform.ToString
import groovy.transform.TupleConstructor

import static codegen.Constant.isNullOrEmptyOrBlank

/**
 * @author afezeria
 */
@TupleConstructor(includeSuperFields = true)
@ToString(includePackage = false, includeNames = true, excludes = 'table')
class Column extends Dbobj {
  Table table
  String defaultValue
  String type
  boolean notnull
  Comment comment
  ForeignKey foreignKey
  PrimaryKey primaryKey
  Unique unique
  Check check

  @Override
  def fromMap(Map m) {
    name = m.column
    defaultValue = m.defaultValue
    type = m.type
    notnull = m.notnull.toBoolean()
    if (!isNullOrEmptyOrBlank(m.comment)) {

      comment = new Comment(parent: this, value: m.comment)
    }
    if (m.primaryKey.toBoolean()) {
      primaryKey = new PrimaryKey(column: this)
    }
    if (m.foreignKey.toBoolean()) {
      foreignKey = new ForeignKey(column: this, table: m.f_rel_table, delaction: m.delaction, updaction: m.updaction)
    }
    if (m.unique.toBoolean()) {
      unique = new Unique(column: this)
    }
    if (!isNullOrEmptyOrBlank(m.check_name)) {
      check = new Check(name: m.check_name, column: this, expr: m.check_expr)
    }
    this
  }

  @Override
  def getPath() {
    "$table.path.$name"
  }

  @Override
  def toScript() {
    """    "$name"  $type ${defaultValue==null?"":"default $defaultValue"} ${notnull ? "not null" : ""}"""
  }

  static void main(String[] args) {
  }

}
