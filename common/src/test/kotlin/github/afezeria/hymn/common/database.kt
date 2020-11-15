package github.afezeria.hymn.common

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection

/**
 * @author afezeria
 */
val ds = HikariDataSource(HikariConfig("/database.properties"))
val conn: Connection
    get() = ds.connection
