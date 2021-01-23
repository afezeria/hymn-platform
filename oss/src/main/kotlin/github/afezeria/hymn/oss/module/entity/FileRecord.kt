package github.afezeria.hymn.oss.module.entity

import java.time.LocalDateTime
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 * 文件存储列表
 * @author afezeria
 */
@ApiModel(value="文件存储列表",description = """文件存储列表 ;; uk:[[object_id field_id data_id file_name]]""")
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
    @ApiModelProperty(value = "文件大小")
    var size: Int? = null,
    @ApiModelProperty(value = "")
    var remark: String? = null,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
