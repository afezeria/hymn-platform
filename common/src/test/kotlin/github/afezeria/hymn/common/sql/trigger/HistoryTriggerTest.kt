package github.afezeria.hymn.common.sql.trigger

import github.afezeria.hymn.common.BASE_ARRAY
import github.afezeria.hymn.common.BaseDbTest
import github.afezeria.hymn.common.DEFAULT_ORG_ID
import github.afezeria.hymn.common.adminConn
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
            adminConn.use {
                val query = it.execute(
                    "insert into hymn.core_org  (name, director_id, deputy_director_id, parent_id, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) values (?,?,?,?,?,?,?,?,?,?) returning id;",
                    "测试组织",
                    null,
                    null,
                    DEFAULT_ORG_ID,
                    *BASE_ARRAY
                )
                id = query[0]["id"]!! as String
            }
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            adminConn.use {
                it.execute("delete from hymn.core_org where id = ?", id)
            }
        }
    }

    @Test
    @Order(1)
    fun insertHistoryTest() {
        adminConn.use {
            val org = it.execute("select * from hymn.core_org where id= ? ", id).run {
                size shouldBe 1
                get(0)
            }
            val orgHistory =
                it.execute("select * from hymn.core_org_history where id = ?", id).run {
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
        adminConn.use {
            it.execute("update hymn.core_org set name= ? where id = ?", "测试组织2", id)
            it.execute("select * from hymn.core_org_history where id = ? and operation = 'u'", id)
                .run {
                    size shouldBe 1
                }
        }
    }

    @Test
    @Order(3)
    fun deleteHistoryTest() {
        adminConn.use {
            it.execute("delete from hymn.core_org where id=?", id)
            it.execute("select * from hymn.core_org_history where id = ? and operation = 'd'", id)
                .run {
                    size shouldBe 1
                }
        }
    }
}
