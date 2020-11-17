package codegen

/**
 * @author afezeria
 */
class Constant {
  static def queryTable = """
select t.schemaname as schema, t.tablename as name, cast(obj_description(relfilenode, 'pg_class') as varchar) as comment
from pg_class c
         inner join pg_tables t on t.tablename = c.relname
         inner join pg_namespace pn on c.relnamespace = pn.oid
where t.schemaname = ?
  and pn.nspname = ?
order by t.tablename;
"""
  static def queryColumn = """
SELECT 
       a.attname     AS columnname,
       t.typname     AS sqltype,
       a.attnotnull  AS notnull,
       b.description AS comment
FROM pg_class c,
     pg_namespace pn,
     pg_attribute a
         LEFT OUTER JOIN pg_description b ON a.attrelid = b.objoid AND a.attnum = b.objsubid,
     pg_type t
WHERE c.relname = ?
  and a.attnum > 0
  and a.attrelid = c.oid
  and a.atttypid = t.oid
  and pn.oid = c.relnamespace
  and pn.nspname = ?
ORDER BY a.attnum;
"""
  static def standardFieldName = [
      "id",
      "create_by_id",
      "create_by",
      "modify_by_id",
      "modify_by",
      "create_date",
      "modify_date",
  ]

  static def sqlType2KtormType = [
      "uuid"       : "uuid",
      "text"       : "varchar",
      "int4"       : "int",
      "int8"       : "long",
      "bool"       : "boolean",
      "timestamp"  : "datetime",
      "timestamptz": "datetime",
  ]
  static def sqlType2JavaType = [
      "uuid"       : "UUID",
      "text"       : "String",
      "int4"       : "Int",
      "int8"       : "Long",
      "bool"       : "Boolean",
      "timestamp"  : "LocalDateTime",
      "timestamptz": "LocalDateTime",
  ]

  static def specTitle = [
      "schema", "table", "column", "comment", "type", "defaultValue", "notnull", "primaryKey", "foreignKey", "f_rel_table", "delaction", "updaction", "unique", "check_name", "check_expr",
  ]

  static isNullOrEmptyOrBlank(String str) {
    return str == null || str.isEmpty() || str.isBlank()
  }

}
