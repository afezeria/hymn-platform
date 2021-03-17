package com.github.afezeria.hymn.core.conf

import com.github.afezeria.hymn.common.platform.StaticTimedTask
import com.github.afezeria.hymn.common.platform.script.ScriptService
import com.github.afezeria.simplescheduler.ActionProvider
import com.github.afezeria.simplescheduler.Scheduler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

/**
 * @author afezeria
 */
@Configuration
class SimpleScheduleConfiguration {
    @Autowired
    private lateinit var dataSource: DataSource

    @Autowired
    private lateinit var dataServiceConfiguration: DataServiceConfiguration

    @Autowired(required = false)
    private var taskList: List<StaticTimedTask> = emptyList()

    @Autowired
    private lateinit var scriptService: ScriptService

    @Bean
    fun test() {
        Scheduler(
            dataSource = dataSource,
            actionProvider = ActionProviderImpl(scriptService, taskList),
            maximumPoolSize = 20,
            pollInterval = 10,
            printStackTraceToErrorMsg = true,
            schema = "hymn",
        )
    }

    inner class ActionProviderImpl(
        private val scriptService: ScriptService,
        taskList: List<StaticTimedTask>,
    ) : ActionProvider {
        private val staticTimedTaskMap: Map<String, StaticTimedTask> =
            taskList.map { it.javaClass.name to it }.toMap()


        override fun getTask(actionName: String): ((String) -> Unit)? {
            var action = staticTimedTaskMap[actionName]?.run {
                { param: String -> this.execute(param) }
            }

            if (action == null) {
                val script = scriptService.getScript(actionName)
                if (script != null) {
                    action = { param: String ->
                        script.invoke(
                            arrayOf(
                                dataServiceConfiguration.createDataServiceImpl(true),
                                param
                            )
                        )
                    }
                }
            }
            return action
        }
    }
}