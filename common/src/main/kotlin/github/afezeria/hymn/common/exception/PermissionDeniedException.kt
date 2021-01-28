package github.afezeria.hymn.common.exception

/**
 * @author afezeria
 */
open class PermissionDeniedException(
    msg: String = "无访问权限",
    cause: Throwable? = null,
    code: String = "",
) : PlatformException(403, code, msg, cause)