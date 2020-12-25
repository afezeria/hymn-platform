package github.afezeria.hymn.common.util

import github.afezeria.hymn.common.constant.ResultCode


/**
 * @author afezeria
 */
sealed class PlatformException(val code: ResultCode, override val message: String = code.msg) :
    RuntimeException(message)

class NoAccessException(msg: String = ResultCode.PERMISSION_DENIED.msg) :
    PlatformException(ResultCode.PERMISSION_DENIED, msg)

class UnauthorizedException(msg: String = ResultCode.UNAUTHORIZED.msg) :
    PlatformException(ResultCode.UNAUTHORIZED, msg)

class DataNotFoundException(msg: String = ResultCode.DATA_NOT_FOUND.msg) :
    PlatformException(ResultCode.DATA_NOT_FOUND, "$msg not found")

class InnerException(msg: String = ResultCode.INNER_ERROR.msg) :
    PlatformException(ResultCode.INNER_ERROR, msg)

class BusinessException(msg: String = ResultCode.BUSINESS_FAILED.msg) :
    PlatformException(ResultCode.BUSINESS_FAILED, msg)