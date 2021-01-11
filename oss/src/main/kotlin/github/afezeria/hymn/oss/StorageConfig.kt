package github.afezeria.hymn.oss

import com.fasterxml.jackson.module.kotlin.readValue
import github.afezeria.hymn.common.platform.ConfigService
import github.afezeria.hymn.common.platform.StorageService
import github.afezeria.hymn.common.util.mapper
import github.afezeria.hymn.oss.ftp.FtpStorageService
import github.afezeria.hymn.oss.local.LocalStorageService
import github.afezeria.hymn.oss.minio.MinioStorageService
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
                    StorageType.LOCAL -> LocalStorageService(mapper.readValue(data))
                    StorageType.FTP -> FtpStorageService(mapper.readValue(data))
                    StorageType.MINIO -> MinioStorageService(mapper.readValue(data))
                }
            }
        } else {
            LocalStorageService()
        }
    }
}