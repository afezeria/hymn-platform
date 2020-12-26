package github.afezeria.hymn.common

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection
import java.time.LocalDateTime

/**
 * @author afezeria
 */
val ds1 = HikariDataSource(HikariConfig("/admin-database.properties"))
val adminConn: Connection
    get() = ds1.connection

val ds2 = HikariDataSource(HikariConfig("/user-database.properties"))
val userConn: Connection
    get() = ds2.connection

fun main() {
//    val prepareStatement = adminConn.prepareStatement(
//        """
//            select 'a'
//        """
//    )
    val prepareStatement = adminConn.prepareStatement(
        """
            insert into a (a, b, c)
            values (?,?,?),(?,?,?)on conflict (a,b) do update set c=excluded.c ,t=?;
        """
    )
    prepareStatement.setObject(1,1)
    prepareStatement.setObject(2,3)
    prepareStatement.setObject(3,9)
    prepareStatement.setObject(4,1)
    prepareStatement.setObject(5,2)
    prepareStatement.setObject(6,8)
    prepareStatement.setObject(7,LocalDateTime.now())
    println(prepareStatement.execute())
    println(prepareStatement.updateCount)
}