package codegen

import codegen.template.Controller
import codegen.template.Dto
import codegen.template.Entity
import codegen.template.Dao
import codegen.template.DaoImpl
import codegen.template.Table
import groovy.sql.Sql

import static codegen.Constant.getQueryTable
import static codegen.Constant.queryColumn

/**
 * @author afezeria
 */
@Newify(pattern = '.*')
class Main {
  @Delegate
  Config config = Config.instance

  def run() {
    config.init()
    def sql = Sql.newInstance(db)
    def tables = sql.rows(queryTable, [schema, schema])
        .findAll { !lineFile.ignoreTableList.contains(it['name']) && it['name'] =~ ~tableRegex}
        .collect { new DbTable(it) }
        .each {
          it.fields = sql.rows(queryColumn, [it.name, schema])
              .collect { new Field(it) }
        }

    tables.each {table->
      config.templates.each {name->
        switch (name){
          case Controller.simpleName:
            Controller(_table: table).render()
            break
          case Dto.simpleName:
            Dto(_table: table).render()
            break
          case Entity.simpleName:
            Entity(_table: table).render()
            break
          case Dao.simpleName:
            Dao(_table: table).render()
            break
          case DaoImpl.simpleName:
            DaoImpl(_table: table).render()
            break
          case Table.simpleName:
            Table(_table: table).render()
            break
        }
      }
    }
    println "${Gen._fileCount} files in total"
    println()
  }

  static void main(String[] args) {
    new Main().run()
  }
}
