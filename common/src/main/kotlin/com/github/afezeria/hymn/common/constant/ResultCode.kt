package com.github.afezeria.hymn.common.constant

/**
 * @author afezeria
 */

enum class ResultCode(val code: Int, val msg: String) {
    SUCCESS(0, "ok"),
    UNAUTHORIZED(1001, "unauthorized"),
    NOT_LOGGED_IN(1002, "not logged in"),
    DATA_NOT_FOUND(2001, "data does not exist"),
    INNER_ERROR(3001, "internal error, please contact administrators"),
    BAD_REQUEST(4001, "bad request"),
    ROUTE_NOT_FOUND(5001, "route not found"),
    BUSINESS_FAILED(6001, "business failed"),
    PERMISSION_DENIED(7001, "permission denied")


//    /* 参数错误：10001-19999 */
//    PARAM_IS_INVALID(10001, "参数无效"),
//    PARAM_IS_BLANK(10002, "参数为空"),
//    PARAM_TYPE_BIND_ERROR(10003, "参数类型错误"),
//    PARAM_NOT_COMPLETE(10004, "参数缺失"),
//
//    /* 用户错误：20001-29999*/
//    USER_NOT_LOGGED_IN(20001, "用户未登录"),
//    USER_LOGIN_ERROR(20002, "账号不存在或密码错误"),
//    USER_ACCOUNT_FORBIDDEN(20003, "账号已被禁用"),
//    USER_NOT_EXIST(20004, "用户不存在"),
//    USER_HAS_EXISTED(20005, "用户已存在"),
//
//    /* 业务错误：30001-39999 */
//    SPECIFIED_QUESTIONED_USER_NOT_EXIST(30001, "某业务出现问题"),
//
//    /* 系统错误：40001-49999 */
//    SYSTEM_INNER_ERROR(40001, "系统繁忙，请稍后重试"),
//
//    /* 数据错误：50001-599999 */
//    RESULE_DATA_NONE(50001, "数据未找到"),
//    DATA_IS_WRONG(50002, "数据有误"),
//    DATA_ALREADY_EXISTED(50003, "数据已存在"),
//
//    /* 接口错误：60001-69999 */
//    INTERFACE_INNER_INVOKE_ERROR(60001, "内部系统接口调用异常"),
//    INTERFACE_OUTTER_INVOKE_ERROR(60002, "外部系统接口调用异常"),
//    INTERFACE_FORBID_VISIT(60003, "该接口禁止访问"),
//    INTERFACE_ADDRESS_INVALID(60004, "接口地址无效"),
//    INTERFACE_REQUEST_TIMEOUT(60005, "接口请求超时"),
//    INTERFACE_EXCEED_LOAD(60006, "接口负载过高"),
//
//    /* 权限错误：70001-79999 */
//    PERMISSION_NO_ACCESS(70001, "无访问权限");
}

