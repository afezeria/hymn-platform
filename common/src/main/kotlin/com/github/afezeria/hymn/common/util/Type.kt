package com.github.afezeria.hymn.common.util

import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

/**
 *
 * @author afezeria
 * date 2021/6/29 下午10:18
 */
object TypeUtil {
    private val DEFAULT_ZONE_ID = TimeZone.getDefault().toZoneId()

    fun castToString(value: Any?): String? {
        return value?.toString()
    }

    fun castToByte(value: Any?): Byte? {
        return when (value) {
            null -> null
            is Byte -> value
            is Number -> value.toByte()
            is String -> value.toByteOrNull()
            else -> throw TypeCastException("can not cast to byte, value : $value")
        }
    }

    fun castToShort(value: Any?): Short? {
        return when (value) {
            null -> null
            is Short -> value
            is Number -> value.toShort()
            is String -> value.toShortOrNull()
            else -> throw TypeCastException("can not cast to short, value : $value")
        }
    }

    fun castToInt(value: Any?): Int? {
        return when (value) {
            null -> null
            is Int -> value
            is Number -> value.toInt()
            is String -> value.toIntOrNull()
            is Boolean -> if (value) 1 else 0
            else -> throw TypeCastException("can not cast to int, value : $value")
        }
    }

    fun castToLong(value: Any?): Long? {
        return when (value) {
            null -> null
            is Long -> value
            is Number -> value.toLong()
            is String -> value.toLongOrNull()
            else -> throw TypeCastException("can not cast to Long, value : $value")
        }
    }

    fun castToFloat(value: Any?): Float? {
        return when (value) {
            null -> null
            is Float -> value
            is Number -> value.toFloat()
            is String -> value.toFloatOrNull()
            else -> throw TypeCastException("can not cast to Float, value : $value")
        }
    }

    fun castToDouble(value: Any?): Double? {
        return when (value) {
            null -> null
            is Double -> value
            is Number -> value.toDouble()
            is String -> value.toDoubleOrNull()
            else -> throw TypeCastException("can not cast to Double, value : $value")
        }
    }

    fun castToBigDecimal(value: Any?): BigDecimal? {
        return when (value) {
            null -> null
            is BigDecimal -> value
            is Number -> BigDecimal(value.toString())
            is String -> BigDecimal(value.toString())
            else -> throw TypeCastException("can not cast to Float, value : $value")
        }
    }

    fun castToBoolean(value: Any?): Boolean? {
        return when (value) {
            null -> null
            is Boolean -> value
            is Number -> value.toInt() > 0
            is String -> value.toBooleanStrictOrNull()
            else -> throw TypeCastException("can not cast to Double, value : $value")
        }
    }

    fun castToBytes(value: Any?): ByteArray? {
        return when (value) {
            null -> null
            is ByteArray -> value
            is String -> value.toByteArray()
            else -> throw TypeCastException("can not cast to Bytes, value : $value")
        }
    }

    fun castToLocalDateTime(value: Any?): LocalDateTime? {
        return when (value) {
            null -> null
            is LocalDateTime -> value
            is Long -> {
                Instant.ofEpochMilli(value).atZone(DEFAULT_ZONE_ID).toLocalDateTime()
            }
            is String -> {
                if (value.isBlank()) {
                    return null
                }
                try {
                    return LocalDateTime.parse(value, yyyyMMddHHmmss)
                } catch (e: Exception) {
                    throw TypeCastException("can not cast to Bytes, value : $value")
                }
            }
            is LocalDate -> {
                LocalDateTime.of(value, LocalTime.MIDNIGHT)
            }
            else -> throw TypeCastException("can not cast to Bytes, value : $value")
        }
    }

    fun castToLocalDate(value: Any?): LocalDate? {
        return when (value) {
            null -> null
            is LocalDate -> value
            is Long -> {
                Instant.ofEpochMilli(value).atZone(DEFAULT_ZONE_ID).toLocalDate()
            }
            is String -> {
                if (value.isBlank()) {
                    return null
                }
                try {
                    return LocalDate.parse(value)
                } catch (e: Exception) {
                    throw TypeCastException("can not cast to Bytes, value : $value")
                }
            }
            is LocalDateTime -> {
                value.toLocalDate()
            }
            else -> throw TypeCastException("can not cast to Bytes, value : $value")
        }
    }
}
