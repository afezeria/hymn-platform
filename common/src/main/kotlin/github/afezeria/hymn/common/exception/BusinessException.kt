package github.afezeria.hymn.common.exception


/**
 * @author afezeria
 */
open class BusinessException(
    msg: String,
    cause: Throwable? = null,
    code: String = "",
) : PlatformException(400, code, msg, cause)