package github.afezeria.hymn.common

import org.springframework.core.io.support.PathMatchingResourcePatternResolver
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
            val classLoader = this::class.java.classLoader
            prop.load(
                classLoader.getResourceAsStream("admin-database.properties")
            )
            PathMatchingResourcePatternResolver(classLoader)
                .getResources("classpath:/sql/**/*.sql")
                .groupBy {
                    "\\[sql/(\\w+)".toRegex()
                        .find(it.description)!!.groupValues[1]
                }.map { it.key to it.value.sortedBy { it.filename } }
                .sortedBy { it.first != "common" }
                .forEach { scripts ->
                    scripts.second.forEach { res ->
                        val temp = File.createTempFile("${scripts.first}-${res.filename}", ".sql")
                        temp.deleteOnExit()
                        temp.outputStream().use {
                            it.write(res.inputStream.readAllBytes())
                        }
                        runSqlScript(temp.absolutePath)
                    }
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
            }.takeIf { it.isNotEmpty() }?.apply {
                println("========== error ==========")
                throw RuntimeException(this.joinToString("\n"))
            }
        }
    }
}