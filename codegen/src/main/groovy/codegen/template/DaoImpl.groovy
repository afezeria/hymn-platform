package codegen.template
/**
 * @author afezeria
 */
class DaoImpl extends Dao{
  @Override
  def getName() {
    daoImplClassName
  }

  @Override
  String getPackageName() {
    serviceImplPackageName
  }
}
