package codegen.template

/**
 * @author afezeria
 */
class Dto extends Entity{
  @Override
  def getName() {
    dtoClassName
  }

  @Override
  String getPackageName() {
    dtoPackageName
  }
}
