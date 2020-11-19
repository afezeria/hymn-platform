package github.afezeria.hymn.common.sql

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.ktorm.database.Database
import java.io.InputStreamReader
import java.sql.Connection
import java.util.*
import javax.sql.DataSource


/**
 * @author afezeria
 */
open class BaseDbTest {

    companion object {
        private val prop = Properties()

        init {
            println("========== db init start ==========")
            prop.load(
                this::class.java.classLoader.getResourceAsStream("database.properties")
            )
            val path = System.getProperty("user.dir")
            val scripts = listOf(
                "table.sql",
                "init-data.sql",
                "history-table-and-trigger.sql",
                "function.sql"
            )
            for (script in scripts) {
                runSqlScript("$path/src/test/resources/sql/$script")
            }
            println("========== db init end ==========")
        }

        private fun runSqlScript(path: String) {
            val proc = prop.run {
                println("run script $path")
                val command = "psql \"postgresql://${get("dataSource.user")}:${get("dataSource.password")}@${get("dataSource.serverName")}:${get("dataSource.portNumber")}/${get("dataSource.databaseName")}\" -f $path -q"
                Runtime.getRuntime().exec(arrayOf("bash", "-c", command))
            }
//            InputStreamReader(proc.inputStream).readLines().forEach(::println)

            //检查数据库是否报错
            val regex = "(psql:(.+:\\d+:)* 错误)".toRegex()
            InputStreamReader(proc.errorStream).readLines().takeIf {
                it.find { regex.find(it) != null }.isNullOrBlank().not()
            }?.apply {
                println("========== error ==========")
                forEach(::println)
                throw RuntimeException()
            }
        }
    }
}