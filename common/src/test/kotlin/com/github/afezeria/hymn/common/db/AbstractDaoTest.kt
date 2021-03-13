package com.github.afezeria.hymn.common.db

import com.github.afezeria.hymn.common.adminConn
import com.github.afezeria.hymn.common.constant.AccountType
import com.github.afezeria.hymn.common.constant.ClientType
import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.Session
import com.github.afezeria.hymn.common.util.execute
import com.github.afezeria.hymn.common.util.randomUUIDStr
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.*
import org.ktorm.dsl.and
import org.ktorm.dsl.asc
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * @author afezeria
 */
internal class AbstractDaoTest {
    companion object {
        val table = TestTables()
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
            SimpleOrmTestHelper.init()
            Session.current.set(session)
        }

        @JvmStatic
        @AfterAll
        fun after() {
            SimpleOrmTestHelper.clear()
            Session.current.remove()
        }
    }

    val ea = TestTable(
        inta = 2,
        longa = 42424204040,
        doublea = 3.2,
        decimala = BigDecimal("42.42420913"),
        boola = true
    ).apply {
        id = "cc25e3c045390139d47e1cb6d0c265af"
        lazya = "abc"
        roleId = "cc25e3c045390139d47e1cb6d0c265af"
        roleName = "admin"
        orgId = "cc25e3c045390139d47e1cb6d0c265af"
        orgName = "admin"
        createById = "cc25e3c045390139d47e1cb6d0c265af"
        createBy = "admin"
        modifyById = "cc25e3c045390139d47e1cb6d0c265af"
        modifyBy = "admin"
        createDate = LocalDateTime.of(2020, 1, 1, 0, 0, 0)
        modifyDate = LocalDateTime.of(2020, 1, 1, 0, 0, 0)
    }
    val eb = TestTable(
        inta = 3,
        longa = null,
        doublea = 3.2,
        decimala = BigDecimal("42.42420913"),
        boola = true
    ).apply {
        id = "d2dbcc6045390139d47f1cb6d0c265af"
        lazya = "abc"
        roleId = "cc25e3c045390139d47e1cb6d0c265af"
        roleName = "admin"
        orgId = "cc25e3c045390139d47e1cb6d0c265af"
        orgName = "admin"
        createById = "cc25e3c045390139d47e1cb6d0c265af"
        createBy = "admin"
        modifyById = "cc25e3c045390139d47e1cb6d0c265af"
        modifyBy = "admin"
        createDate = LocalDateTime.of(2020, 1, 1, 0, 0, 0)
        modifyDate = LocalDateTime.of(2021, 1, 1, 0, 0, 0)
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
                           ('d2dbcc6045390139d47f1cb6d0c265af', 3, null, 3.2, 42.42420913, true, 'abc',
                            'cc25e3c045390139d47e1cb6d0c265af', 'admin', 'cc25e3c045390139d47e1cb6d0c265af', 'admin',
                            'cc25e3c045390139d47e1cb6d0c265af', 'admin', 'cc25e3c045390139d47e1cb6d0c265af', 'admin', '2020-01-02 00:00:00',
                            '2021-01-01 00:00:00')
            """
            )
        }

    }

    @AfterEach
    fun afterEach() {
        adminConn.use {
            it.execute("delete from test_dao.test_table")
            it.execute("delete from test_dao.test_table_history")
        }
    }

    @Test
    fun delete() {
        val res = dao.delete { it.inta eq 2 }
        res shouldBe 1
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
    fun `map update will ignore unmutable field`() {
        val id = "cc25e3c045390139d47e1cb6d0c265af"
        val entity = dao.selectById(id)!!
        val boola = entity.boola
        dao.update(id, mapOf("boola" to entity.boola.not()))
        val new = dao.selectById(id)!!
        new.boola shouldBe boola
    }

    @Test
    fun `entity update will ignore unmutable field`() {
        val entity = dao.selectById("cc25e3c045390139d47e1cb6d0c265af")!!
        val boola = entity.boola
        table.setValueByFieldName(entity, "boola", entity.boola.not())
        dao.update(entity) shouldBe 1
        val new = dao.selectById("cc25e3c045390139d47e1cb6d0c265af")!!
        new.boola shouldBe boola
    }

    @Test
    fun insert() {
        val copy = ea.copy(inta = 52)
        val insert = dao.insert(copy)
        copy.apply {
            assertSoftly {
                id shouldBe insert
                createBy shouldBe "root"
                modifyBy shouldBe "root"
                createById shouldBe session.accountId
                modifyById shouldBe session.accountId
                createDate.toLocalDate() shouldBe LocalDate.now()
                modifyDate.toLocalDate() shouldBe LocalDate.now()
                roleId shouldBe session.roleId
                roleName shouldBe session.roleName
                orgId shouldBe session.orgId
                orgName shouldBe session.orgName

            }
        }
    }


    @Test
    fun bulkInsert() {
        val list = listOf(
            TestTable(
                inta = 4,
                longa = null,
                doublea = 0.0,
                decimala = BigDecimal("2"),
                boola = false
            ),
            TestTable(
                inta = 5,
                longa = null,
                doublea = 0.0,
                decimala = BigDecimal("2"),
                boola = false
            ),
        )
        dao.bulkInsert(list) shouldBe 2
        dao.count() shouldBe 4
    }

    @Test
    fun `insertOrUpdate conflict on id`() {
        ea.inta = 50
        dao.insertOrUpdate(ea) shouldBe 1
        dao.count() shouldBe 2
        val new = dao.selectById(ea.id)!!
        new.inta = 50
    }

    @Test
    fun `insertOrUpdate conflict on specify column`() {
        val new = TestTable(
            inta = 2,
            longa = null,
            doublea = 0.0,
            decimala = BigDecimal("22"),
            boola = false
        )
        new shouldNotBe ea
        dao.insertOrUpdate(new, table.inta) shouldBe 1
        val nea = dao.selectById(ea.id)!!
        new shouldBe nea
        nea.lazya shouldBe ea.lazya
    }

    @Test
    fun bulkInsertOrUpdate() {
        val list = listOf(
            TestTable(
                inta = 2,
                longa = null,
                doublea = 0.0,
                decimala = BigDecimal("2"),
                boola = false
            ),
            TestTable(
                inta = 3,
                longa = null,
                doublea = 0.0,
                decimala = BigDecimal("2"),
                boola = false
            ),
        )
        ea shouldNotBe list[0]
        eb shouldNotBe list[1]
        dao.bulkInsertOrUpdate(list, table.inta)
        val nea = dao.selectById(ea.id)!!
        val neb = dao.selectById(eb.id)!!
        nea shouldBe list[0]
        neb shouldBe list[1]
    }

    @Test
    fun `specify column query`() {
        val select = dao.select(listOf(table.id), null, 0, 0).toList()
        select.size shouldBe 2
        select.map { it["id"] } shouldContainAll listOf(
            "cc25e3c045390139d47e1cb6d0c265af",
            "d2dbcc6045390139d47f1cb6d0c265af"
        )
    }

    @Test
    fun `query default order test`() {
        val ids = listOf("cc25e3c045390139d47e1cb6d0c265af", "d2dbcc6045390139d47f1cb6d0c265af")
    }

    @Test
    fun `query order test`() {
        val ids = listOf("cc25e3c045390139d47e1cb6d0c265af", "d2dbcc6045390139d47f1cb6d0c265af")
        assertSoftly {
            var res = dao.select(null, 0, 2, listOf(table.id.asc()))
            res.map { it.id } shouldContainExactly ids
//            default order
            res = dao.select(null, 0, 2)
            res.map { it.id } shouldContainExactly ids.reversed()

            res = dao.select(null, 0, 2, listOf(table.id.desc()))
            res.map { it.id } shouldContainExactly ids.reversed()

            res = dao.select(null, 0, 2, listOf(table.roleName.asc(), table.id.desc()))
            res.map { it.id } shouldContainExactly ids.reversed()

            res = dao.select(null, 0, 2, listOf(table.modifyDate.asc(), table.id.desc()))
            res.map { it.id } shouldContainExactly ids
        }
    }

    @Test
    fun `entity query`() {
        val select = dao.select({ it.id eq "cc25e3c045390139d47e1cb6d0c265af" })
        select.size shouldBe 1
        select[0] shouldBe ea
    }

    @Test
    fun `limit and offset test`() {
        var res = dao.select(null, 0, 1, listOf(table.id.asc()))
        res.size shouldBe 1
        res[0] shouldBe ea
        res = dao.select(null, 1, 1, listOf(table.id.asc()))
        res.size shouldBe 1
        res[0] shouldBe eb
    }

    @Test
    fun `multiple conditions query test`() {
        var select =
            dao.select({ (table.roleName eq "admin") and (table.modifyDate eq eb.modifyDate) })
        select.size shouldBe 1
        select[0] shouldBe eb
        select =
            dao.select(listOf(table.roleName eq "admin", table.modifyDate eq eb.modifyDate))
        select.size shouldBe 1
        select[0] shouldBe eb
    }

    @Test
    fun singleRowSelectTest() {
        var select = dao.singleRowSelect({ it.modifyDate eq eb.modifyDate })
        select shouldBe eb
        select = dao.singleRowSelect({ it.id eq "abc" })
        select shouldBe null
    }

    @Test
    fun pageSelect() {
        var res = dao.pageSelect(null, 1, 1, listOf(table.id.asc()))
        res.size shouldBe 1
        res[0] shouldBe ea
        res = dao.pageSelect(null, 1, 2, listOf(table.id.asc()))
        res.size shouldBe 1
        res[0] shouldBe eb

        res = dao.pageSelect({ it.boola eq true }, 10, 1, listOf(table.id.asc()))
        res.size shouldBe 2
        res[0] shouldBe ea

        shouldThrow<IllegalArgumentException> {
            dao.pageSelect(null, 0, 2, listOf(table.id.asc()))
        }.apply {
            message shouldBe "pageSize must be greater than 0, current value 0"
        }
        shouldThrow<IllegalArgumentException> {
            dao.pageSelect(null, 1, 0, listOf(table.id.asc()))
        }.apply {
            message shouldBe "pageNum must be greater than 0, current value 0"
        }
    }

    @Test
    fun `count test`() {
        val count = dao.count()
        count shouldBe 2
        dao.count(table.longa) shouldBe 1
        dao.count(table.longa, { it.id eq eb.id }) shouldBe 0
    }

    @Test
    fun `select all`() {
        dao.selectAll() shouldContainAll listOf(ea, eb)
    }

    @Test
    fun `select by id`() {
        dao.selectById(ea.id) shouldBe ea
        dao.selectById("abc") shouldBe null
    }

    @Test
    fun `select by ids`() {
        dao.selectByIds(listOf(ea.id, eb.id)) shouldContainAll listOf(ea, eb)
    }

    @Test
    fun history() {
        val copy = ea.copy(inta = 72)
        val insert = dao.insert(copy)
        copy.inta = 5
        dao.update(copy)
        dao.deleteById(insert)
        val history = dao.history(insert)
        history.size shouldBe 3
        history[0].keys.size shouldBe 19
        history.map { it["operation"] } shouldContainExactlyInAnyOrder listOf("u", "i", "d")
    }

    @Test
    fun dataExist() {
        val id = "abcd"
        shouldThrow<DataNotFoundException> {
            dao.selectByIdThrowIfNotExist(id)
        }.apply {
            message shouldBe "TestTable [id:$id] 不存在"
        }
        shouldThrow<DataNotFoundException> {
            dao.exist(id, true)
        }.apply {
            message shouldBe "TestTable [id:$id] 不存在"
        }
    }
}