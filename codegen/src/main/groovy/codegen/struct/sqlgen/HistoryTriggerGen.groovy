package codegen.struct.sqlgen

import codegen.Config
import codegen.kt.DbTable
import codegen.kt.Field
import groovy.sql.Sql

import static codegen.Constant.*

/**
 * 生成历史记录表及历史记录触发器
 * @author afezeria
 */
class HistoryTriggerGen {
  @Delegate
  Config config = Config.instance


  def run() {
    config.init()
    def sql = Sql.newInstance(db)
    def res = sql.rows(queryTable, [schema, schema])
        .findAll { it['name'] =~ ~tableRegex }
        .collect { new DbTable(it) }
        .each {
          it.fields = sql.rows(queryColumn, [it.name, schema])
              .collect { new Field(it) }
        }
        .findAll {
          it.fields.collect { it.columnname }
              .containsAll(standardFieldName)
        }
        .collect {
          """
drop table if exists hymn.${it.name}_history cascade;
create table hymn.${it.name}_history
(
    operation    text,
    stamp        timestamp,
${it.fields.collect { "    ${it.columnname} ${it.sqltype}" }.join(",\n")}
);
create or replace function hymn.${it.name}_history_ins() returns trigger
    language plpgsql as
\$\$
begin
    insert into hymn.${it.name}_history select 'i',now(),new.*;
    return null;
end
\$\$;
create or replace function hymn.${it.name}_history_upd() returns trigger
    language plpgsql as
\$\$
begin
    insert into hymn.${it.name}_history select 'u',now(),new.*;
    return null;
end
\$\$;
create or replace function hymn.${it.name}_history_del() returns trigger
    language plpgsql as
\$\$
begin
    insert into hymn.${it.name}_history select 'd',now(),old.*;
    return null;
end
\$\$;
drop trigger if exists ${it.name}_history_ins on hymn.${it.name};
create trigger ${it.name}_history_ins
    after insert
    on hymn.${it.name}
    for each row
execute function hymn.${it.name}_history_ins();
drop trigger if exists ${it.name}_history_upd on hymn.${it.name};
create trigger ${it.name}_history_upd
    after update
    on hymn.${it.name}
    for each row
execute function hymn.${it.name}_history_upd();
drop trigger if exists ${it.name}_history_del on hymn.${it.name};
create trigger ${it.name}_history_del
    after delete
    on hymn.${it.name}
    for each row
execute function hymn.${it.name}_history_del();

"""
        }
    println()
    return res
  }

  static void main(String[] args) {
    new HistoryTriggerGen().run().each { println it }

  }
}
