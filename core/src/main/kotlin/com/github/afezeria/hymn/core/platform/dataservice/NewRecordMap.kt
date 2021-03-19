package com.github.afezeria.hymn.core.platform.dataservice

import com.github.afezeria.hymn.common.platform.dataservice.FieldInfo
import java.util.function.BiFunction
import java.util.function.Function

/**
 * @author afezeria
 */
class NewRecordMap(private val fieldApiMap: Map<String, FieldInfo>) :
    LinkedHashMap<String, Any?>() {

    private fun checkValue(
        key: String,
        value: Any?,
    ): Pair<Boolean, Any?> {
        val field = fieldApiMap[key] ?: return false to null
        return NewDataValidator.check(field, value)
    }

    override fun put(key: String, value: Any?): Any? {
        val (flag, newValue) = checkValue(key, value)
        return if (flag) {
            super.put(key, newValue)
        } else {
            null
        }
    }

    override fun putAll(from: Map<out String, Any?>) {
        for ((k, v) in from) {
            put(k, v)
        }
    }

    override fun putIfAbsent(key: String, value: Any?): Any? {
        var v = get(key)
        if (v == null) {
            v = put(key, value)
        }
        return v
    }

    override fun remove(key: String): Any? {
        return replace(key, null)
    }

    override fun remove(key: String, value: Any?): Boolean {
        return replace(key, value, null)
    }

    override fun replace(key: String, oldValue: Any?, newValue: Any?): Boolean {
        if (!containsKey(key)) return false
        val v = get(key)

        if (v != oldValue) return false

        val fieldInfo = requireNotNull(fieldApiMap[key])
//        标准字段新值不能为null
        return if (fieldInfo.predefined && newValue == null) {
            false
        } else {
            put(key, oldValue)
            true
        }
    }

    override fun replace(key: String, value: Any?): Any? {
        if (!containsKey(key)) return null
        val fieldInfo = requireNotNull(fieldApiMap[key])
//        标准字段新值不能为null
        return if (fieldInfo.predefined && value == null) {
            null
        } else {
            put(key, value)
        }
    }

    override fun computeIfAbsent(
        key: String,
        mappingFunction: Function<in String, out Any?>
    ): Any? {
        throw UnsupportedOperationException()
    }

    override fun computeIfPresent(
        key: String,
        remappingFunction: BiFunction<in String, in Any, out Any?>
    ): Any? {
        throw UnsupportedOperationException()
    }

    override fun compute(
        key: String,
        remappingFunction: BiFunction<in String, in Any?, out Any?>
    ): Any? {
        throw UnsupportedOperationException()
    }

    override fun replaceAll(function: BiFunction<in String, in Any?, out Any?>) {
        throw UnsupportedOperationException()
    }
}