package github.afezeria.hymn.common.platform.script

import github.afezeria.hymn.common.constant.TriggerEvent
import github.afezeria.hymn.common.platform.dataservice.DataService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author afezeria
 */
interface ScriptService {
    /**
     * 执行触发器
     *
     * @param event 触发时机
     * @param objectApiName 业务对象api
     * @param new 新数据
     * @param old 旧数据
     * @param tmpMap 共享map，用于在多个触发器之间共享数据
     * @param around 触发器执行方法，可在触发器执行前后执行逻辑或者跳过触发器
     */
    fun executeTrigger(
        dataService: DataService,
        event: TriggerEvent,
        objectApiName: String,
        old: MutableMap<String, Any?>?,
        new: MutableMap<String, Any?>?,
        tmpMap: MutableMap<String, Any?>,
        around: (TriggerInfo, () -> Unit) -> Unit = { _, trigger -> trigger.invoke() },
    )

    /**
     * 执行自定义接口
     */
    fun executeInterface(
        dataService: DataService,
        api: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
    )

    /**
     * 执行其他脚本
     */
    fun executeScript(
        dataService: DataService,
        api: String,
        params: MutableMap<String, Any?>,
    ): Any?
}