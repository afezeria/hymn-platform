package com.github.afezeria.hymn.common.exception


/**
 * @author afezeria
 */
open class BusinessException(
    msg: String,
    cause: Throwable? = null,
    code: String = "",
    httpCode: Int = 400
) : PlatformException(httpCode, code, msg, cause)