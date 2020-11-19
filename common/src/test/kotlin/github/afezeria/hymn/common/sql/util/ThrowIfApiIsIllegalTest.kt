package github.afezeria.hymn.common.sql.util

import github.afezeria.hymn.common.conn
import github.afezeria.hymn.common.sql.BaseDbTest
import github.afezeria.hymn.common.util.execute
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.postgresql.util.PGobject
import org.postgresql.util.PSQLException

/**
 * @author afezeria
 */
class ThrowIfApiIsIllegalTest : BaseDbTest() {
    @Test
    fun legalApi() {
        conn.use {
            val result = it.execute("select hymn.throw_if_api_is_illegal('object_api','abc')")
            result.size shouldBe 1
            result[0]["throw_if_api_is_illegal"] shouldBe PGobject().apply { type = "void";value = "" }
        }
    }

    @Test
    fun throwExceptionWhenApiIsKeyword() {
        conn.use {
            val exception = shouldThrow<PSQLException> {
                it.execute("select hymn.throw_if_api_is_illegal('object_api','table')")
            }
            exception.sqlState shouldBe "P0001"
            exception.message shouldContain "invalid object_api, table is sql keyword"
        }
    }

    @Test
    fun throwExceptionWhenApiIsTooShort() {
        conn.use {
            val exception = shouldThrow<PSQLException> {
                it.execute("select hymn.throw_if_api_is_illegal('object_api','t')")
            }
            exception.sqlState shouldBe "P0001"
            exception.message shouldContain "invalid object_api, must match [a-zA-Z][a-zA-Z0-9_]{1,40}"
        }
    }

    @Test
    fun throwExceptionWhenApiIsTooLong() {
        conn.use {
            val exception = shouldThrow<PSQLException> {
                it.execute("select hymn.throw_if_api_is_illegal('object_api','ttttttttttttttttttttttttttttttttttttttttttt')")
            }
            exception.sqlState shouldBe "P0001"
            exception.message shouldContain "invalid object_api, must match [a-zA-Z][a-zA-Z0-9_]{1,40}"
        }
    }

    @Test
    fun throwExceptionWhenApiStartWithUnderline() {
        conn.use {
            val exception = shouldThrow<PSQLException> {
                it.execute("select hymn.throw_if_api_is_illegal('object_api','_abc')")
            }
            exception.sqlState shouldBe "P0001"
            exception.message shouldContain "invalid object_api, must match [a-zA-Z][a-zA-Z0-9_]{1,40}"
        }
    }

    @Test
    fun throwExceptionWhenApiContainIllegalCharacter() {
        conn.use {
            var exception = shouldThrow<PSQLException> {
                it.execute("select hymn.throw_if_api_is_illegal('object_api','ab#')")
            }
            exception.sqlState shouldBe "P0001"
            exception.message shouldContain "invalid object_api, must match [a-zA-Z][a-zA-Z0-9_]{1,40}"

            exception = shouldThrow {
                it.execute("select hymn.throw_if_api_is_illegal('object_api','?abc')")
            }
            exception.sqlState shouldBe "P0001"
            exception.message shouldContain "invalid object_api, must match [a-zA-Z][a-zA-Z0-9_]{1,40}"
        }
    }

}