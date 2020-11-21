package github.afezeria.hymn.common.sql.history

import github.afezeria.hymn.common.conn
import github.afezeria.hymn.common.sql.BASE_ARRAY
import github.afezeria.hymn.common.sql.BaseDbTest
import github.afezeria.hymn.common.sql.DEFAULT_ORG_ID
import github.afezeria.hymn.common.util.execute
import io.kotest.matchers.maps.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.*

/**
 * @author afezeria
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class HistoryTriggerTest : BaseDbTest() {
    companion object {
        lateinit var id: String

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            conn.use {
                val query = it.execute("insert into hymn.sys_core_org  (name, director_id, deputy_director_id, parent_id, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) values (?,?,?,?,?,?,?,?,?,?) returning id;", "测试组织", null, null, DEFAULT_ORG_ID, *BASE_ARRAY)
                id = query[0]["id"]!! as String
            }
        }
    }

    @Test
    @Order(1)
    fun insertHistoryTest() {
        conn.use {
            val org = it.execute("select * from hymn.sys_core_org where id= ? ", id).run {
                size shouldBe 1
                get(0)
            }
            val orgHistory = it.execute("select * from hymn.sys_core_org_history where id = ?", id).run {
                size shouldBe 1
                get(0)
            }
            orgHistory shouldContainAll org
            orgHistory["operation"] shouldBe "i"
        }
    }

    @Test
    @Order(2)
    fun updateHistoryTest() {
        conn.use {
            it.execute("update hymn.sys_core_org set name= ? where id = ?", "测试组织2", id)
            it.execute("select * from hymn.sys_core_org_history where id = ? and operation = 'u'", id).run {
                size shouldBe 1
            }
        }
    }

    @Test
    @Order(3)
    fun deleteHistoryTest() {
        conn.use {
            it.execute("delete from hymn.sys_core_org where id=?", id)
            it.execute("select * from hymn.sys_core_org_history where id = ? and operation = 'd'", id).run {
                size shouldBe 1
            }
        }
    }
}
