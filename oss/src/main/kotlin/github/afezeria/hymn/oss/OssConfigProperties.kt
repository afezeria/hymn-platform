package github.afezeria.hymn.oss

import github.afezeria.hymn.oss.ftp.FTPConfig
import github.afezeria.hymn.oss.local.LocalConfig
import github.afezeria.hymn.oss.minio.MinioConfig
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

/**
 * @author afezeria
 */
@ConfigurationProperties("hymn.oss")
class OssConfigProperties {
    var type: OssType = OssType.LOCAL
    var prefix: String = ""

    @NestedConfigurationProperty
    var ftp = FTPConfig()

    @NestedConfigurationProperty
    var local = LocalConfig()

    @NestedConfigurationProperty
    var minio = MinioConfig()
}