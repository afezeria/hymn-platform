package com.github.afezeria.hymn.core.service.dataservice

import com.github.afezeria.hymn.common.exception.BusinessException
import com.github.afezeria.hymn.common.exception.InnerException
import com.github.afezeria.hymn.common.platform.dataservice.FieldInfo
import mu.KLogging
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author afezeria
 */
class NewDataValidator {

    companion object : KLogging() {
        class InvalidDataTypeException(field: FieldInfo, value: Any) : BusinessException(
            "字段 ${field.name} 数据类型错误，要求类型：${field.getTypeDescription()},实际类型：${value.javaClass.simpleName}",
        )

        private val DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")!!
        fun check(
            field: FieldInfo,
            value: Any?,
        ): Pair<Boolean, Any?> {
            return value?.run {
                var res = this
                when (field.type) {
                    "text" -> {
                        if (value is String) {
                            if (value.length < field.minLength!! || value.length > field.maxLength!!) {
                                throw BusinessException("字段 ${field.name} 长度必须大于${field.minLength}且小于${field.maxLength}")
                            }
                        } else {
                            throw InvalidDataTypeException(field, value)
                        }
                    }
                    "check_box" -> {
                        if (value !is Boolean) {
                            throw InvalidDataTypeException(field, value)
                        }
                    }
                    "check_box_group" -> {
                        if (value is String) {
                            val split = value.split(",").filter { it.isNotBlank() }
                            if (split.size > field.optionalNumber!!) {
                                throw BusinessException("字段 ${field.name} 选择的选项个数必须小于等于 ${field.optionalNumber}")
                            }
                        } else {
                            throw InvalidDataTypeException(field, value)
                        }
                    }
                    "select" -> {
                        if (value is String) {
                            val split = value.split(",").filter { it.isNotBlank() }
                            if (split.size > field.optionalNumber!!) {
                                throw BusinessException("字段 ${field.name} 选择的选项个数必须小于等于 ${field.optionalNumber}")
                            }
                        } else {
                            throw InvalidDataTypeException(field, value)
                        }
                    }
                    "integer" -> {
                        if (value is Long || value is Int) {
                            res = value.toString().toLong()
                            if (res < field.minLength!! || res > field.maxLength!!) {
                                throw BusinessException("字段 ${field.name} 的值必须大于 ${field.minLength} 且小于等于 ${field.maxLength}")
                            }
                        } else {
                            throw InvalidDataTypeException(field, value)
                        }
                    }
                    "float" -> {
                        res = when (value) {
                            is Double -> value
                            is Float -> value.toDouble()
                            else -> {
                                try {
                                    value.toString().toDouble()
                                } catch (e: Exception) {
                                    throw InvalidDataTypeException(field, value)
                                }
                            }
                        }
                    }
                    "money" -> {
                        res = when (value) {
                            is BigDecimal -> value
                            is String -> {
                                try {
                                    BigDecimal(value)
                                } catch (e: Exception) {
                                    throw BusinessException("字段 ${field.name} 格式错误，无法将 \"$value\" 转换为 BigDecimal")
                                }
                            }
                            else -> {
                                throw InvalidDataTypeException(field, value)
                            }
                        }
                    }
                    "date" -> {
                        if (value is String) {
                            try {
                                res = LocalDateTime.parse(value, DATE_TIME_FORMAT)
                            } catch (e: Exception) {
                                throw BusinessException("字段 ${field.name} 格式错误，无法将 \"$value\" 转换为 LocalDateTime")
                            }
                        } else if (value !is LocalDateTime) {
                            throw InvalidDataTypeException(field, value)
                        }
                    }
                    "datetime" -> {
                        if (value is String) {
                            try {
                                res = LocalDateTime.parse(value, DATE_TIME_FORMAT)
                            } catch (e: Exception) {
                                throw BusinessException("字段 ${field.name} 格式错误，无法将 \"$value\" 转换为 LocalDateTime")
                            }
                        } else if (value !is LocalDateTime) {
                            throw InvalidDataTypeException(field, value)
                        }
                    }
                    "master_slave", "reference", "mreference", "areference", "picture", "files" -> {
                        if (value !is String) {
                            throw InvalidDataTypeException(field, value)
                        }
                    }
                    "summary", "auto" -> {
//                        NewRecordMap中不添加汇总和自动编号字段
                        return false to null
                    }
                    else -> {
//                        never execute
                        throw InnerException("错误的字段类型:[$field]")
                    }
                }
                true to res
            } ?: true to null
        }
    }
}
