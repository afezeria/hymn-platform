package github.afezeria.hymn.common.db

import github.afezeria.hymn.common.exception.InnerException
import github.afezeria.hymn.common.util.lCamelize
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.Column
import java.lang.reflect.Constructor
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

/**
 * @author afezeria
 */
abstract class AbstractTable<E : AbstractEntity>(
    tableName: String,
    alias: String? = null,
    catalog: String? = null,
    schema: String? = null,
    entityClass: KClass<E>? = null
) : BaseTable<E>(tableName, alias, catalog, schema, entityClass) {

    inner class InnerMap(val fn: (String) -> Any?) : LinkedHashMap<String, Column<*>>() {
        override fun put(key: String, value: Column<*>): Column<*>? {
            fn(key)
            return super.put(key, value)
        }
    }

    private val columnName2Field: MutableMap<String, EntityField> = mutableMapOf()
    private val fieldName2Field: MutableMap<String, EntityField> = mutableMapOf()
    private val entityParameterlessConstructor: Constructor<E>

    init {
        val columns = BaseTable::class.java.getDeclaredField("_columns")
        columns.isAccessible = true
        val kClass = this.entityClass ?: throw RuntimeException("无法获取表 $tableName 的实体类的Class实例")
        entityParameterlessConstructor = kClass.java.getConstructor()
        columns.set(this, InnerMap { columnName ->
            val fieldName = columnName.lCamelize()
            kClass.memberProperties
                .find { it.name == fieldName }
                ?.apply {
                    val javaField = this.javaField!!
                    javaField.isAccessible = true
                    val entityField = EntityField(
                        name = fieldName,
                        nullable = this.returnType.isMarkedNullable,
                        lazy = this.isLateinit,
                        field = javaField,
                        autoFill = javaField.getAnnotation(AutoFillField::class.java)
                    )
                    columnName2Field.put(columnName, entityField)
                    fieldName2Field.put(fieldName, entityField)
                }
        })
    }

    fun <E> getEntityValueByFieldName(e: E, fieldName: String): Any? {
        val entityField = fieldName2Field[fieldName]
            ?: throw InnerException("field ${entityClass!!.qualifiedName}.$fieldName does not exist")
        return entityField.field.get(e)
    }

    fun <E> getEntityValueByColumnName(e: E, columnName: String): Any? {
        val entityField = columnName2Field[columnName]
            ?: throw InnerException("column $schema.$tableName.$columnName does not exist")
        return entityField.field.get(e)
    }

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): E {
        TODO("Not yet implemented")
    }
}