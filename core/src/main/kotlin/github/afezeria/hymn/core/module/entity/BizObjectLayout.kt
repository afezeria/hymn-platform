package github.afezeria.hymn.core.module.entity

import java.time.LocalDateTime
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 *
 * 业务对象详情页面布局 ;;uk:[[biz_object_id name]]
 * @author afezeria
 */
@ApiModel(value="业务对象详情页面布局",description = """业务对象详情页面布局 ;;uk:[[biz_object_id name]]""")
data class BizObjectLayout(

    @ApiModelProperty(value = "引用对象 ;;fk:[core_biz_object cascade]")
    var bizObjectId: String,
    @ApiModelProperty(value = "布局名称")
    var name: String,
    @ApiModelProperty(value = "", required = true)
    var remark: String? = null,
    @ApiModelProperty(value = "引用字段的数据的列表，用于根据权限对字段进行过滤，布局json中不能直接使用字段数据，在需要字段数据的部分通过rel_field_json_arr中的json对象的_id引用，找不到的场合下忽略该字段")
    var relFieldJsonArr: String,
    @ApiModelProperty(value = "pc端查看页面页面布局")
    var pcReadLayoutJson: String,
    @ApiModelProperty(value = "pc端新建、编辑页面页面布局")
    var pcEditLayoutJson: String,
    @ApiModelProperty(value = "移动端查看页面页面布局")
    var mobileReadLayoutJson: String,
    @ApiModelProperty(value = "移动端新建、编辑页面页面布局")
    var mobileEditLayoutJson: String,
    @ApiModelProperty(value = "小窗预览界面布局")
    var previewLayoutJson: String,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
