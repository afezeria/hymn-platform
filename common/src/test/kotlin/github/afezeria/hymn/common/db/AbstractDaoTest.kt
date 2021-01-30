package github.afezeria.hymn.common.db

import github.afezeria.hymn.common.adminConn
import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.common.constant.ClientType
import github.afezeria.hymn.common.platform.Session
import github.afezeria.hymn.common.util.execute
import github.afezeria.hymn.common.util.randomUUIDStr
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.*
import org.ktorm.dsl.eq
import java.math.BigDecimal
import java.time.LocalDate

/**
 * @author afezeria
 */
internal class AbstractDaoTest {
    companion object {
        val table = TestTables()
        val entity = TestTable(1, 2L, 3.0, BigDecimal("4.5"), true).apply {
            orgId = "abc"
        }
        val dao = TestTableDao(TestDatabaseServiceImpl)
        val session: Session =
            Session(
                id = randomUUIDStr(),
                accountType = AccountType.ADMIN,
                accountId = randomUUIDStr(),
                accountName = "root",
                clientType = ClientType.BROWSER,
                roleId = randomUUIDStr(),
                roleName = "abc",
                orgId = randomUUIDStr(),
                orgName = "efg",
            )

        @JvmStatic
        @BeforeAll
        fun before() {
            Sql.init()
            Session.current.set(session)
        }

        @JvmStatic
        @AfterAll
        fun after() {
            Sql.clear()
            Session.current.remove()
        }
    }

    @BeforeEach
    fun beforeEach() {
        adminConn.use {
            it.execute(
                """
                    insert into test_dao.test_table (id, inta, longa, doublea, decimala, boola, lazya, create_by_id, create_by,
                                 modify_by_id, modify_by, role_id, role_name, org_id, org_name, create_date,
                                 modify_date)
                    values ('cc25e3c045390139d47e1cb6d0c265af', 2, 42424204040, 3.2, 42.42420913, true, 'abc',
                            'cc25e3c045390139d47e1cb6d0c265af', 'admin', 'cc25e3c045390139d47e1cb6d0c265af', 'admin',
                            'cc25e3c045390139d47e1cb6d0c265af', 'admin', 'cc25e3c045390139d47e1cb6d0c265af', 'admin', '2020-01-01 00:00:00',
                            '2020-01-01 00:00:00'),
                           ('d2dbcc6045390139d47f1cb6d0c265af', 2, null, 3.2, 42.42420913, true, true,
                            'cc25e3c045390139d47e1cb6d0c265af', 'admin', 'cc25e3c045390139d47e1cb6d0c265af', 'admin',
                            'cc25e3c045390139d47e1cb6d0c265af', 'admin', 'cc25e3c045390139d47e1cb6d0c265af', 'admin', '2020-01-01 00:00:00',
                            '2020-01-01 00:00:00')
            """
            )
        }
    }

    @AfterEach
    fun afterEach() {
        adminConn.use {
            it.execute("delete from test_dao.test_table")
        }
    }

    @Test
    fun delete() {
        val res = dao.delete { it.inta eq 2 }
        res shouldBe 2
    }

    @Test
    fun deleteById() {
        dao.deleteById("cc25e3c045390139d47e1cb6d0c265af") shouldBe 1
    }

    @Test
    fun updateEntity() {
        var entity = dao.selectById("cc25e3c045390139d47e1cb6d0c265af")!!
        entity.apply {
            assertSoftly {
                createBy shouldBe "admin"
                inta shouldNotBe 100
                modifyBy shouldNotBe "root"
                modifyById shouldNotBe session.accountId
                modifyDate.toLocalDate() shouldNotBe LocalDate.now()
                roleId shouldNotBe session.roleId
                roleName shouldNotBe session.roleName
                orgId shouldNotBe session.orgId
                orgName shouldNotBe session.orgName
            }
        }
        entity.inta = 100
        dao.update(entity) shouldBe 1
        entity = dao.selectById("cc25e3c045390139d47e1cb6d0c265af")!!
        entity.apply {
            assertSoftly {
                inta shouldBe 100
                createBy shouldBe "admin"
                modifyBy shouldBe "root"
                modifyById shouldBe session.accountId
                modifyDate.toLocalDate() shouldBe LocalDate.now()
                roleId shouldBe session.roleId
                roleName shouldBe session.roleName
                orgId shouldBe session.orgId
                orgName shouldBe session.orgName
            }
        }
    }

    @Test
    fun updateByMap() {
        var entity = dao.selectById("cc25e3c045390139d47e1cb6d0c265af")!!
        entity.apply {
            assertSoftly {
                createBy shouldBe "admin"
                inta shouldNotBe 100
                modifyBy shouldNotBe "root"
                modifyById shouldNotBe session.accountId
                modifyDate.toLocalDate() shouldNotBe LocalDate.now()
                roleId shouldNotBe session.roleId
                roleName shouldNotBe session.roleName
                orgId shouldNotBe session.orgId
                orgName shouldNotBe session.orgName
            }
        }
        dao.update("cc25e3c045390139d47e1cb6d0c265af", mapOf("inta" to 100))
        entity = dao.selectById("cc25e3c045390139d47e1cb6d0c265af")!!
        entity.apply {
            assertSoftly {
                inta shouldBe 100
                createBy shouldBe "admin"
                modifyBy shouldBe "root"
                modifyById shouldBe session.accountId
                modifyDate.toLocalDate() shouldBe LocalDate.now()
                roleId shouldBe session.roleId
                roleName shouldBe session.roleName
                orgId shouldBe session.orgId
                orgName shouldBe session.orgName
            }
        }
    }

    @Test
    fun insert() {
        val insert = dao.insert(entity)
        dao.selectById(insert) shouldNotBe null
    }

    @Test
    fun baseSelect() {
        val select = dao.select(listOf(table.id), null, 0, 0, null).toList()
        select.size shouldBe 2
        select.map { it[table.id] } shouldContain listOf("cc25e3c045390139d47e1cb6d0c265af","d2dbcc6045390139d47f1cb6d0c265af")
    }

    @Test
    fun testSelect() {
//        dao.select()
    }

    @Test
    fun singleRowSelect() {
    }

    @Test
    fun testSelect1() {
    }

    @Test
    fun pageSelect() {
    }

    @Test
    fun selectAll() {
    }

    @Test
    fun selectById() {
    }

    @Test
    fun selectByIds() {
    }

    @Test
    fun count() {
    }

    @Test
    fun history() {
    }

    @Test
    fun getTable() {
    }

    @Test
    fun getDatabaseService() {
    }
}