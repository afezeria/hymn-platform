package codegen

import groovy.sql.Sql

import static codegen.Constant.getQueryTable

/**
 * @author afezeria
 */
class HistoryTriggerGen {
  @Delegate
  Config config = Config.instance


  def run() {
    config.init()
    def sql = Sql.newInstance(db)
    sql.rows(queryTable, [schema, schema])
        .findAll {
          !lineFile.ignoreTableList.contains(it['name'])
              &&
              it['name'] =~ ~tableRegex
        }
        .collect { new DbTable(it) }
        .each {

          println """
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
create trigger ${it.name}_history_ins
    after insert
    on hymn.${it.name}
    for each row
execute function hymn.${it.name}_history_ins();
create trigger ${it.name}_history_upd
    after update
    on hymn.${it.name}
    for each row
execute function hymn.${it.name}_history_upd();
create trigger ${it.name}_history_del
    after delete
    on hymn.${it.name}
    for each row
execute function hymn.${it.name}_history_del();

"""

        }


  }

  static void main(String[] args) {
    new HistoryTriggerGen().run()

  }
}
