package com.github.afezeria.hymn.common.web

/**
 * @author afezeria
 */
class ErrorResponse(
    val code: String = "",
    val message: String,
    val errors: String = ""
)