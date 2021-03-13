package com.github.afezeria.hymn.common.platform.script

import com.github.afezeria.hymn.common.constant.TriggerEvent
import com.github.afezeria.hymn.common.platform.dataservice.DataService
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
        old: Map<String, Any?>?,
        new: Map<String, Any?>?,
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

    /**
     * 编译脚本
     *
     * [txCallback]会在脚本编译完成并没有错误时执行，调用后将提交事务并清空所有节点关于该脚本的缓存
     *
     * [type]和[id]一起组成一个脚本的唯一标识
     *
     * [id]为null时不执行清空缓存逻辑
     *
     * @param type 编译代码的类型
     * @param id 脚本id
     * @param lang 脚本使用
     * @param option 编译选项
     * @param code 脚本代码
     * @param txCallback 事务回调，调用者提供的执行数据库更新的函数，
     */
    fun <T> compile(
        type: CompileType,
        id: String?,
        lang: String,
        option: String?,
        code: String,
        txCallback: () -> T
    ): T
}