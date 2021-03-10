package github.afezeria.hymn.core.module.entity

import github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 业务对象详情页面布局
 * @author afezeria
 */
@ApiModel(value = "业务对象详情页面布局", description = """业务对象详情页面布局 """)
data class BizObjectLayout(

    @ApiModelProperty(value = "引用对象 ")
    var bizObjectId: String,
    @ApiModelProperty(value = "布局名称")
    var name: String,
    @ApiModelProperty(value = "")
    var remark: String? = null,
    @ApiModelProperty(value = "布局中使用的对象/字段/按钮列表，后端用于根据权限对字段进行过滤，布局json中相关位置使用id占位，不保存名称、api、函数等信息，渲染页面时根据id在components中查找,找不到的场合下忽略该字段")
    var componentJson: String,
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
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
