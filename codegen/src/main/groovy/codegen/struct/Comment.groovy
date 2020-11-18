package codegen.struct


import groovy.transform.TupleConstructor

/**
 * @author afezeria
 */
@TupleConstructor(includeSuperFields = true)
class Comment extends Dbobj {
  Dbobj parent
  String value

  @Override
  def toScript() {
    """comment on ${parent.class.simpleName} ${parent.path} is '${value.replace("'", "''")}';"""
  }

  @Override
  public String toString() {
    return value
  }
}
