package com.github.afezeria.hymn.common.platform

/**
 * 定时任务接口
 *
 * 所有允许通过系统配置的静态编译的定时任务都需要实现该接口
 *
 * 子类必须是无状态的
 * @author afezeria
 */
interface StaticTimedTask {
    fun execute(parameter: String)
}