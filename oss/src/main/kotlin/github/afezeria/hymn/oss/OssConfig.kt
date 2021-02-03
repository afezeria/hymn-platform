package github.afezeria.hymn.oss

import github.afezeria.hymn.common.platform.ConfigService
import github.afezeria.hymn.oss.OssType.*
import github.afezeria.hymn.oss.ftp.FTPOssService
import github.afezeria.hymn.oss.local.LocalOssService
import github.afezeria.hymn.oss.minio.MinioOssService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author afezeria
 */
@Configuration
class OssConfig {

    @Autowired
    lateinit var configService: ConfigService

    @Bean
    fun ossConfigProperties(): OssConfigProperties {
        val config = configService.get<OssConfigProperties>("oss")
            ?: OssConfigProperties()
        return config
    }


    @Bean
    fun fileService(config: OssConfigProperties): StorageService {
        return when (config.type) {
            LOCAL -> LocalOssService(config.local)
            FTP -> FTPOssService(requireNotNull(config.ftp))
            MINIO -> MinioOssService(requireNotNull(config.minio))
        }
    }

}