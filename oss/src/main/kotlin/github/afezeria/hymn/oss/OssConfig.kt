package github.afezeria.hymn.oss

import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.OssService
import github.afezeria.hymn.common.platform.PermService
import github.afezeria.hymn.oss.OssType.*
import github.afezeria.hymn.oss.ftp.FTPOssService
import github.afezeria.hymn.oss.local.LocalOssService
import github.afezeria.hymn.oss.minio.MinioOssService
import github.afezeria.hymn.oss.module.service.FileRecordService
import github.afezeria.hymn.oss.module.service.PreSignedHistoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author afezeria
 */
@Configuration
@EnableConfigurationProperties(OssConfigProperties::class)
class OssConfig {

    @Autowired
    lateinit var config: OssConfigProperties

    @Autowired
    lateinit var fileRecordService: FileRecordService

    @Autowired
    lateinit var preSignedHistoryService: PreSignedHistoryService

    @Autowired
    lateinit var dataBaseService: DataBaseService

    @Autowired
    lateinit var fileService: FileService

    @Autowired
    lateinit var permService: PermService

    @Bean
    fun fileService(): FileService {
        return when (config.type) {
            LOCAL -> LocalOssService(config.local)
            FTP -> FTPOssService(config.ftp)
            MINIO -> MinioOssService(config.minio)
        }
    }

    @Bean
    fun ossService(): OssService {
        return OssServiceImpl(
            prefix = config.prefix,
            fileRecordService = fileRecordService,
            preSignedHistoryService = preSignedHistoryService,
            dataBaseService = dataBaseService,
            fileService = fileService,
            permService = permService,

            )

    }
}