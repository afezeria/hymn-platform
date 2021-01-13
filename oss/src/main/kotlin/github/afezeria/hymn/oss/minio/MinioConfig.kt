package github.afezeria.hymn.oss.minio

/**
 * @author afezeria
 */
data class MinioConfig(
    val url: String,
    val accessKey: String,
    val secretKey: String,
    val prefix: String? = null,
    val useMinioPreSignedURL: Boolean = false,
)