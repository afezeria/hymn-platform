package codegen.struct.sqlgen

import groovy.transform.Canonical

/**
 * @author afezeria
 */
@Canonical
class DataTableGen {
  int tableCount
  String file

  def run() {
    new File(file).withWriter {
      it.write(tableMappingData())
      it.write(columnMappingData())
      it.write(tableSql())
      it.write(seqSql())
      it.close()
    }
  }

  def tableMappingData() {
    """
insert into hymn.sys_core_table_obj_mapping (table_name, obj_api)
values 
${(1..tableCount).collect { "('sys_core_date_table_${String.format('%03d', it)}',null)" }.join(",\n")};
"""
  }

  def columnMappingData() {
    """
insert into hymn.sys_core_column_field_mapping (table_name, column_name, field_api)
values 
""" + (1..tableCount).collect { tn ->
      def table = getTableName(tn)

      """
${(1..100).collect { "('$table','text${String.format('%03d', it)}',null)," }.join("\n")}
${(1..50).collect { "('$table','bigint${String.format('%02d', it)}',null)," }.join("\n")}
${(1..50).collect { "('$table','double${String.format('%03d', it)}',null)," }.join("\n")}
${(1..20).collect { "('$table','decimal${String.format('%03d', it)}',null)," }.join("\n")}
${(1..20).collect { "('$table','datetime${String.format('%03d', it)}',null)${tn == tableCount && it == 20 ? '' : ','}" }.join("\n")}
"""
    }.join() + ";"
  }
  def getTableName(int i){
    "sys_core_data_table_${String.format('%03d', i)}"
  }

  def seqSql() {
    (1..tableCount).collect {
      def table = getTableName(it)
      """
drop sequence if exists hymn.${table}_seq;
create sequence hymn.${table}_seq start 1;"""
    }.join()
  }

  def tableSql() {
    (1..tableCount).collect {
      def table = getTableName(it)
      """
create table hymn.${table}(
    id                text primary key default replace(public.uuid_generate_v4()::text, '-', ''),
    owner_id          text not null,
    create_by_id      text not null,
    modify_by_id      text not null,
    create_date       timestamptz not null,
    modify_date       timestamptz not null,
    type_id           text not null,
    lock_state        int not null,
    name              text not null,
${(1..100).collect { "    text${String.format('%03d', it)} text," }.join("\n")}
${(1..50).collect { "    bigint${String.format('%03d', it)} bigint," }.join("\n")}
${(1..50).collect { "    double${String.format('%03d', it)} double precision," }.join("\n")}
${(1..20).collect { "    decimal${String.format('%03d', it)} decimal," }.join("\n")}
${(1..20).collect { "    datetime${String.format('%03d', it)} timestamptz" }.join(",\n")}
);
create table hymn.${table}_history
(
    id        text,
    operation text,
    stamp     timestamp,
    change    text
);
create index ${table}_owner_id_idx on hymn.${table} (owner_id);
create index ${table}_create_by_id_idx on hymn.${table} (create_by_id);
create index ${table}_modify_by_id_idx on hymn.${table} (modify_by_id);
create index ${table}_created_date_idx on hymn.${table} (create_date);
create index ${table}_modify_date_idx on hymn.${table} (modify_date);
create index ${table}_name_idx on hymn.${table} (name);
"""
    }.join("\n\n\n")
  }

  static void main(String[] args) {
//    new DataTableGen(300,'data-table.sql').run()
    new DataTableGen(5, 'test-data-table.sql').run()


  }
}
