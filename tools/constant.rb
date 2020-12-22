module Constant
  QUERY_TABLE = <<SQL
select pn.nspname as schema, c.relname as name, cast(obj_description(relfilenode, 'pg_class') as varchar) as comment
from pg_class c
         inner join pg_tables t on t.tablename = c.relname
         inner join pg_namespace pn on c.relnamespace = pn.oid
where
   pn.nspname = 'hymn'
and c.relkind = 'r'
order by c.relname;
SQL

  QUERY_COLUMN = <<SQL
SELECT c.relname     as table_name,
       a.attname     AS column_name,
       t.typname     AS sql_type,
       a.attnotnull  AS not_null,
       b.description AS comment
FROM pg_class c,
     pg_namespace pn,
     pg_attribute a
         LEFT OUTER JOIN pg_description b ON a.attrelid = b.objoid AND a.attnum = b.objsubid,
     pg_type t
WHERE a.attnum > 0
  and a.attrelid = c.oid
  and a.atttypid = t.oid
  and pn.oid = c.relnamespace
  and pn.nspname = 'hymn'
ORDER BY c.relname, a.attnum;
SQL
  QUERY_INDEX = <<SQL
select pc2.relname as table_name,
       pc.relname  as name,
       case pi.indisprimary
           when true then true
           else false
           end     as is_pk,
       case pi.indisunique
           when true then true
           else false
           end     as is_uk,
       pa.attname  as column_name,
       pa.attnum   as ord
from pg_class pc
         left join pg_namespace pn on pn.oid = pc.relnamespace
         left join pg_attribute pa on pa.attrelid = pc.oid
         left join pg_index pi on pi.indexrelid = pc.oid
         left join pg_class pc2 on pc2.oid = pi.indrelid
where pc.relkind = 'i'
  and pn.nspname = 'hymn'
  and pi.indisprimary = false;
SQL

  STANDARD_FIELD = %w[create_by_id create_by modify_by_id modify_by create_date modify_date id]

  JAVA_TYPE = {
    "uuid" => "UUID",
    "text" => "String",
    "int4" => "Int",
    "int8" => "Long",
    "bool" => "Boolean",
    "timestamp" => "LocalDateTime",
    "timestamptz" => "LocalDateTime",
  }

  KTORM_TYPE = {
    "uuid" => "uuid",
    "text" => "varchar",
    "int4" => "int",
    "int8" => "long",
    "bool" => "boolean",
    "timestamp" => "datetime",
    "timestamptz" => "datetime",
  }
end