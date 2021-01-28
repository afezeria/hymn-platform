package github.afezeria.hymn.common.exception

/**
 * @author afezeria
 */
open class PlatformException(
    val httpCode: Int = 500,
    val code: String = "",
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)