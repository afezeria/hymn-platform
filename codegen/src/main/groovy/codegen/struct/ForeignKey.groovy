package codegen.struct


import groovy.transform.ToString
import groovy.transform.TupleConstructor

/**
 * @author afezeria
 */
@TupleConstructor(includeSuperFields = true)
@ToString(includePackage=false,includeNames=true,excludes = 'column,table')
class ForeignKey extends Dbobj {
  Column column
  String table
  String delaction
  String updaction

  @Override
  def toScript() {
    return null
  }
}
