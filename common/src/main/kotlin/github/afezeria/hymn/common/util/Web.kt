package github.afezeria.hymn.common.util

import github.afezeria.hymn.common.constant.ResultCode

/**
 * @author afezeria
 */
data class ApiResp<T>(
    val code: Int,
    val result: Boolean,
    val resultInfo: String,
    val msg: String = "",
    val data: T? = null
)

fun <T> successApiResp(data: T? = null): ApiResp<T> {
    return ApiResp(0, true, "OK", data = data)
}

fun <T> failApiResp(code: ResultCode, msg: String = "", data: T? = null): ApiResp<T> {
    return ApiResp(code.code, false, msg, data = data)
}

fun <T> dataNotFoundApiResp(msg: String = ""): ApiResp<T> {
    return ApiResp(ResultCode.DATA_NOT_FOUND.code, false, "$msg not found", )
}


fun <T> businessFailedApiResp(msg: String = ""): ApiResp<T> {
    return ApiResp(
        code = ResultCode.BUSINESS_FAILED.code,
        result = false,
        resultInfo = ResultCode.BUSINESS_FAILED.msg,
        msg = msg
    )
}

fun <T> innerErrorApiResp(msg: String = ""): ApiResp<T> {
    return ApiResp(
        code = ResultCode.INNER_ERROR.code,
        result = false,
        resultInfo = ResultCode.INNER_ERROR.msg,
        msg = msg
    )
}

fun <T> unauthorizedApiResp(msg: String = ""): ApiResp<T> {
    return ApiResp(
        code = ResultCode.UNAUTHORIZED.code,
        result = false,
        resultInfo = ResultCode.UNAUTHORIZED.msg,
        msg = msg
    )
}


val successApiResp = successApiResp<Nothing>()
//val unauthorizedApiResp = failApiResp(ResultCode.UNAUTHORIZED)
//val dataNotFoundApiResp = failApiResp(ResultCode.DATA_NOT_FOUND)
//val innerErrorApiResp = failApiResp(ResultCode.INNER_ERROR)
//val badRequest = failApiResp(ResultCode.BAD_REQUEST)
//val routeNotFoundApiResp = failApiResp(ResultCode.ROUTE_NOT_FOUND)
//val businessFailedApiResp = failApiResp(ResultCode.BUSINESS_FAILED)