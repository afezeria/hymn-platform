package github.afezeria.hymn.common.util

import github.afezeria.hymn.common.adminConn
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import io.kotest.matchers.types.beInstanceOf
import org.junit.jupiter.api.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
internal class JdbcExtensionTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            adminConn.use {
                it.createStatement().execute(
                    """
                    drop table if exists test_table;
                    create table test_table(
                    id uuid primary key default uuid_generate_v4(),
                    atext text,
                    aint int4,
                    adatetime timestamptz default now(),
                    adate date default now()
                    )
            """
                )
            }
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            adminConn.use {
                it.createStatement().execute(
                    """
                    drop table test_table
                """
                )
            }
        }
    }

    @BeforeEach
    fun beforeEach() {
        adminConn.use {
            it.createStatement().execute("delete from test_table")
        }
    }

    @Nested
    inner class ParseNamingSqlTest {
        @Test
        fun `one param`() {
            val sql = "select * from sys where id = #{id}"
            val map = mapOf("id" to 1)
            parseNamingSql(sql, map).apply {
                first shouldBe "select * from sys where id = ?"
                second shouldBe listOf(1)
            }
        }

        @Test
        fun `two param`() {
            val sql = "select * from sys where id = #{id} and name = #{name}"
            val map = mapOf("id" to 1, "name" to "admin")
            parseNamingSql(sql, map).apply {
                first shouldBe "select * from sys where id = ? and name = ?"
                second shouldBe listOf(1, "admin")
            }

        }

        @Test
        fun `ignore sharp after backslash`() {
            val sql = "select * from sys where id = #{id} and name = \\#{name}"
            val map = mapOf("id" to 1, "name" to "admin")
            parseNamingSql(sql, map).apply {
                first shouldBe "select * from sys where id = ? and name = #{name}"
                second shouldBe listOf(1)
            }
        }

        @Test
        fun `when two backslashes are in front of sharp`() {
            val sql = "select * from sys where id = #{id} and name = \\\\#{name}"
            val map = mapOf("id" to 1, "name" to "admin")
            parseNamingSql(sql, map).apply {
                first shouldBe "select * from sys where id = ? and name = \\?"
                second shouldBe listOf(1, "admin")
            }
        }

        @Test
        fun `when three backslashes are in front of sharp`() {
            val sql = "select * from sys where id = #{id} and name = \\\\\\#{name}"
            val map = mapOf("id" to 1, "name" to "admin")
            parseNamingSql(sql, map).apply {
                first shouldBe "select * from sys where id = ? and name = \\#{name}"
                second shouldBe listOf(1)
            }
        }
    }

    @Test
    fun `execute with params`() {
        val ins = adminConn.execute(
            "insert into test_table (atext,aint) values (?,?) returning id,atext,aint,adate;",
            "abc",
            123
        )
        ins.size shouldBe 1
        ins[0].apply {
            size shouldBe 4
            get("id") should beInstanceOf<UUID>()
            get("atext") shouldBe "abc"
            get("aint") shouldBe 123
        }
    }

    @Test
    fun `execute with list`() {
        val ins = adminConn.execute(
            "insert into test_table (atext,aint) values (?,?) returning id,atext,aint,adate;",
            listOf("abc", 123)
        )
        ins.size shouldBe 1
        ins[0].apply {
            size shouldBe 4
            get("id") should beInstanceOf<UUID>()
            get("atext") shouldBe "abc"
            get("aint") shouldBe 123
        }
    }


    @Nested
    inner class ExecuteWithMapTest {
        private val sql =
            "insert into test_table (atext,aint) values (#{atext},#{aint}) returning id,atext,aint,adate;"

        @Test
        fun `execute`() {
            val ins = adminConn.execute(sql, mapOf("atext" to "abc", "aint" to 123))
            ins.size shouldBe 1
            ins[0].apply {
                size shouldBe 4
                get("id") should beInstanceOf<UUID>()
                get("atext") shouldBe "abc"
                get("aint") shouldBe 123
            }
        }

        @Test
        fun `ignore redundant parameters`() {
            val ins = adminConn.execute(sql, mapOf("atext" to "abc", "aint" to 123, "bb" to "bb"))
            ins.size shouldBe 1
            ins[0].apply {
                size shouldBe 4
                get("id") should beInstanceOf<UUID>()
                get("atext") shouldBe "abc"
                get("aint") shouldBe 123
            }
        }

        @Test
        fun `throw exception when missing parameter`() {
            val e = shouldThrow<RuntimeException> {
                adminConn.execute(sql, mapOf("atext" to "abc"))
            }
            e.message shouldStartWith "missing parameter in sql"
        }

    }

    @Test
    fun `sql time convert to local time 1`() {
        val ins = adminConn.execute(
            "insert into test_table (atext,aint) values (?,?) returning id,adate,adatetime;",
            "abc",
            123
        )
        ins.size shouldBe 1
        ins[0].apply {
            size shouldBe 3
            get("adate") should beInstanceOf<LocalDate>()
            get("adatetime") should beInstanceOf<LocalDateTime>()
        }
    }


    @Test
    fun `sql time convert to local time 2`() {
        val dateTime = LocalDateTime.now()
        val date = LocalDate.now()
        val ins = adminConn.execute(
            "insert into test_table (adatetime,adate) values (?,?) returning adate,adatetime;",
            dateTime,
            date
        )
        ins.size shouldBe 1
        ins[0].apply {
            size shouldBe 2
            get("adate") shouldBe date
            get("adatetime") shouldBe dateTime
        }
    }
}