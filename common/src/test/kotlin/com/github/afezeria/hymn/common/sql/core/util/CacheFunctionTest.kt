package com.github.afezeria.hymn.common.sql.core.util

import com.github.afezeria.hymn.common.BaseDbTest
import com.github.afezeria.hymn.common.adminConn
import com.github.afezeria.hymn.common.util.execute
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

/**
 * @author afezeria
 */
class CacheFunctionTest : BaseDbTest() {
    @AfterEach
    fun afterEach() {
        adminConn.use {
            it.execute("delete from hymn.core_cache where true")
        }
    }

    @Test
    fun setCache() {
        adminConn.use {
            val group = "abc"
            val key = "abc"
            val value = "bb"
            val expiry = 100
            val execute =
                it.execute("select hymn.set_cache(?,?,?,?) as new_cache", group, key, value, expiry)
            execute.size shouldBe 1
            execute[0]["new_cache"] shouldBe true
//            it.execute("select count(*) from hymn.core_cache").size shouldBe 1
        }
    }

    @Test
    fun coverExpiredCache() {
        adminConn.use {
            val group = "abc"
            val key = "abc"
            var value = "bb"
            val expiry = 1
            it.execute("select hymn.set_cache(?,?,?,?)", group, key, value, expiry)
            val res1 = it.execute("select * from hymn.core_cache")[0]
            value = "cc"
            Thread.sleep(1000)
            val execute =
                it.execute("select hymn.set_cache(?,?,?,?) as new_cache", group, key, value, expiry)
            execute[0]["new_cache"] shouldBe true
            val res2 = it.execute("select * from hymn.core_cache")[0]
            (res2["last_time"] as LocalDateTime).isAfter((res1["last_time"] as LocalDateTime)) shouldBe true
            res2["c_value"] shouldBe "cc"
        }
    }

    @Test
    fun coverCache() {
        adminConn.use {
            val group = "abc"
            val key = "abc"
            var value = "bb"
            val expiry = 100
            it.execute("select hymn.set_cache(?,?,?,?)", group, key, value, expiry)
            val res1 = it.execute("select * from hymn.core_cache")[0]
            value = "cc"
            val execute =
                it.execute("select hymn.set_cache(?,?,?,?) as new_cache", group, key, value, expiry)
            execute[0]["new_cache"] shouldBe false
            val res2 = it.execute("select * from hymn.core_cache")[0]
            (res2["last_time"] as LocalDateTime).isAfter((res1["last_time"] as LocalDateTime)) shouldBe true
            res2["c_value"] shouldBe "cc"
        }
    }

    @Test
    fun deleteExpiredCache() {
        adminConn.use {
            val group = "abc"
            val key = "abc"
            val value = "bb"
            val expiry = 1
            it.execute("select hymn.set_cache(?,?,?,?)", group, key, value, expiry)
            Thread.sleep(1000)
            it.execute("select hymn.get_cache(?,?)", group, key).size shouldBe 0
            it.execute("select * from hymn.core_cache").size shouldBe 0
        }
    }

    @Test
    fun fuzzyMatching() {
        adminConn.use {
            val group = "abc"
            val key = "abc"
            val value = "bb"
            val expiry = 10
            it.execute("select hymn.set_cache(?,?,?,?)", group, key, value, expiry)
            it.execute("select hymn.get_cache(?,?)", group, "ab%").size shouldBe 1
        }
    }

    @Test
    fun deleteCache() {
        adminConn.use {
            val group = "abc"
            val key = "abc"
            val value = "bb"
            val expiry = 10
            it.execute("select hymn.set_cache(?,?,?,?)", group, key, value, expiry)
            it.execute("select hymn.remove_cache(?,?)", group, key).size shouldBe 1
            it.execute("select * from hymn.core_cache").size shouldBe 0
        }
    }
}