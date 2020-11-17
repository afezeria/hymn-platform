package codegen.struct

import codegen.Config
import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook

/**
 * @author afezeria
 */
class Main {
  @Delegate
  Config config = Config.instance

  static void main(String[] args) {
    Config.instance.init()
//    new Main().genSpec('hymn','./spec.xlsx')
//    genScript("./spec.xlsx")
    db2List('hymn')
  }

  static def db2List(schemaName) {
    def sql = Sql.newInstance(Config.instance.db)
    def rows = sql.rows(str, schemaName, schemaName)
    def schema = Schema.fromList(rows)
    println schema.toScript()
    def list = schema.toList()
    println()
  }


  static def workbook2List(Workbook wb) {
    def sheet = wb.getSheetAt(0)
    def titles = sheet.getRow(0).cellIterator().collect { it.getStringCellValue() }
    (1..467).collect {
      def row = sheet.getRow(it)
      def m = [:]
      titles.eachWithIndex { String entry, int i ->
        def type = row.getCell(i).getCellType()
        if (type == CellType.BOOLEAN) {
          m[entry] = row.getCell(i).getBooleanCellValue()
        } else {
          m[entry] = row.getCell(i).getStringCellValue()
        }

      }
      m
    }
  }

  def list2workbook(List<Map<String, Object>> list) {
    def wb = new XSSFWorkbook()
    def sheet = wb.createSheet()
    def keys = list[0].keySet() as Set<String>
    sheet.createRow(0).with {
      keys.eachWithIndex { entry, i ->
        it.createCell(i).setCellValue(entry)
      }
    }
    list.eachWithIndex { GroovyRowResult entry, int i ->
      sheet.createRow(i + 1).with { row ->
        keys.eachWithIndex { String key, int j ->
          row.createCell(j).setCellValue(entry[key])
        }
      }
    }
    def file = new File(writePath)
    def stream = file.newOutputStream()
    wb.write(stream)
    stream.close()
  }

  def genSpec(schemaName, writePath) {
    def sql = Sql.newInstance(db)
    def rows = sql.rows(str, schemaName, schemaName)
    list2workbook(rows)
  }

  static isNullOrEmptyOrBlank(String str) {
    return str == null || str.isEmpty() || str.isBlank()
  }

  static String str = """
select t.schema,
       "table",
       "column",
       comment,
       type,
       "default" as "defaultValue",
       "notnull",
       case
           when p_constraint_name is null then false
           else true
           end           as "primaryKey",
       case
           when f_constraint_name is null then false
           else true
           end           as "foreignKey",
       f_rel_table,
       delaction,
       updaction,
       case
           when u_constraint_name is null then false
           else true
           end           as "unique",
       c_constraint_name as "check_name",
       "check"           as "check_expr"
from (
         with pkey as (
             select pns.nspname  as "schema",
                    pclm.relname as "table_name",
                    pa.attname   as "column",
                    pcs.conname  as constraint_name
             from pg_constraint pcs
                      left join pg_namespace pns on pns.oid = pcs.connamespace
                      left join pg_class pclm on pclm.oid = pcs.conrelid
                      left join pg_attribute pa on pa.attrelid = pclm.oid and pa.attnum = pcs.conkey[1]
             where pcs.contype = 'p'
         ),
              fkey as (
                  select pns.nspname  as "schema",
                         pclm.relname as "table_name",
                         pcls.relname as "rel_table_name",
                         pa.attname   as "column",
                         pcs.conname  as constraint_name,
                         case pcs.confdeltype
                             when 'a' then 'none'
                             when 'r' then 'restrict'
                             when 'c' then 'on cascade'
                             when 'n' then 'set null'
                             when 'd' then 'set default'
                             end      as delaction,
                         case pcs.confupdtype
                             when 'a' then 'none'
                             when 'r' then 'restrict'
                             when 'c' then 'on cascade'
                             when 'n' then 'set null'
                             when 'd' then 'set default'
                             end      as updaction
                  from pg_constraint pcs
                           left join pg_namespace pns on pns.oid = pcs.connamespace
                           left join pg_class pclm on pclm.oid = pcs.conrelid
                           left join pg_class pcls on pcls.oid = pcs.confrelid
                           left join pg_attribute pa on pa.attrelid = pclm.oid and pa.attnum = pcs.conkey[1]
                  where pcs.contype = 'f'
              ),
              ukey as (
                  select pns.nspname  as "schema",
                         pclm.relname as "table_name",
                         pa.attname   as "column",
                         pcs.conname  as constraint_name
                  from pg_constraint pcs
                           left join pg_namespace pns on pns.oid = pcs.connamespace
                           left join pg_class pclm on pclm.oid = pcs.conrelid
                           left join pg_attribute pa on pa.attrelid = pclm.oid and pa.attnum = pcs.conkey[1]
                  where pcs.contype = 'u'
              ),
              ckey as (
                  select pns.nspname                   as "schema",
                         pclm.relname                  as "table_name",
                         pa.attname                    as "column",
                         pcs.conname                   as constraint_name,
                         pg_get_constraintdef(pcs.oid) as "constraint"
                  from pg_constraint pcs
                           left join pg_namespace pns on pns.oid = pcs.connamespace
                           left join pg_class pclm on pclm.oid = pcs.conrelid
                           left join pg_attribute pa on pa.attrelid = pclm.oid and pa.attnum = pcs.conkey[1]
                  where pcs.contype = 'c'
              )
         select pn.nspname           as schema,
                pc.relname           as "table",
                pa.attname           as "column",
                pd.description       as "comment",
                pa.attnum            as "ord",
                pt.typname           as "type",
                CASE
                    WHEN pa.attgenerated = ''::"char" THEN pg_get_expr(pad.adbin, pad.adrelid)
                    ELSE NULL::text
                    END              as "default",
                pa.attnotnull        as "notnull",
                pkey.constraint_name as "p_constraint_name",
                fkey.constraint_name as "f_constraint_name",
                fkey.rel_table_name  as "f_rel_table",
                fkey.delaction,
                fkey.updaction,
                ukey.constraint_name as "u_constraint_name",
                ckey.constraint_name as "c_constraint_name",
                ckey."constraint"    as "check"

         from pg_namespace pn
                  left join pg_class pc on pc.relnamespace = pn.oid
                  left join pg_attribute pa on pc.oid = pa.attrelid
                  left join pg_attrdef pad ON pa.attrelid = pad.adrelid AND pa.attnum = pad.adnum
                  left join pg_description pd on pc.oid = pd.objoid and pa.attnum = pd.objsubid
                  left join pg_type pt on pa.atttypid = pt.oid
                  left join pkey
                            on pkey.schema = pn.nspname and pkey.table_name = pc.relname and pkey."column" = pa.attname
                  left join fkey
                            on fkey.table_name = pc.relname and fkey.schema = pn.nspname and fkey."column" = pa.attname
                  left join ukey
                            on ukey.table_name = pc.relname and ukey.schema = pn.nspname and ukey."column" = pa.attname
                  left join ckey
                            on ckey.table_name = pc.relname and ckey.schema = pn.nspname and ckey."column" = pa.attname
         where pn.nspname = ?
           and pa.attnum > 0
           and pc.relkind = 'r'
         union all
         select pn.nspname     as schema,
                pc.relname     as "table",
                null,
                pd.description as "comment",
                0              as "ord",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
         from pg_class pc
                  left join pg_description pd on pd.objoid = pc.oid
                  left join pg_namespace pn on pn.oid = pc.relnamespace
         where (pd.objsubid = 0 or pd.objsubid is null)
           and pc.relkind = 'r'
           and pn.nspname = ?
     ) as t

order by "table", t.ord;

"""
}
