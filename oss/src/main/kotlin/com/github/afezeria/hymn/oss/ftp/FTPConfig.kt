package com.github.afezeria.hymn.oss.ftp

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
class FTPConfig {
    lateinit var host: String
    var port: Int = 21
    var path: String? = null
    lateinit var username: String
    lateinit var password: String
    var connectTimeout: Int = 30
    var bufferSize: Int = 8 * 1024
    var prefix: String? = null
}