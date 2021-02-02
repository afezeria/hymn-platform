package github.afezeria.hymn.common.exception

/**
 * @author afezeria
 */
open class ResourceNotFoundException(
    name: String,
    code: String = "",
    httpCode: Int = 404
) : PlatformException(httpCode, code, "${name}不存在", null)