package github.afezeria.hymn.common

import java.io.File
import java.io.InputStreamReader
import java.util.*


/**
 * @author afezeria
 */
open class BaseDbTest {

    companion object {
        private val prop = Properties()

        init {
            println("========== db init start ==========")
            prop.load(
                this::class.java.classLoader.getResourceAsStream("admin-database.properties")
            )
            val scripts = listOf(
                "1.table.sql",
                "2.history-table-and-trigger.sql",
                "3.function.sql",
                "4.init-data.sql",
                "5.constraint.sql",
                "6.test-data-table.sql",
            )
            for (script in scripts) {
                val temp = File.createTempFile(script, ".sql")
                temp.deleteOnExit()
                val stream =
                    requireNotNull(this::class.java.classLoader.getResourceAsStream("sql/$script"))
                temp.outputStream().use {
                    it.write(stream.readAllBytes())
                }
                runSqlScript(temp.absolutePath)
            }
            println("========== db init end ==========")
        }

        private fun runSqlScript(path: String) {
            val proc = prop.run {
                println("run script $path")
                val command =
                    "psql \"postgresql://${get("dataSource.user")}:${get("dataSource.password")}@${
                        get("dataSource.serverName")
                    }:${get("dataSource.portNumber")}/${get("dataSource.databaseName")}\" -f $path -q"
                Runtime.getRuntime().exec(arrayOf("bash", "-c", command))
            }
//            InputStreamReader(proc.inputStream).readLines().forEach(::println)

            //检查数据库是否报错
            val regex = "(psql:(.+:\\d+:)* 错误)".toRegex()

            InputStreamReader(proc.errorStream).readLines().filter {
                regex.find(it) != null
            }.takeIf { it.isNotEmpty() }?.apply{
                println("========== error ==========")
                throw RuntimeException(this.joinToString("\n"))
            }
        }
    }
}