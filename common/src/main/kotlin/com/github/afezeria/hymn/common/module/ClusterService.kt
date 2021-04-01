package com.github.afezeria.hymn.common.module

import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.platform.DbCache
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class ClusterService : CommandLineRunner {
    @Autowired
    private lateinit var webServerAppContext: ServletWebServerApplicationContext

    @Autowired
    private lateinit var databaseService: DatabaseService

    private val cache: DbCache by lazy {
//        过期时间3分钟
        databaseService.getCache("module", 180)
    }

    @Value("\${hymn.lanIp}")
    lateinit var lanIp: String

    companion object {
        private val modules = mutableListOf<String>()

        internal fun addModule(name: String) {
            modules.add(name)
        }
    }


    fun getNodeAddress(): String {
        return lanIp + ":" + webServerAppContext.webServer.port
    }

    /**
     * 每分钟刷新在线模块信息
     */
    @Scheduled(cron = "0 * * * * ?")
    fun register() {
        modules.forEach { cache.set("$it:${getNodeAddress()}", getNodeAddress()) }
    }

    /**
     * 启动时注册模块信息
     */
    override fun run(vararg args: String?) {
        register()
    }
}