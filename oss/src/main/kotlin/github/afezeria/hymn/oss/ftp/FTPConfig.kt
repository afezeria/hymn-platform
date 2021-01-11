package github.afezeria.hymn.oss.ftp

/**
 * ftp配置
 * @param host 主机ip
 * @param port 端口
 * @param username 用户名
 * @param password 密码
 * @param connectTimeout 连接超时时间（秒）
 * @param bufferSize 缓冲大小
 * @author afezeria
 */
data class FTPConfig(
    val host: String,
    val port: Int = 21,
    val path: String? = null,
    val username: String,
    val password: String,
    val connectTimeout: Int = 30,
    val bufferSize: Int = 8 * 1024,
)