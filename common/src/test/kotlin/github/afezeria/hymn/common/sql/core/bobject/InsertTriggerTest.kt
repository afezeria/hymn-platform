package github.afezeria.hymn.common.sql.core.bobject

import github.afezeria.hymn.common.BaseDbTest
import github.afezeria.hymn.common.COMMON_INFO
import github.afezeria.hymn.common.adminConn
import github.afezeria.hymn.common.clearBObject
import github.afezeria.hymn.common.util.execute
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException

/**
 * @author afezeria
 */
class InsertTriggerTest : BaseDbTest() {
    @AfterEach
    fun deleteObject() {
        clearBObject()
    }

    @Test
    fun `cannot end with _history`() {
        adminConn.use {
            val e = shouldThrow<PSQLException> {
                it.execute(
                    """
                insert into hymn.core_biz_object(name,api,create_by_id,create_by,modify_by_id,
                    modify_by,create_date,modify_date)
                values ('测试对象','test_obj_history',?,?,?,?,now(),now()) returning *;
                """,
                    *COMMON_INFO
                )
            }
            e.message shouldContain "对象api不能以 _history 结尾"
        }
    }

    @Test
    fun `the active property of the new object must be true`() {
        adminConn.use {
            val data = it.execute(
                """
                insert into hymn.core_biz_object(type,active,name,api,create_by_id,create_by,modify_by_id,
                    modify_by,create_date,modify_date)
                values ('custom',false,'测试对象','test_obj',?,?,?,?,now(),now()) returning *;
                """,
                *COMMON_INFO
            )[0]
            data["active"] shouldBe true
        }
    }

    @Test
    fun `custom objects automatically add postfix __co`() {
        adminConn.use {
            val data = it.execute(
                """
                insert into hymn.core_biz_object(type,name,api,create_by_id,create_by,modify_by_id,
                    modify_by,create_date,modify_date)
                values ('custom','测试对象','test_obj',?,?,?,?,now(),now()) returning *;
                """,
                *COMMON_INFO
            )[0]
            data["api"] shouldBe "test_obj__co"
        }
    }

    @Test
    fun `remote objects automatically add postfix __cr`() {
        adminConn.use {
            val data = it.execute(
                """
                insert into hymn.core_biz_object(type,name,api,create_by_id,create_by,modify_by_id,
                    modify_by,create_date,modify_date)
                values ('remote','测试对象','test_obj',?,?,?,?,now(),now()) returning *;
                """,
                *COMMON_INFO
            )[0]
            data["api"] shouldBe "test_obj__cr"
        }
    }

    @Test
    fun `the number of custom objects reaches the upper limit`() {
        adminConn.use {
            val e = shouldThrow<PSQLException> {
                for (i in (1..10)) {
                    it.execute(
                        """
                        insert into hymn.core_biz_object(type,name,api,create_by_id,create_by,modify_by_id,
                            modify_by,create_date,modify_date)
                        values ('custom','测试对象','ab${i}',?,?,?,?,now(),now()) returning *;
                        """,
                        *COMMON_INFO
                    )[0]
                }
            }
            e.message shouldContain "自定义对象的数量已达到上限"
        }
    }

    @Test
    @Order(1)
    fun `the source_table property must be set to create a module object`() {
        adminConn.use {
            val e = shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object(name,api,type,module_api,remark,create_by_id,create_by,
                        modify_by_id,modify_by,create_date,modify_date)
                    values ('模块对象','module_obj','module','core','模块对象',?,?,?,?,now(),now()) returning *;
                    """,
                    *COMMON_INFO
                )
            }
            assertSoftly {

                e.sqlState shouldBe "P0001"
                e.message shouldContain "创建模块对象必须指定数据表"
            }
        }
    }

    @Test
    fun `throw when source_table does not exists and type is module`() {
        adminConn.use {
            val e = shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object(source_table,name,api,type,module_api,remark,
                        create_by_id,create_by,modify_by_id,modify_by,create_date,modify_date)
                    values ('sys_test_obj','模块对象','module_obj','module','core','模块对象',
                        ?,?,?,?,now(),now()) returning *;
                    """,
                    *COMMON_INFO
                )
            }
            assertSoftly {
                e.sqlState shouldBe "P0001"
                e.message shouldContain "表 .*? 不存在".toRegex()
            }
        }
    }

    @Test
    fun `create history trigger if type is custom`() {
        adminConn.use {
            var data = it.execute(
                """
                insert into hymn.core_biz_object(type,name,api,create_by_id,create_by,modify_by_id,
                    modify_by,create_date,modify_date)
                values ('custom','测试对象','test_obj',?,?,?,?,now(),now()) returning *;
                """,
                *COMMON_INFO
            )[0]
            var api = data["api"] as String
            it.execute(
                """
                select * from pg_class pc left join pg_namespace pn on pc.relnamespace = pn.oid
                where pn.nspname='hymn_view'
                and pc.relname='${api}_history'
            """
            ).size shouldBe 1
            it.execute(
                """
                   select * 
                   from pg_class pc 
                   left join pg_namespace pn on pc.relnamespace = pn.oid
                   left join pg_trigger pt on pt.tgrelid=pc.oid
                   where pn.nspname='hymn'
                   and pc.relname='${data["source_table"]}'
                   and pt.tgname='a99_${data["source_table"]}_history'
                """
            ).size shouldBe 1


            it.execute("create table hymn.module_table1(id serial);")
            try {
                data = it.execute(
                    """
                insert into hymn.core_biz_object(type,source_table,name,api,create_by_id,create_by,modify_by_id,
                    modify_by,create_date,modify_date)
                values ('module','module_table1','模块对象','test_obj',?,?,?,?,now(),now()) returning *;
                """,
                    *COMMON_INFO
                )[0]
                api = data["api"] as String
                it.execute(
                    """
                select * from pg_class pc left join pg_namespace pn on pc.relnamespace = pn.oid
                where pn.nspname='hymn_view'
                and pc.relname='${api}_history'
            """
                ).size shouldBe 0
                it.execute(
                    """
                   select * 
                   from pg_class pc 
                   left join pg_namespace pn on pc.relnamespace = pn.oid
                   left join pg_trigger pt on pt.tgrelid=pc.oid
                   where pn.nspname='hymn'
                   and pc.relname='${data["source_table"]}'
                   and pt.tgname='${data["source_table"]}_history'
                """
                ).size shouldBe 0
            } finally {
                it.execute("drop table hymn.module_table1 cascade ")
            }

        }
    }

    @Test
    fun `create view if type in (custom,module)`() {
        adminConn.use {
            var data = it.execute(
                """
                insert into hymn.core_biz_object(type,name,api,create_by_id,create_by,modify_by_id,
                    modify_by,create_date,modify_date)
                values ('custom','测试对象','test_obj',?,?,?,?,now(),now()) returning *;
                """,
                *COMMON_INFO
            )[0]
            var api = data["api"] as String
            it.execute(
                """
                select * from pg_class pc left join pg_namespace pn on pc.relnamespace = pn.oid
                where pn.nspname='hymn_view'
                and pc.relname='${api}'
            """
            ).size shouldBe 1

            it.execute("create table hymn.module_table1(id serial);")
            try {
                data = it.execute(
                    """
                insert into hymn.core_biz_object(type,source_table,name,api,create_by_id,create_by,modify_by_id,
                    modify_by,create_date,modify_date)
                values ('module','module_table1','模块对象','test_obj',?,?,?,?,now(),now()) returning *;
                """,
                    *COMMON_INFO
                )[0]
                api = data["api"] as String
                it.execute(
                    """
                select * from pg_class pc left join pg_namespace pn on pc.relnamespace = pn.oid
                where pn.nspname='hymn_view'
                and pc.relname='${api}'
            """
                ).size shouldBe 1
            } finally {
                it.execute("drop table hymn.module_table1 cascade ")
            }

        }
    }

}
