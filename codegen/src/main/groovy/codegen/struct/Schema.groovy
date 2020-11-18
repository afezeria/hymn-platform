package codegen.struct

import groovy.transform.ToString
import groovy.transform.TupleConstructor

import static codegen.Constant.specTitle

/**
 * @author afezeria
 */
@TupleConstructor(includeSuperFields = true)
@ToString
class Schema extends Dbobj {
  List<Table> tables = []

  @Override
  def toScript() {
    """
drop schema if exists $name cascade;
create schema $name;

${tables.collect { it.toScript() }.join("\n\n\n")}

"""
  }

  def diff(Schema other) {

  }

  List<Map<String, Object>> toList() {
    def getField = { t, f ->
      try {
        t."$f"?.toString() ?: ""
      } catch (e) {
        ""
      }
    }
    def res = []
    tables.each { table ->
      def tm = [schema: name]
      specTitle.findAll { it != 'schema' }.each { title ->
        tm[title] = getField(table, title)
      }
      res << tm
      table.columns.each { col ->
        def cm = [schema: name, table: table.name]
        specTitle.findAll { !['schema', 'table'].contains(it) }.each { title ->
          cm[title] = getField(col, title)
        }
        cm['column'] = col.name
        cm['primaryKey'] = col.primaryKey != null
        cm['foreignKey'] = col.foreignKey != null
        cm['unique'] = col.unique != null
        if (col.check != null) {
          cm['check_name'] = col.check.name
          cm['check_expr'] = col.check.expr
        }
        res << cm
      }
    }

    res
  }


  @Override
  def fromMap(Map map) {
    def m = map as Map<String, List<Map<String, Object>>>
    name = m.keySet()[0]
    tables = m.values()[0].groupBy { it.table as String }.collect {
      new Table(schema: this).fromMap([(it.key): it.value])
    }
    this
  }

  static def fromList(List<Map<String, Object>> list) {
    new Schema().fromMap([(list[0].schema): list])
  }

  static void main(String[] args) {
    def schema = new Schema('hymn')
    def table = new Table(schema, 'test')
    schema.tables << table
    println schema
  }

}
