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
end