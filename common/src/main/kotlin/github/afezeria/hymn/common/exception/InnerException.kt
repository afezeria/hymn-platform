package github.afezeria.hymn.common.exception

/**
 * @author afezeria
 */
open class InnerException(
    msg: String = "服务器内部错误",
    cause: Throwable? = null,
    code: String = "",
) : PlatformException(500, code, msg, cause)