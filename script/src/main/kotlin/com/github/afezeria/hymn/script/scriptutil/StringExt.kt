package com.github.afezeria.hymn.script.scriptutil

import org.springframework.util.DigestUtils
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.nio.charset.Charset
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * @author afezeria
 */
object StringExt {
    fun toBigDecimalOrNull(str: String, precision: Int): BigDecimal? {
        return str.toBigDecimalOrNull(MathContext(precision, RoundingMode.HALF_UP))
    }

    fun toLongOrNull(str: String, radix: Int): Long? = str.toLongOrNull(radix)
    fun toIntOrNull(str: String, radix: Int): Int? = str.toIntOrNull(radix)
    fun toByteOrNull(str: String, radix: Int): Byte? = str.toByteOrNull(radix)
    fun toFloatOrNull(str: String): Float? = str.toFloatOrNull()
    fun toDoubleOrNull(str: String): Double? = str.toDoubleOrNull()
    fun toBoolean(str: String): Boolean = str.toBoolean()

    fun toCharArray(str: String): CharArray = str.toCharArray()
    fun toByteArray(str: String, charset: String?): ByteArray {
        return if (charset == null) {
            str.toByteArray(Charsets.UTF_8)
        } else {
            str.toByteArray(Charset.forName(charset))
        }
    }

    fun md5(str: String): String {
        return DigestUtils.md5DigestAsHex(str.toByteArray())
    }

    fun toDateTime(str: String, format: String?): LocalDateTime? {
        return try {
            if (format == null) {
                LocalDateTime.parse(str, DateExt.defaultFormatter)
            } else {
                LocalDateTime.parse(str, DateTimeFormatter.ofPattern(format))
            }
        } catch (e: DateTimeParseException) {
            null
        }
    }

    fun toDate(str: String, format: String?): LocalDate? {
        return try {
            if (format == null) {
                LocalDate.parse(str, DateTimeFormatter.ISO_LOCAL_DATE)
            } else {
                LocalDate.parse(str, DateTimeFormatter.ofPattern(format))
            }
        } catch (e: DateTimeParseException) {
            null
        }

    }

}