package codegen.kt.template
/**
 * @author afezeria
 */
class Table extends Entity {
  @Override
  def getName() {
    tableClassName
  }

  @Override
  String getPackageName() {
    tablePackageName
  }
}