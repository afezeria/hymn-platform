package com.github.afezeria.hymn.common.db

import com.github.afezeria.hymn.common.*
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
import io.kotest.matchers.types.instanceOf
import mu.KotlinLogging
import org.junit.jupiter.api.*
import org.ktorm.dsl.and
import org.ktorm.dsl.asc
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {}

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
        roleId = DEFAULT_ROLE_ID
        roleName = DEFAULT_ROLE_NAME
        orgId = DEFAULT_ORG_ID
        orgName = DEFAULT_ROLE_NAME
        createById = DEFAULT_ACCOUNT_ID
        createBy = DEFAULT_ACCOUNT_NAME
        modifyById = DEFAULT_ACCOUNT_ID
        modifyBy = DEFAULT_ACCOUNT_NAME
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
        roleId = DEFAULT_ROLE_ID
        roleName = DEFAULT_ROLE_NAME
        orgId = DEFAULT_ORG_ID
        orgName = DEFAULT_ROLE_NAME
        createById = DEFAULT_ACCOUNT_ID
        createBy = DEFAULT_ACCOUNT_NAME
        modifyById = DEFAULT_ACCOUNT_ID
        modifyBy = DEFAULT_ACCOUNT_NAME
        createDate = LocalDateTime.of(2020, 1, 1, 0, 0, 0)
        modifyDate = LocalDateTime.of(2021, 1, 1, 0, 0, 0)
    }

    @BeforeEach
    fun beforeEach() {
        adminConn.use {
            it.execute(
                """
                    insert into test_dao.test_table (
                            id, inta, longa, doublea, decimala, boola, lazya, 
                            create_by_id, create_by, modify_by_id, modify_by, role_id, role_name, org_id, org_name, 
                            create_date, modify_date)
                    values (?, 2, 42424204040, 3.2, 42.42420913, true, 'abc', 
                            ?, ?, ?, ?, ?, ?, ?, ?, 
                            '2020-01-01 00:00:00', '2020-01-01 00:00:00'), 
                           (?, 3, null, 3.2, 42.42420913, true, 'abc', 
                            ?, ?, ?, ?, ?, ?, ?, ?, 
                            '2020-01-02 00:00:00', '2021-01-01 00:00:00')
            """,
                ea.id,
                DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME, DEFAULT_ROLE_ID,
                DEFAULT_ROLE_NAME, DEFAULT_ORG_ID, DEFAULT_ORG_NAME,
                eb.id,
                DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME, DEFAULT_ROLE_ID,
                DEFAULT_ROLE_NAME, DEFAULT_ORG_ID, DEFAULT_ORG_NAME,
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
        dao.deleteById(ea.id) shouldBe 1
    }


    @Test
    fun updateEntity() {
        var entity = dao.selectById(ea.id)!!
        entity.apply {
            inta shouldNotBe 100
            createBy shouldBe DEFAULT_ACCOUNT_NAME
            modifyBy shouldNotBe "root"
            modifyById shouldNotBe session.accountId
            modifyDate.toLocalDate() shouldNotBe LocalDate.now()
            roleId shouldNotBe session.roleId
            roleName shouldNotBe session.roleName
            orgId shouldNotBe session.orgId
            orgName shouldNotBe session.orgName
        }
        entity.inta = 100
        dao.update(entity) shouldBe 1
        entity = dao.selectById(ea.id)!!
        entity.apply {
            inta shouldBe 100
            createBy shouldBe DEFAULT_ACCOUNT_NAME
            modifyBy shouldBe "root"
            modifyById shouldBe session.accountId
            modifyDate.toLocalDate() shouldBe LocalDate.now()
            roleId shouldBe session.roleId
            roleName shouldBe session.roleName
            orgId shouldBe session.orgId
            orgName shouldBe session.orgName
        }
    }

    @Test
    fun updateByMap() {
        var entity = dao.selectById(ea.id)!!
        entity.apply {
            assertSoftly {
                inta shouldNotBe 100
                modifyBy shouldNotBe session.accountName
                modifyById shouldNotBe session.accountId
                modifyDate.toLocalDate() shouldNotBe LocalDate.now()
                roleId shouldNotBe session.roleId
                roleName shouldNotBe session.roleName
                orgId shouldNotBe session.orgId
                orgName shouldNotBe session.orgName
            }
        }
        dao.update(ea.id, mapOf("inta" to 100))
        entity = dao.selectById(ea.id)!!
        entity.apply {
            assertSoftly {
                inta shouldBe 100
                createBy shouldBe DEFAULT_ACCOUNT_NAME
                modifyBy shouldBe session.accountName
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
    fun `map update will ignore immutable field`() {
        val id = ea.id
        val entity = dao.selectById(id)!!
        val boola = entity.boola
        dao.update(id, mapOf("boola" to entity.boola.not()))
        val new = dao.selectById(id)!!
        new.boola shouldBe boola
    }

    @Test
    fun `entity update will ignore unmutable field`() {
        val entity = dao.selectById(ea.id)!!
        val boola = entity.boola
        table.setValueByFieldName(entity, "boola", entity.boola.not())
        dao.update(entity) shouldBe 1
        val new = dao.selectById(ea.id)!!
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
        val select = dao.selectJson(listOf(table.id)).toList()
        select.size shouldBe 2
        select.map { it["id"] } shouldContainAll listOf(
            ea.id, eb.id
        )
    }

    @Test
    fun `query order test`() {
        val ids = listOf(ea.id, eb.id)
        assertSoftly {
            var res = dao.select(null, listOf(table.id.asc()))
            res.map { it.id } shouldContainExactly ids
//            default order
            res = dao.select()
            res.map { it.id } shouldContainExactly ids.reversed()

            res = dao.select(null, listOf(table.id.desc()))
            res.map { it.id } shouldContainExactly ids.reversed()

            res = dao.select(null, listOf(table.roleName.asc(), table.id.desc()))
            res.map { it.id } shouldContainExactly ids.reversed()

            res = dao.select(null, listOf(table.modifyDate.asc(), table.id.desc()))
            res.map { it.id } shouldContainExactly ids
        }
    }

    @Test
    fun `entity query`() {
        val select = dao.select({ it.id eq ea.id })
        select.size shouldBe 1
        select[0] shouldBe ea
    }

    @Test
    fun `multiple conditions query test`() {
        var select =
            dao.select({ (table.roleName eq DEFAULT_ROLE_NAME) and (table.modifyDate eq eb.modifyDate) })
        select.size shouldBe 1
        select[0] shouldBe eb
        select = dao.select({ (table.roleName eq DEFAULT_ROLE_NAME) and (table.modifyDate eq eb.modifyDate) })
        select.size shouldBe 1
        select[0] shouldBe eb
    }

    @Test
    fun pageSelect() {

        var pageRes = PageUtil.pagination(1, 1) {
            dao.select(null, listOf(table.id.asc()))
        }
        pageRes shouldBe instanceOf(Page::class)
        pageRes.size shouldBe 1
        pageRes[0] shouldBe ea
        pageRes = PageUtil.pagination(2, 1) {
            dao.select(null, listOf(table.id.asc()))
        }
        pageRes.size shouldBe 1
        pageRes[0] shouldBe eb

        pageRes = PageUtil.pagination(1, 10) {
            dao.select({ it.boola eq true }, listOf(table.id.asc()))
        }
        pageRes.size shouldBe 2
        pageRes[0] shouldBe ea

        val listRes = PageUtil.limit(0, 1) {
            dao.select()
        }
        listRes shouldBe instanceOf(List::class)
        listRes.size shouldBe 1
        listRes[0] shouldBe eb

        shouldThrow<IllegalArgumentException> {
            PageUtil.pagination(0, 0) {
                dao.select(null, listOf(table.id.asc()))
            }
        }.apply {
            message shouldBe "pageNum must be greater than 0, current value 0"
        }
        shouldThrow<IllegalArgumentException> {
            PageUtil.pagination(1, 0) {
                dao.select(null, listOf(table.id.asc()))
            }
        }.apply {
            message shouldBe "pageSize must be greater than 0, current value 0"
        }
        shouldThrow<IllegalArgumentException> {
            PageUtil.limit(-1, 0) {
                dao.select(null, listOf(table.id.asc()))
            }
        }.apply {
            message shouldBe "offset must be greater than or equal to 0, current value -1"
        }
        shouldThrow<IllegalArgumentException> {
            PageUtil.limit(1, 0) {
                dao.select(null, listOf(table.id.asc()))
            }
        }.apply {
            message shouldBe "limit must be greater than 0, current value 0"
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
        dao.select() shouldContainAll listOf(ea, eb)
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