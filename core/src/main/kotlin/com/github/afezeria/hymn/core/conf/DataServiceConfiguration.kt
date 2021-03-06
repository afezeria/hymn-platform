package com.github.afezeria.hymn.core.conf

import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.platform.dataservice.DataService
import com.github.afezeria.hymn.core.module.service.*
import com.github.afezeria.hymn.core.platform.dataservice.ScriptDataService
import com.github.afezeria.hymn.core.platform.dataservice.ScriptDataServiceImpl
import com.github.afezeria.hymn.core.platform.script.ScriptService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.lang.reflect.Proxy

/**
 * @author afezeria
 */
@Configuration
class DataServiceConfiguration {
    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var bizObjectService: BizObjectService

    @Autowired
    lateinit var typeService: BizObjectTypeService

    @Autowired
    lateinit var fieldService: BizObjectFieldService

    @Autowired
    lateinit var objectPermService: BizObjectPermService

    @Autowired
    lateinit var typePermService: BizObjectTypePermService

    @Autowired
    lateinit var fieldPermService: BizObjectFieldPermService

    @Autowired
    lateinit var databaseService: DatabaseService

    @Autowired
    lateinit var scriptService: ScriptService

    fun createDataServiceImpl(memorize: Boolean): ScriptDataService {
        return ScriptDataServiceImpl(
            accountService,
            bizObjectService,
            typeService,
            fieldService,
            objectPermService,
            typePermService,
            fieldPermService,
            databaseService,
            scriptService,
            memorize
        )
    }

    @Bean
    fun dataService(): DataService {
        val impl = createDataServiceImpl(false)
        val regex = Regex("^(insert|batchInsert|bulkInsert|update|batchUpdate|delete|batchDelete)")
        val proxy = Proxy.newProxyInstance(
            DataService::class.java.classLoader,
            arrayOf(DataService::class.java),
        ) { proxy, method, args ->
            if (regex.containsMatchIn(method.name)) {
                databaseService.useTransaction {
                    method?.invoke(createDataServiceImpl(true), *(args ?: arrayOfNulls<Any>(0)))
                }
            } else {
                method?.invoke(
                    impl,
                    *(args ?: arrayOfNulls<Any>(0))
                )
            }
        } as DataService
        return proxy
    }
}