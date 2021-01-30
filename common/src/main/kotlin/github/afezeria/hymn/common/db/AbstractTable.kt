package github.afezeria.hymn.common.db

import github.afezeria.hymn.common.exception.InnerException
import github.afezeria.hymn.common.util.lCamelize
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.Column
import org.ktorm.schema.varchar
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
    history: Boolean = false,
    entityClass: KClass<E>? = null,
) : BaseTable<E>(tableName, alias, catalog, schema, entityClass) {

    inner class InnerMap(val fn: (String, Column<*>) -> Any?) : LinkedHashMap<String, Column<*>>() {
        override fun put(key: String, value: Column<*>): Column<*>? {
            fn(key, value)
            return super.put(key, value)
        }
    }

    val id = varchar("id")
    private val _history = history


    private val columnName2Field: MutableMap<String, EntityField> = mutableMapOf()
    private val fieldName2Field: MutableMap<String, EntityField> = mutableMapOf()
    private val entityFieldList: MutableList<EntityField> = mutableListOf()
    private val fieldName2Column: MutableMap<String, Column<*>> = mutableMapOf()
    private val entityParameterlessConstructor: Constructor<E>

    init {
        val columns = BaseTable::class.java.getDeclaredField("_columns")
        columns.isAccessible = true
        val kClass = this.entityClass ?: throw RuntimeException("无法获取表 $tableName 的实体类的Class实例")
        entityParameterlessConstructor = kClass.java.getConstructor()
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

    val fieldCount: Int
        get() = entityFieldList.size

    fun hasHistory(): Boolean {
        return _history
    }

    fun getColumnByFieldName(fieldName: String): Column<*>? {
        return fieldName2Column[fieldName]
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

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): E {
        val newInstance = entityParameterlessConstructor.newInstance()
        for (entityField in fieldName2Field.values) {
            if (entityField.nullable) {
                entityField.field.set(newInstance, row[entityField.column])
            } else {
                val value = row[entityField.column]
                    ?: throw IllegalArgumentException(
                        "field ${entityClass!!.qualifiedName}.${entityField.field.name} of data with id ${
                            row.getString(
                                "id"
                            )
                        }  should not be null"
                    )
                entityField.field.set(newInstance, value)
            }
        }
        return newInstance
    }
}