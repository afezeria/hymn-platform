package com.github.afezeria.hymn.common.util

import com.fasterxml.jackson.module.kotlin.readValue
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

/**
 *
 * date 2021/6/29
 */
class Json(initialCapacity: Int) : HashMap<String, Any?>(initialCapacity) {

    companion object {
        private const val DEFAULT_INITIAL_CAPACITY = 16
    }

    constructor() : this(DEFAULT_INITIAL_CAPACITY)

    constructor(m: Map<String, Any>) : this() {
        putAll(m)
    }

    fun getJsonObject(key: String): Json? {
        val value = get(key) ?: return null
        if (value is Json) {
            return value
        }
        return when (value) {
            is Json -> value
            is Map<*, *> -> {
                val json = Json()
                for ((k, v) in value.entries) {
                    if (k != null) {
                        json[k.toString()] = v
                    }
                }
                json
            }
            is String -> mapper.readValue(value)
            else -> {
                mapper.readValue(mapper.writeValueAsString(value))
            }
        }
    }

    fun getBoolean(key: String): Boolean? {
        val value = get(key)
        return TypeUtil.castToBoolean(value)
    }

    fun getBytes(key: String): ByteArray? {
        val value = get(key)
        return TypeUtil.castToBytes(value)
    }

    fun getByte(key: String): Byte? {
        val value = get(key)
        return TypeUtil.castToByte(value)
    }

    fun getShort(key: String): Short? {
        val value = get(key)
        return TypeUtil.castToShort(value)
    }

    fun getInteger(key: String): Int? {
        val value = get(key)
        return TypeUtil.castToInt(value)
    }

    fun getLong(key: String): Long? {
        val value = get(key)
        return TypeUtil.castToLong(value)
    }

    fun getFloat(key: String): Float? {
        val value = get(key)
        return TypeUtil.castToFloat(value)
    }

    fun getDouble(key: String): Double? {
        val value = get(key)
        return TypeUtil.castToDouble(value)
    }

    fun getBigDecimal(key: String): BigDecimal? {
        val value = get(key)
        return TypeUtil.castToBigDecimal(value)
    }

    fun getString(key: String): String? {
        val value = get(key)
        return value.toString()
    }

    fun getLocalDateTime(key: String): LocalDateTime? {
        val value = get(key)
        return TypeUtil.castToLocalDateTime(value)
    }

    fun getLocalDate(key: String): LocalDate? {
        val value = get(key)
        return TypeUtil.castToLocalDate(value)
    }

}