package github.afezeria.hymn.common.db

import github.afezeria.hymn.common.exception.InnerException
import github.afezeria.hymn.common.util.lCamelize
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.Column
import org.ktorm.schema.varchar
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField

/**
 * @author afezeria
 */
abstract class AbstractTable<E : AbstractEntity>(
    tableName: String,
    alias: String? = null,
    catalog: String? = null,
    schema: String? = null,
    history: Boolean = false,
    entityClass: KClass<E>? = null,
) : BaseTable<E>(tableName, alias, catalog, schema, entityClass) {

    inner class InnerMap(val fn: (String, Column<*>) -> Any?) : LinkedHashMap<String, Column<*>>() {
        override fun put(key: String, value: Column<*>): Column<*>? {
            fn(key, value)
            return super.put(key, value)
        }
    }

    private val _history = history


    private val columnName2Field: MutableMap<String, EntityField> = mutableMapOf()
    private val fieldName2Field: MutableMap<String, EntityField> = mutableMapOf()
    private val entityFieldList: MutableList<EntityField> = mutableListOf()
    private val fieldName2Column: MutableMap<String, Column<*>> = mutableMapOf()

    private val primaryConstructor: KFunction<E>

    init {
        val columns = BaseTable::class.java.getDeclaredField("_columns")
        columns.isAccessible = true
        val kClass = this.entityClass ?: throw RuntimeException("无法获取表 $tableName 的实体类的Class实例")
        primaryConstructor =
            kClass.primaryConstructor
                ?: throw RuntimeException("无法获取类 ${kClass.qualifiedName} 的主构造器")
        columns.set(this, InnerMap { columnName, column ->
            val fieldName = columnName.lCamelize()
            kClass.memberProperties
                .find { it.name == fieldName }
                ?.apply {
                    val javaField = this.javaField!!
                    javaField.isAccessible = true
                    val entityField = EntityField(
                        field = javaField,
                        column = column,
                        mutable = this is KMutableProperty1,
                        nullable = this.returnType.isMarkedNullable,
                        lazy = this.isLateinit,
                        autoFill = javaField.getAnnotation(AutoFill::class.java),
                    )
                    columnName2Field[columnName] = entityField
                    fieldName2Field[fieldName] = entityField
                    fieldName2Column[fieldName] = column
                    entityFieldList.add(entityField)
                }
        })
    }

    val id = varchar("id").primaryKey()

    val fieldCount: Int
        get() = entityFieldList.size

    fun hasHistory(): Boolean {
        return _history
    }

    fun containsField(fieldName: String): Boolean {
        return fieldName2Field.containsKey(fieldName)
    }

    fun getEntityFieldByFieldName(fieldName: String): EntityField? {
        return fieldName2Field[fieldName]
    }

    fun getEntityFieldByColumnName(columnName: String): EntityField? {
        return columnName2Field[columnName]
    }

    fun getEntityFieldList(): List<EntityField> {
        return entityFieldList
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

    fun setValueByFieldName(e: E, fieldName: String, value: Any?) {
        val entityField = fieldName2Field[fieldName]
            ?: throw InnerException("field ${entityClass!!.qualifiedName}.$fieldName does not exist")
        if (value == null && !entityField.nullable) {
            throw IllegalArgumentException("field ${entityClass!!.qualifiedName}.$fieldName can not be null")
        }
        return entityField.field.set(e, value)
    }

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): E {
        val args = primaryConstructor.parameters.map {
            val entityField = requireNotNull(fieldName2Field[it.name])
            val value = row[entityField.column]
            if (value == null && !it.isOptional) {
                throw IllegalArgumentException(
                    "field ${entityClass!!.qualifiedName}.${entityField.field.name} of data with id ${
                        row.getString(id.label)
                    }  should not be null"
                )
            }
            value
        }.toTypedArray()
        val instance = primaryConstructor.call(*args)
        for (entityField in entityFieldList.filter { it.lazy }) {
            val value = row[entityField.column]
                ?: throw IllegalArgumentException(
                    "field ${entityClass!!.qualifiedName}.${entityField.field.name} of data with id ${
                        row.getString(id.label)
                    }  should not be null"
                )
            entityField.field.set(instance, value)
        }
        return instance
    }
}