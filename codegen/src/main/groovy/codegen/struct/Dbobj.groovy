package codegen.struct

/**
 * @author afezeria
 */
abstract class Dbobj {
  String name

  def getName() {
    name
  }

  def getPath() {
    getName()
  }

  def fromMap(Map map) {
  }


  abstract def toScript()

}