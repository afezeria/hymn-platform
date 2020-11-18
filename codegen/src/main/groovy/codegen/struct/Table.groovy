package codegen.struct


import groovy.transform.ToString
import groovy.transform.TupleConstructor

import static codegen.Constant.isNullOrEmptyOrBlank

/**
 * @author afezeria
 */
@TupleConstructor(includeSuperFields = true)
@ToString(includePackage = false, includeNames = true, excludes = 'schema')
class Table extends Dbobj {
  Schema schema
  List<Column> columns = []
  Comment comment

  @Override
  def getPath() {
    "$schema.name.$name"
  }

  @Override
  def fromMap(Map map) {
    def m = map as Map<String, List<Map<String, Object>>>
    def tm = m.values()[0].find { isNullOrEmptyOrBlank(it.column) }
    name = tm.table
    if (!isNullOrEmptyOrBlank(tm.comment)) {
      comment = new Comment(parent: this, value: tm.comment)
    }
    m.values()[0].findAll { !isNullOrEmptyOrBlank(it.column) }.each {
      def cm = it as Map<String, Object>
      columns << new Column(table: this).fromMap(cm)
    }
    return this
  }

  @Override
  def toScript() {
    """
drop table if exists $schema.name.$name cascade;
create table $schema.name.$name
(
${columns.collect { it.toScript() }.join(",\n")}
);${comment != null ? "\n${comment.toScript()}" : ""}
${columns.findAll { it.comment != null }.collect { it.comment.toScript() }.join("\n")}

"""
  }

  def getConstraints() {

  }

}
