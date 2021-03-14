package com.github.afezeria.hymn.common

import com.github.afezeria.hymn.common.util.execute
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection

/**
 * @author afezeria
 */
private var jdbcUrl: String
val ds1 = HikariDataSource(
    HikariConfig("/admin-database.properties").also { jdbcUrl = it.jdbcUrl }
).apply {
    this.connection.use {
        it.execute(
            """
do
${'$'}${'$'}
    begin
        perform * from pg_roles where rolname = 'hymn_user';
        if not FOUND then
            create role hymn_user;
            raise notice 'create role hymn_user';
        end if;
        perform * from pg_roles where rolname = 'test_user';
        if not FOUND then
            create role test_user login password '123456';
            grant hymn_user to test_user;
            raise notice 'create role test_user';
        end if;
    end;
${'$'}${'$'}
        """
        )
    }
}
val adminConn: Connection
    get() = ds1.connection

val ds2 = HikariDataSource(HikariConfig().also {
    it.jdbcUrl = jdbcUrl
    it.schema = "hymn_view"
    it.password = "123456"
    it.username = "test_user"
})
val userConn: Connection
    get() = ds2.connection