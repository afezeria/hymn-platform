package com.github.afezeria.hymn.common.platform.script

/**
 * @author afezeria
 */
interface ScriptFunctionService {
    /**
     * 根据api执行脚本
     * @throws [ScriptNotFoundException]
     */
    fun execute(
        api: String,
        vararg params: Any?,
    ): Any?
}