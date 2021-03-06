package com.github.afezeria.hymn.oss.module.dto

import com.github.afezeria.hymn.oss.module.entity.PreSignedHistory
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class PreSignedHistoryDto(
    @ApiModelProperty(value = "文件id ;; idx", required = true)
    var fileId: String,
    @ApiModelProperty(value = "有效时间，单位：秒", required = true)
    var expiry: Int,
) {
    fun toEntity(): PreSignedHistory {
        return PreSignedHistory(
            fileId = fileId,
            expiry = expiry,
        )
    }

    fun update(entity: PreSignedHistory) {
        entity.also {
            it.fileId = fileId
            it.expiry = expiry
        }
    }
}
