package com.github.afezeria.hymn.oss.module.entity

import com.github.afezeria.hymn.common.db.AbstractEntity
import com.github.afezeria.hymn.common.db.AutoFill
import com.github.afezeria.hymn.common.db.AutoFillType
import com.github.afezeria.hymn.common.platform.ResourceInfo
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 * 文件存储列表
 * @author afezeria
 */
@ApiModel(value = "文件存储列表", description = """文件存储列表""")
data class FileRecord(

    @ApiModelProperty(value = "bucket 名称", required = true)
    var bucket: String,
    @ApiModelProperty(value = "文件名", required = true)
    var fileName: String,
    @ApiModelProperty(value = "")
    var contentType: String? = null,
    @ApiModelProperty(value = "包含文件名的完整路径", required = true)
    var path: String,
    @ApiModelProperty(value = "所属自定义对象id")
    var objectId: String? = null,
    @ApiModelProperty(value = "所属自定义对像中的字段的id")
    var fieldId: String? = null,
    @ApiModelProperty(value = "所属数据id")
    var dataId: String? = null,
    @ApiModelProperty(value = "文件大小", required = true)
    var size: Int = -1,
    @ApiModelProperty(value = "是否为临时文件")
    var tmp: Boolean? = null,
    @ApiModelProperty(value = "可见性，为 null 时根据 data_id,field_id,data_id 判断数据权限，如果这三个字段也为null则只有管理员可见 ;; optional_value:[anonymous(无限制),normal(用户)]")
    var visibility: String? = null,
    @ApiModelProperty(value = "")
    var remark: String? = null,
) : AbstractEntity() {

    @field:AutoFill(type = AutoFillType.ACCOUNT_ID)
    lateinit var createById: String

    @field:AutoFill(type = AutoFillType.ACCOUNT_NAME)
    lateinit var createBy: String

    @field:AutoFill(fillOnUpdate = true, type = AutoFillType.ACCOUNT_ID)
    lateinit var modifyById: String

    @field:AutoFill(fillOnUpdate = true, type = AutoFillType.ACCOUNT_NAME)
    lateinit var modifyBy: String

    @field:AutoFill(type = AutoFillType.DATETIME)
    lateinit var createDate: LocalDateTime

    @field:AutoFill(fillOnUpdate = true, type = AutoFillType.DATETIME)
    lateinit var modifyDate: LocalDateTime

    fun toObjectInfo(): ResourceInfo {
        return ResourceInfo(
            recordId = id,
            bucket = bucket,
            fileName = fileName,
            contentType = contentType,
            path = path,
            objectId = objectId,
            fieldId = fieldId,
            dataId = dataId,
            size = size,
            tmp = tmp,
            visibility = visibility,
            remark = remark,
            createById = createById,
            createDate = createDate,
        )
    }
}