package github.afezeia.hymn.common.sql

import java.io.InputStreamReader
import java.util.*


/**
 * @author afezeria
 */
open class SqlTest {
    companion object {
        private val _prop = Properties()

        init {
            println("================ db init start ==================")
            _prop.load(
                this::class.java.classLoader.getResourceAsStream("database.properties")
            )
            _prop.forEach { t, u -> println("$t $u") }
            val path = System.getProperty("user.dir")
            val scripts = listOf(
                "table.sql",
//                "history-table-and-trigger.sql",
//                "init-data.sql",
//                "function.sql"
            )
            for (script in scripts) {
                runSqlScript("$path/src/test/resources/sql/$script")
            }

            println("================ db init end ==================")
        }

        private fun runSqlScript(path: String) {
            val proc = _prop.run {
                Runtime.getRuntime().exec(arrayOf("bash", "-c",
                    "psql \"postgresql://${get("username")}:${get("password")}@${get("host")}:${get("port")}/${get("database")}\" -f $path"))
            }
            InputStreamReader(proc.inputStream).readLines().forEach(::println)

            //检查数据库是否报错
            val regex = "(psql:.+:\\d+: 错误)".toRegex()
            InputStreamReader(proc.errorStream).readLines().takeIf {
                it.find { regex.find(it) != null }.isNullOrBlank().not()
            }?.apply {
                println("=")
                forEach(::println)
                throw RuntimeException()
            }
        }
    }
}

