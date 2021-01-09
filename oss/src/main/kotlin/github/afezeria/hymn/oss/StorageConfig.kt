package github.afezeria.hymn.oss

import com.fasterxml.jackson.module.kotlin.readValue
import github.afezeria.hymn.common.platform.ConfigService
import github.afezeria.hymn.common.platform.StorageService
import github.afezeria.hymn.common.util.mapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author afezeria
 */
@Configuration
class StorageConfig {
    @Autowired
    lateinit var configService: ConfigService

    @Bean
    fun bea(): StorageService {
        val ossStr = configService.get("oss")
        return if (ossStr != null) {
            mapper.readValue<Config>(ossStr).run {
                when (type) {
                    StorageType.LOCAL -> LocalStorageService(data)
                    StorageType.MINIO -> MinioStorageService(data)
                }
            }
        } else {
            LocalStorageService()
        }
    }
}