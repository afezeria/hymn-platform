package github.afezeria.hymn.oss.module.dto

import github.afezeria.hymn.oss.module.entity.PreSignedHistory
import java.time.LocalDateTime
import io.swagger.annotations.*
import java.util.*

/**
 * @author afezeria
 */
class PreSignedHistoryDto(
    @ApiModelProperty(value = "文件id ;; idx", required = true)
    var fileId: String,
    @ApiModelProperty(value = "有效时间，单位：秒", required = true)
    var expiry: Int,
){
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
