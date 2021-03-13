package com.github.afezeria.hymn.common.conf

import org.springframework.web.servlet.mvc.condition.RequestCondition
import javax.servlet.http.HttpServletRequest

/**
 * @author afezeria
 */
class ApiVersionCondition(private val apiVersion: Int) :
    RequestCondition<ApiVersionCondition> {

    override fun getMatchingCondition(request: HttpServletRequest): ApiVersionCondition? {
        return VERSION_REGEX.find(request.requestURI)?.run {
            if (groupValues[1].toInt() >= apiVersion) {
                this@ApiVersionCondition
            } else {
                null
            }
        }
    }

    companion object {
        private val VERSION_REGEX = "^/module/[-a-z]++/api/v(\\d++)/".toRegex()
    }

    override fun combine(other: ApiVersionCondition): ApiVersionCondition {
        return ApiVersionCondition(other.apiVersion)
    }

    override fun compareTo(other: ApiVersionCondition, request: HttpServletRequest): Int {
        return other.apiVersion - apiVersion
    }
}
