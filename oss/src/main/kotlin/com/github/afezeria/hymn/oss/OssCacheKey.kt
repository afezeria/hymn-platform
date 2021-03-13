package com.github.afezeria.hymn.oss

/**
 * @author afezeria
 */
object OssCacheKey {
    fun preSigned(randomId: String) = "oss:presigned:$randomId"
}