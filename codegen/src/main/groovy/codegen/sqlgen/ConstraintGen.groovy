package codegen.sqlgen

import codegen.Config
import codegen.Constant
import codegen.DbTable
import codegen.Field
import groovy.sql.Sql

/**
 * @author afezeria
 */
class ConstraintGen {
  @Delegate
  Config config = Config.instance


  def run(Closure<Boolean> filter, Closure<String> template) {
    config.init()
    def sql = Sql.newInstance(db)
    def tables = sql.rows(Constant.queryTable, [schema, schema])
        .findAll { it['name'] =~ ~tableRegex }
        .collect { new DbTable(it) }
        .each {
          it.fields = sql.rows(Constant.queryColumn, [it.name, schema])
              .collect { new Field(it) }
        }
        .findAll(filter)
        .each {
          println template.call(it)
        }
    println()
  }

  static void main(String[] args) {
    def gen = new ConstraintGen()
    gen.run({
      it.fields.collect { it.columnname }
          .contains("role_id")
    }, {
      """
alter table hymn.${it.name}
    add foreign key (role_id) references hymn.sys_core_role on delete cascade;
"""
    })

    gen.run({
      it.fields.collect { it.columnname }
          .containsAll("create_by_id", "modify_by_id")
    }, {
      """
create index ${it.name.replace("sys_core_","")}_create_by_id_idx on hymn.${it.name} (create_by_id);
create index ${it.name.replace("sys_core_","")}_modify_by_id_idx on hymn.${it.name} (modify_by_id);
"""
    })
  }
}
