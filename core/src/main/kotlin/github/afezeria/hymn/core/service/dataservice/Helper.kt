package github.afezeria.hymn.core.service.dataservice

import github.afezeria.hymn.common.exception.BusinessException
import github.afezeria.hymn.common.exception.InnerException
import github.afezeria.hymn.common.platform.dataservice.FieldInfo
import mu.KLogging

/**
 * @author afezeria
 */
class NewDataValidator {
    companion object : KLogging() {
        fun check(
            field: FieldInfo,
            value: Any?,
        ): Any? {
            return value?.run {
                val res = this
                when (field.type) {
                    "text" -> {
                        if (value is String) {
                            if (value.length < field.minLength!! || value.length > field.maxLength!!) {
                                throw BusinessException("字段 ${field.name} 长度必须大于${field.minLength}且小于${field.maxLength}")
                            }
                        } else {
                            throw BusinessException("字段 ${field.name} 数据类型错误，要求类型：${field.getTypeDescription()},实际类型：${value.javaClass.simpleName}")
                        }
                    }
                    "check_box" -> {
                        if (value is Boolean) {

                        } else {
                            throw BusinessException("字段 ${field.name} 数据类型错误，要求类型：${field.getTypeDescription()},实际类型：${value.javaClass.simpleName}")
                        }
                    }
                    "check_box_group" -> {
                        if (value is String) {
                            val split = value.split(",").filter { it.isNotBlank() }
                            if (split.size < field.optionalNumber!!) {
                                throw BusinessException("字段 ${field.name} 选择的选项个数必须大于等于${field.optionalNumber}")
                            }
                        } else {
                            throw BusinessException("字段 ${field.name} 数据类型错误，要求类型：${field.getTypeDescription()},实际类型：${value.javaClass.simpleName}")
                        }
                    }
                    "select" -> {
                    }
                    "integer" -> {
                    }
                    "float" -> {
                    }
                    "money" -> {
                    }
                    "date" -> {
                    }
                    "datetime" -> {
                    }
                    "master_slave" -> {
                    }
                    "reference" -> {
                    }
                    "mreference" -> {
                    }
                    "areference" -> {
                    }
                    "summary" -> {
                    }
                    "auto" -> {
                    }
                    "picture" -> {
                    }
                    "files" -> {
                    }
                    else -> throw InnerException("错误的字段类型:[$field]")
                }
                res
            }
        }
    }
}
