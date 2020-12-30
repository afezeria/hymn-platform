package github.afezeria.hymn.api.conf

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
     */
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val path = Paths.get("${System.getProperty("user.dir")}/static-resource")
        if (!Files.exists(path)) {
            Files.createDirectory(path)
        }
        val location = "file:${path}/"
        registry.addResourceHandler("/public/**")
            .addResourceLocations(location)
    }
}