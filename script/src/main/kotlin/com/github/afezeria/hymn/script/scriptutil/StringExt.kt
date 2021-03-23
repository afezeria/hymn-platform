package com.github.afezeria.hymn.script.scriptutil

import com.github.afezeria.hymn.common.util.randomUUIDStr
import org.springframework.util.DigestUtils
import java.math.BigDecimal

/**
 * @author afezeria
 */
object StringExt {
    fun toBigDecimal(str: String): BigDecimal = str.toBigDecimal()
    fun toLong(str: String): Long = str.toLong()
    fun toInt(str: String): Int = str.toInt()
    fun toByte(str: String): Byte = str.toByte()
    fun toFloat(str: String): Float = str.toFloat()
    fun toDouble(str: String): Double = str.toDouble()

    fun toBigDecimalOrNull(str: String): BigDecimal? = str.toBigDecimalOrNull()
    fun toLongOrNull(str: String): Long? = str.toLongOrNull()
    fun toIntOrNull(str: String): Int? = str.toIntOrNull()
    fun toByteOrNull(str: String): Byte? = str.toByteOrNull()
    fun toFloatOrNull(str: String): Float? = str.toFloatOrNull()
    fun toDoubleOrNull(str: String): Double? = str.toDoubleOrNull()

    fun toByteArray(str: String): ByteArray = str.toByteArray()
    fun toCharArray(str: String): CharArray = str.toCharArray()

    fun toLowerCase(str: String): String = str.toLowerCase()
    fun toUpperCase(str: String): String = str.toUpperCase()

    fun uuid() = randomUUIDStr()
    fun md5(str: String): String {
        return DigestUtils.md5DigestAsHex(str.toByteArray())
    }

}