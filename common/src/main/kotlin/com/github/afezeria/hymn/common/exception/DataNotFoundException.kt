package com.github.afezeria.hymn.common.exception

/**
 * @author afezeria
 */
open class DataNotFoundException(
    msg: String = "数据",
    cause: Throwable? = null,
    code: String = "",
) : PlatformException(400, code, "$msg 不存在", cause)