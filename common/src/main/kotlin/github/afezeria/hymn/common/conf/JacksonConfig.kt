package github.afezeria.hymn.common.conf

import com.fasterxml.jackson.databind.ObjectMapper
import github.afezeria.hymn.common.util.mapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

/**
 * @author afezeria
 */
@Configuration
class JacksonConfig {
    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        return mapper
    }
}