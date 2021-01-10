package github.afezeria.hymn.common.util

import github.afezeria.hymn.common.constant.ResultCode


/**
 * @author afezeria
 */
sealed class PlatformException(
    val code: ResultCode,
    override val message: String = code.msg,
    cause: Throwable? = null
) :
    RuntimeException(message, cause)

class PermissionDeniedException(
    msg: String = ResultCode.PERMISSION_DENIED.msg,
    cause: Throwable? = null
) :
    PlatformException(ResultCode.PERMISSION_DENIED, msg, cause)

class UnauthorizedException(msg: String = ResultCode.UNAUTHORIZED.msg, cause: Throwable? = null) :
    PlatformException(ResultCode.UNAUTHORIZED, msg, cause)

class DataNotFoundException(msg: String = ResultCode.DATA_NOT_FOUND.msg, cause: Throwable? = null) :
    PlatformException(ResultCode.DATA_NOT_FOUND, "$msg not found", cause)

class InnerException(msg: String = ResultCode.INNER_ERROR.msg, cause: Throwable? = null) :
    PlatformException(ResultCode.INNER_ERROR, msg, cause)

class BusinessException(msg: String = ResultCode.BUSINESS_FAILED.msg, cause: Throwable? = null) :
    PlatformException(ResultCode.BUSINESS_FAILED, msg, cause)