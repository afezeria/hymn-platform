package github.afezeria.hymn.common.sql.core.util

import github.afezeria.hymn.common.*
import github.afezeria.hymn.common.util.execute
import github.afezeria.hymn.common.util.randomUUIDStr
import github.afezeria.hymn.common.util.toJson
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

/**
 * @author afezeria
 */
class QueryDataNameTest : BaseDbTest() {
    @Test
    fun abc() {
        val tableCount = 20
        val dataCount = 50
        val objects = mutableListOf<String>()
        try {
            adminConn.use {
                val map = mutableMapOf<String, MutableSet<String>>()
                (1..tableCount).forEach { j ->
                    val o = createBObject()
                    val set = mutableSetOf<String>()
                    map[o["id"] as String] = set
                    objects.add(o["id"] as String)
                    (1..dataCount).forEach { i ->
                        val data = it.execute(
                            """
                            insert into hymn_view.${o["api"]} (create_date,modify_date,owner_id,create_by_id,
                                modify_by_id,type_id) 
                            values (now(), now(), ?, ?, ?, ?) returning *;
                            """,
                            DEFAULT_ACCOUNT_ID,
                            DEFAULT_ACCOUNT_ID,
                            DEFAULT_ACCOUNT_ID,
                            randomUUIDStr()
                        )[0]
                        set.add(data["id"] as String)
                    }
                }
                val execute = it.execute(
                    "select * from hymn.query_data_name(cast(? as json)) t(object_id text,data_id text,data_name text)",
                    map.toJson()
                )
                execute.size shouldBe tableCount * dataCount
            }
        } finally {
            objects.forEach {
                deleteBObject(it)
            }
        }
    }
}