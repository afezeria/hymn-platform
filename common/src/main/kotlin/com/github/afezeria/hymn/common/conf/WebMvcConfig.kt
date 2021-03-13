package com.github.afezeria.hymn.common.conf

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping


/**
 * @author afezeria
 */
@Configuration
class WebMvcConfig : WebMvcRegistrations {
    override fun getRequestMappingHandlerMapping(): RequestMappingHandlerMapping {
        return ApiRequestMappingHandlerMapping()
    }
}