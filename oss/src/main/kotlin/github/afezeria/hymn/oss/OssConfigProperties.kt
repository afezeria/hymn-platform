package github.afezeria.hymn.oss

import github.afezeria.hymn.oss.ftp.FTPConfig
import github.afezeria.hymn.oss.local.LocalConfig
import github.afezeria.hymn.oss.minio.MinioConfig

/**
 * @author afezeria
 */
data class OssConfigProperties(
    var type: OssType = OssType.LOCAL,
    var prefix: String = "",
    var ftp: FTPConfig? = null,
    var local: LocalConfig? = null,
    var minio: MinioConfig? = null,
)