package com.github.afezeria.hymn.oss.module.entity

import com.github.afezeria.hymn.common.db.AbstractEntity
import com.github.afezeria.hymn.common.db.AutoFill
import com.github.afezeria.hymn.common.db.AutoFillType
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 * 创建文件预签url记录，预签url不需要任何权限验证
 * @author afezeria
 */
@ApiModel(value = "创建文件预签url记录，预签url不需要任何权限验证", description = """创建文件预签url记录，预签url不需要任何权限验证""")
data class PreSignedHistory(

    @ApiModelProperty(value = "文件id ;; idx", required = true)
    var fileId: String,
    @ApiModelProperty(value = "有效时间，单位：秒", required = true)
    var expiry: Int,
) : AbstractEntity() {
    @field:AutoFill(type = AutoFillType.ACCOUNT_ID)
    lateinit var createById: String

    @field:AutoFill(type = AutoFillType.ACCOUNT_NAME)
    lateinit var createBy: String


    @field:AutoFill(type = AutoFillType.DATETIME)
    lateinit var createDate: LocalDateTime
}
