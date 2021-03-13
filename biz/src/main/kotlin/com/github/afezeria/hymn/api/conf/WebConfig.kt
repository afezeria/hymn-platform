package com.github.afezeria.hymn.api.conf

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author afezeria
 */
@Configuration
class WebConfig : WebMvcConfigurer {
    /**
     * 自定义页面代码存放在应用工作目录下的的static-resource目录下
     * 压缩文档存放在工作目录下的archive目录中
     * 启动时检查数据库core_custom_page表中的项和md5值是否和archive目录中的压缩文件一致
     * 不一致时从文件服务器下载压缩文件
     */
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val path = Paths.get("${System.getProperty("user.dir")}/static-resource")
        if (!Files.exists(path)) {
            Files.createDirectory(path)
        }
        val location = "file:${path}/"
        registry.addResourceHandler("/api/public/custom/**")
            .addResourceLocations(location)
    }

}