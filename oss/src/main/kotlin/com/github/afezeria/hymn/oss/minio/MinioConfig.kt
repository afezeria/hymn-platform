package com.github.afezeria.hymn.oss.minio

/**
 * @author afezeria
 */
class MinioConfig {
    lateinit var url: String
    lateinit var accessKey: String
    lateinit var secretKey: String
    var useMinioPreSignedURL: Boolean = false
}