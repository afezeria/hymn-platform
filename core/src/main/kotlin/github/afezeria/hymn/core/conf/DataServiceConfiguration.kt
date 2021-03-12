package github.afezeria.hymn.core.conf

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.platform.dataservice.DataService
import github.afezeria.hymn.common.platform.script.ScriptService
import github.afezeria.hymn.core.module.service.*
import github.afezeria.hymn.core.service.dataservice.ScriptDataServiceImpl
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

    @Bean
    fun dataService(): DataService {
        val createImpl = { memorize: Boolean ->
            ScriptDataServiceImpl(
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
        val impl = createImpl(false)
        val regex = Regex("^(insert|batchInsert|bulkInsert|update|batchUpdate|delete|batchDelete)")
        val proxy = Proxy.newProxyInstance(
            DataService::class.java.classLoader,
            arrayOf(DataService::class.java),
        ) { proxy, method, args ->
            if (regex.containsMatchIn(method.name)) {
                databaseService.useTransaction {
                    method?.invoke(createImpl(true), *(args ?: arrayOfNulls<Any>(0)))
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