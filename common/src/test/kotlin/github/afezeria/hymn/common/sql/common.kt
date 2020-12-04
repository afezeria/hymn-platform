package github.afezeria.hymn.common.sql

import github.afezeria.hymn.common.conn
import github.afezeria.hymn.common.util.execute
import java.time.LocalDateTime

/**
 * @author afezeria
 */
const val DEFAULT_ACCOUNT_NAME = "system admin"
const val DEFAULT_ACCOUNT_ID = "911c60ea5d62420794d86eeecfddce7c"
const val DEFAULT_ORG_ID = "b18245e9d690461190172b6cb90c46ac"
const val DEFAULT_ROLE_ID = "301c35c23be449abb5bdf6c80b6878af"

val BASE_ARRAY = arrayOf<Any>(
    DEFAULT_ACCOUNT_ID,
    DEFAULT_ACCOUNT_NAME,
    DEFAULT_ACCOUNT_ID,
    DEFAULT_ACCOUNT_NAME,
    LocalDateTime.now(),
    LocalDateTime.now()
)
val COMMON_INFO =
    arrayOf(DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME)

fun deleteBObject(id: String) {
    conn.use {
        it.execute("update hymn.core_b_object set active=false where id=?", id)
        it.execute("delete from hymn.core_b_object where id=?", id)
    }
}