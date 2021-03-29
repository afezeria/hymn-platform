package com.github.afezeria.hymn.common.platform

/**
 * @author afezeria
 */
interface ScriptFunctionService {
    /**
     * 根据api执行脚本
     * @throws [com.github.afezeria.hymn.common.exception.ScriptNotFoundException] 脚本不存在时抛出
     */
    fun execute(
        api: String,
        vararg params: Any?,
    ): Any?
}