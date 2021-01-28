package github.afezeria.hymn.oss

import github.afezeria.hymn.common.platform.ConfigService
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.platform.OssService
import github.afezeria.hymn.common.platform.PermService
import github.afezeria.hymn.oss.OssType.*
import github.afezeria.hymn.oss.ftp.FTPOssService
import github.afezeria.hymn.oss.local.LocalOssService
import github.afezeria.hymn.oss.minio.MinioOssService
import github.afezeria.hymn.oss.module.service.FileRecordService
import github.afezeria.hymn.oss.module.service.PreSignedHistoryService
import github.afezeria.hymn.oss.platform.OssServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.data.redis.core.RedisTemplate

/**
 * @author afezeria
 */
@Configuration
class OssConfig {

    @Autowired
    lateinit var configService: ConfigService

    @Autowired
    lateinit var fileRecordService: FileRecordService

    @Autowired
    lateinit var preSignedHistoryService: PreSignedHistoryService

    @Autowired
    lateinit var dataBaseService: DatabaseService

    @Autowired
    lateinit var storageService: StorageService

    @Autowired
    lateinit var permService: PermService

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, String>

    lateinit var config: OssConfigProperties

    @Bean
    fun fileService(): StorageService {
        config = configService.get<OssConfigProperties>("oss")
            ?: OssConfigProperties()
        return when (config.type) {
            LOCAL -> LocalOssService(config.local)
            FTP -> FTPOssService(requireNotNull(config.ftp))
            MINIO -> MinioOssService(requireNotNull(config.minio))
        }
    }

    @Bean
    @DependsOn("fileService")
    fun ossService(): OssService {
        return OssServiceImpl(
            prefix = config.prefix,
            fileRecordService = fileRecordService,
            preSignedHistoryService = preSignedHistoryService,
            dataBaseService = dataBaseService,
            storageService = storageService,
            permService = permService,
            redisTemplate = RedisTemplate()
        )
    }
}