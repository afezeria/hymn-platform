package github.afezeria.hymn.core.service.dataservice

import github.afezeria.hymn.common.exception.BusinessException
import github.afezeria.hymn.common.exception.InnerException
import github.afezeria.hymn.common.platform.Session
import github.afezeria.hymn.common.platform.dataservice.DataService
import github.afezeria.hymn.common.platform.dataservice.FieldInfo
import github.afezeria.hymn.common.util.execute
import mu.KLogger
import org.ktorm.database.Database
import java.time.LocalDateTime

/**
 * @author afezeria
 */
interface ScriptDataService : DataService {
    val logger: KLogger
    val database: Database

    fun execute(
        sqlBuilder: (
            oldData: Map<String, Any?>?,
            newData: RecordMap?,
        ) -> Pair<String, Collection<Any?>>,
        type: WriteType,
        objectApiName: String,
        oldData: Map<String, Any?>?,
        newData: RecordMap?,
        withTrigger: Boolean,
    ): MutableMap<String, Any?>


    override fun sql(sql: String, vararg params: Any?): MutableList<MutableMap<String, Any?>> {
        database.useConnection {
            return it.execute(sql, *params)
        }
    }

    override fun sql(sql: String, params: List<Any?>): MutableList<MutableMap<String, Any?>> {
        database.useConnection {
            return it.execute(sql, params)
        }
    }

    override fun sql(
        sql: String,
        params: Map<String, Any?>
    ): MutableList<MutableMap<String, Any?>> {
        database.useConnection {
            return it.execute(sql, params)
        }
    }

    fun checkNewDataValue(
        field: FieldInfo,
        value: Any?,
        session: Session,
        now: LocalDateTime
    ): Any? {
        return value?.apply {

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
        }
    }

}