package github.afezeria.hymn.oss

import com.fasterxml.jackson.module.kotlin.readValue
import github.afezeria.hymn.common.platform.ConfigService
import github.afezeria.hymn.common.platform.OssService
import github.afezeria.hymn.common.util.mapper
import github.afezeria.hymn.oss.ftp.FTPOssService
import github.afezeria.hymn.oss.local.LocalOssService
import github.afezeria.hymn.oss.minio.MinioOssService
import github.afezeria.hymn.oss.web.controller.SimpleFileController
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

    @Autowired
    lateinit var fileController: SimpleFileController

    @Bean
    fun bea(): OssService {
        val ossStr = configService.get("oss")
        return if (ossStr != null) {
            mapper.readValue<Config>(ossStr).run {
                when (type) {
                    StorageType.LOCAL -> LocalOssService(
                        fileController,
                        mapper.readValue(data),
                    )
                    StorageType.FTP -> FTPOssService(mapper.readValue(data), fileController)
                    StorageType.MINIO -> MinioOssService(mapper.readValue(data),fileController)
                }
            }
        } else {
            LocalOssService(fileController)
        }
    }
}