package github.afezeria.hymn.common.platform

import github.afezeria.hymn.common.constant.TriggerEvent
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
     * @param objectApi 业务对象api
     * @param new 新数据
     * @param old 旧数据
     * @param data 共享map，用于在多个触发器之间共享数据
     */
    fun executeTrigger(
        event: TriggerEvent,
        objectApi: String,
        new: MutableMap<String, Any?>,
        old: MutableMap<String, Any?>,
        data: MutableMap<String, Any?>
    )

    /**
     * 执行自定义接口
     */
    fun executeInterface(request: HttpServletRequest, response: HttpServletResponse)

    /**
     * 执行其他脚本
     */
    fun executeScript(params: MutableMap<String, Any?>)
}