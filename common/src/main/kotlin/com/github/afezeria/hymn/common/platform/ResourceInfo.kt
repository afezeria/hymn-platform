package com.github.afezeria.hymn.common.platform

import java.time.LocalDateTime

/**
 * @author afezeria
 */
data class ResourceInfo(
    val recordId: String,
    val bucket: String,
    val fileName: String,
    val contentType: String? = null,
    val path: String,
    val objectId: String? = null,
    val fieldId: String? = null,
    val dataId: String? = null,
    val size: Int,
    val tmp: Boolean? = null,
    val visibility: String? = null,
    val remark: String? = null,
    val createById: String,
    val createDate: LocalDateTime,
)