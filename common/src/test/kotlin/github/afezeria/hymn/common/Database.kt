package github.afezeria.hymn.common

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection

/**
 * @author afezeria
 */
val ds1 = HikariDataSource(HikariConfig("/admin-database.properties"))
val adminConn: Connection
    get() = ds1.connection

val ds2 = HikariDataSource(HikariConfig("/user-database.properties"))
val userConn: Connection
    get() = ds2.connection
