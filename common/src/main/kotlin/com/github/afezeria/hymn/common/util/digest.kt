package com.github.afezeria.hymn.common.util

import java.math.BigInteger
import java.security.MessageDigest

/**
 * @author afezeria
 */

val md5 = MessageDigest.getInstance("MD5")

fun String.md5(): String {
    return String.format(
        "%032x",
        BigInteger(1, md5.digest(toByteArray(Charsets.UTF_8)))
    )
}
