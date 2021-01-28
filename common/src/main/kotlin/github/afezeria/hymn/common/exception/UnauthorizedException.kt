package github.afezeria.hymn.common.exception

/**
 * @author afezeria
 */
open class UnauthorizedException(
    msg: String = "未登录",
    cause: Throwable? = null,
    code: String = "",
) : PlatformException(401, code, msg, cause)