package com.github.afezeria.hymn.script.scriptutil

import com.github.afezeria.hymn.common.util.randomUUIDStr

/**
 * @author afezeria
 */
class ScriptTools {
    @JvmField
    val str = StringExt

    @JvmField
    val date = DateExt

    @JvmField
    val http = HttpExt
    fun uuid() = randomUUIDStr()
}