package codegen.struct


import groovy.transform.ToString
import groovy.transform.TupleConstructor

/**
 * @author afezeria
 */
@TupleConstructor(includeSuperFields = true)
@ToString(includePackage=false,includeNames=true,excludes = 'column')
class Check extends Dbobj {
  Column column
  String expr

  @Override
  def toScript() {
    return null
  }
}
