package github.afezeria.hymn.oss.module.dto

import github.afezeria.hymn.oss.module.entity.FileRecord
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class FileRecordDto(
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
) {
    fun toEntity(): FileRecord {
        return FileRecord(
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
        )
    }

    fun update(entity: FileRecord) {
        entity.also {
            it.bucket = bucket
            it.fileName = fileName
            it.contentType = contentType
            it.path = path
            it.objectId = objectId
            it.fieldId = fieldId
            it.dataId = dataId
            it.size = size
            it.tmp = tmp
            it.visibility = visibility
            it.remark = remark
        }
    }
}
