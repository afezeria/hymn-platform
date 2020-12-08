package github.afezeria.hymn.common.sql.util

import github.afezeria.hymn.common.adminConn
import github.afezeria.hymn.common.sql.BaseDbTest
import github.afezeria.hymn.common.util.execute
import io.kotest.assertions.assertSoftly
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
        adminConn.use {
            val result = it.execute("select hymn.throw_if_api_is_illegal('object_api','abc')")
            result.size shouldBe 1
            result[0]["throw_if_api_is_illegal"] shouldBe PGobject().apply {
                type = "void";value = ""
            }
        }
    }

    @Test
    fun throwExceptionWhenApiIsKeyword() {
        adminConn.use {
            val exception = shouldThrow<PSQLException> {
                it.execute("select hymn.throw_if_api_is_illegal('object_api','table')")
            }
            exception.sqlState shouldBe "P0001"
            exception.message shouldContain "无效的.*?是数据库关键字".toRegex()
        }
    }

    @Test
    fun throwExceptionWhenApiIsTooShort() {
        adminConn.use {
            val exception = shouldThrow<PSQLException> {
                it.execute("select hymn.throw_if_api_is_illegal('object_api','t')")
            }
            exception.sqlState shouldBe "P0001"
            exception.message shouldContain "invalid object_api, must match [a-zA-Z][a-zA-Z0-9_]{1,24}"
        }
    }

    @Test
    fun throwExceptionWhenApiIsTooLong() {
        adminConn.use {
            val exception = shouldThrow<PSQLException> {
                it.execute("select hymn.throw_if_api_is_illegal('object_api','ttttttttttttttttttttttttttttt')")
            }
            exception.sqlState shouldBe "P0001"
            exception.message shouldContain "invalid object_api, must match [a-zA-Z][a-zA-Z0-9_]{1,24}"
        }
    }

    @Test
    fun throwExceptionWhenApiStartWithUnderline() {
        adminConn.use {
            val exception = shouldThrow<PSQLException> {
                it.execute("select hymn.throw_if_api_is_illegal('object_api','_abc')")
            }
            exception.sqlState shouldBe "P0001"
            exception.message shouldContain "invalid object_api, must match [a-zA-Z][a-zA-Z0-9_]{1,24}"
        }
    }

    @Test
    fun throwExceptionWhenApiContainIllegalCharacter() {
        adminConn.use {
            assertSoftly {
                var exception = shouldThrow<PSQLException> {
                    it.execute("select hymn.throw_if_api_is_illegal('object_api','ab#')")
                }
                exception.sqlState shouldBe "P0001"
                exception.message shouldContain "invalid object_api, must match [a-zA-Z][a-zA-Z0-9_]{1,24}"

                exception = shouldThrow {
                    it.execute("select hymn.throw_if_api_is_illegal('object_api','?abc')")
                }
                exception.sqlState shouldBe "P0001"
                exception.message shouldContain "invalid object_api, must match [a-zA-Z][a-zA-Z0-9_]{1,24}"
            }
        }
    }

}