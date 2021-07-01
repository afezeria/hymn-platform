package com.github.afezeria.hymn.common.util

import com.github.afezeria.hymn.common.exception.DtoConvertException
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.sql.ResultSet
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaConstructor
import kotlin.reflect.jvm.javaSetter

/**
 * 映射函数生成器
 * 生成将Map/ResultSet映射到dto的函数
 * @date 2021/7/1
 */
abstract class DtoMappers {

    /**
     * 生成将[ResultSet]转换到dto的函数
     */
    protected fun <T : Any> rsMapper(
        kClass: KClass<T>,
        vararg rules: Pair<String, KMutableProperty1<*, *>>
    ): (ResultSet) -> T {
        val (dtoClassName, constructor, params, setters) = getMapperInfo(kClass, *rules)

        return { rs ->
            val array = Array(params.size) {
                val (name, optional) = params[it]
                val value = rs.getObject(name)
                if (!optional && value == null) {
                    throw DtoConvertException("the map must contain the key \"$name\" and the value cannot be null when converting to $dtoClassName class")
                }
                value
            }
            val instance = constructor.newInstance(*array)
            for ((name, setter) in setters) {
                rs.getObject(name)?.let {
                    setter.invoke(instance, it)
                }
            }
            instance
        }
    }

    /**
     * 生成将[Map]转换到dto的函数
     */
    protected fun <T : Any> mapper(
        kClass: KClass<T>,
        vararg rules: Pair<String, KMutableProperty1<*, *>>
    ): (Map<String, *>) -> T {
        val (dtoClassName, constructor, params, setters) = getMapperInfo(kClass, *rules)
        return { map ->
            val array = Array(params.size) {
                val (name, optional) = params[it]
                val value = map[name]
                if (!optional && value == null) {
                    throw DtoConvertException("the map must contain the key \"$name\" and the value cannot be null when converting to $dtoClassName class")
                }
                value
            }
            val instance = constructor.newInstance(*array)
            for ((name, setter) in setters) {
                map[name]?.let {
                    setter.invoke(instance, it)
                }
            }
            instance
        }
    }

    private fun <T : Any> getMapperInfo(
        kClass: KClass<T>,
        vararg rules: Pair<String, KMutableProperty1<*, *>>
    ): MapperInfo<T> {
        if (kClass.isData.not()) {
            throw IllegalArgumentException("the generic type T must be data class")
        }
        val dtoClassName = requireNotNull(kClass.qualifiedName)
        val constructor = requireNotNull(kClass.primaryConstructor!!.javaConstructor)
        val constructorParameters = mutableListOf<ConstructorParam>()
        val constructorParameterNameSet = mutableSetOf<String>()
        val setters = mutableListOf<SourceName2Setter>()
        for (parameter in kClass.primaryConstructor!!.parameters) {
            val name = parameter.name!!.snakeCase()
            constructorParameters.add(ConstructorParam(name, parameter.isOptional))
            constructorParameterNameSet.add(name)
        }
        for (property in kClass.declaredMemberProperties) {
            if (property is KMutableProperty1
                && !constructorParameterNameSet.contains(property.name)
            ) {
                setters.add(SourceName2Setter(property.name.snakeCase(), property.javaSetter!!))
            }
        }
        for ((alias, property) in rules) {
            val original = property.name.snakeCase()
            if (property.javaSetter?.declaringClass != kClass.java) {
                throw IllegalArgumentException("property must belong to $dtoClassName class")
            }
            if (constructorParameterNameSet.contains(original)) {
                constructorParameters.forEachIndexed { index, constructorParam ->
                    if (constructorParam.name == original) {
                        constructorParameters[index] = constructorParam.copy(name = alias)
                    }
                }
            } else {
                setters.forEachIndexed { index, pair ->
                    if (pair.name == original) {
                        setters[index] = pair.copy(name = alias)
                    }
                }
            }
        }
        return MapperInfo(
            dtoClassName,
            constructor,
            constructorParameters,
            setters
        )
    }
}

private data class MapperInfo<T>(
    val dtoClassName: String,
    val constructor: Constructor<T>,
    val params: List<ConstructorParam>,
    val setters: List<SourceName2Setter>,
)

private data class ConstructorParam(val name: String, val optional: Boolean)
private data class SourceName2Setter(val name: String, val setter: Method)
