package com.github.afezeria.hymn.common.conf

import com.github.afezeria.hymn.common.ann.ApiVersion
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.web.servlet.mvc.condition.RequestCondition
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import java.lang.reflect.Method


/**
 * @author afezeria
 */
class ApiRequestMappingHandlerMapping : RequestMappingHandlerMapping() {


    override fun getCustomTypeCondition(handlerType: Class<*>): RequestCondition<ApiVersionCondition>? {
        val apiVersion: ApiVersion? =
            AnnotationUtils.findAnnotation(handlerType, ApiVersion::class.java)
        return createCondition(apiVersion)
    }

    override fun getCustomMethodCondition(method: Method): RequestCondition<ApiVersionCondition>? {
        val apiVersion: ApiVersion? = AnnotationUtils.findAnnotation(method, ApiVersion::class.java)
        return createCondition(apiVersion)
    }

    private fun createCondition(apiVersion: ApiVersion?): RequestCondition<ApiVersionCondition>? {
        return if (apiVersion == null) null else ApiVersionCondition(apiVersion.value)
    }

}