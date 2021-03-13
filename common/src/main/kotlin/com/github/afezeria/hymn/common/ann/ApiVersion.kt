package com.github.afezeria.hymn.common.ann

/**
 * @author afezeria
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class ApiVersion(val value: Int = 2104)