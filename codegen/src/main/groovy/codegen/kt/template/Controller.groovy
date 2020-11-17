package codegen.kt.template

import codegen.kt.Gen

/**
 * @author afezeria
 */
class Controller extends Gen {

  @Override
  def getName() {
    controllerClassName
  }

  @Override
  String getPackageName() {
    controllerPackageName
  }

  def router() {
    "/module/${_config.moduleName}/api/${(_table.name - /^sys_core_/).replace('_', '-')}"
  }

  def functions() {
    def funList = []

    def shell = new GroovyShell()
    def script = shell.parse("")

    def common = new Binding()
    common.setVariable('_controller', this)
    script.binding=common
    script.evaluate(getScript('controller-common-fun'))
    common.variables
        .findAll { it.key != '_controller' && (it.value instanceof GString || it.value instanceof String) }
        .each {
          funList << it.value
        }

    def specify = new Binding()
    specify.setVariable('_controller', this)
    script.binding=specify
    script.evaluate(getScript('controller-fun'))
    specify['mapping'][this._table.name]?.with {
      it.each {
        funList << specify[it]
      }
    }

    funList
  }

}
