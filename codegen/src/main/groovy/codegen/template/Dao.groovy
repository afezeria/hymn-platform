package codegen.template

import codegen.Config
import codegen.Function
import codegen.FunctionGenerator
import codegen.Gen

/**
 * @author afezeria
 */
class Dao extends Gen {


  @Override
  def getName() {
    daoClassName
  }

  @Override
  String getPackageName() {
    servicePackageName
  }

  def functions() {
    def funs = []

    def binding = new Binding([
        _service: this,
        fun     : { d, i -> funs << new Function(d, i) }
    ])
    def shell = new GroovyShell(binding)
    shell.evaluate(getScript('dao-common-fun'))

    Config.instance.lineFile.serviceFunList
        .collect { it.split(',') }
        .findAll { it[0] == _table.name }
        .each {
          funs << new FunctionGenerator(it[1], this, it[2] == 'list').create()
        }

    funs
  }

}
