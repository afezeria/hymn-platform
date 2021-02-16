package github.afezeria.hymn.core.module.entity

import github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 业务对象
 * @author afezeria
 */
@ApiModel(value = "业务对象", description = """业务对象""")
data class BizObject(

    @ApiModelProperty(value = "业务对象名称，用于页面显示", required = true)
    var name: String,
    @ApiModelProperty(value = "业务对象api，唯一标识 ;;uk", required = true)
    var api: String,
    @ApiModelProperty(value = "实际表名，例： core_data_table_500")
    var sourceTable: String? = null,
    @ApiModelProperty(value = "是否启用，停用后无法进行增删改查等操作", required = true)
    var active: Boolean,
    @ApiModelProperty(
        value = "对象类型, 模块对象不能在系统后台进行新增删除，底层表单和相关数据需要手动创建，外部对象没有底层表，通过url调用外部接口，只能在应用层脚本中使用 ;; optional_value:[custom(自定义对象),module(模块对象),remote(远程对象)]",
        required = true
    )
    var type: String,
    @ApiModelProperty(value = "远程rest接口地址，系统通过该地址调用远程数据")
    var remoteUrl: String? = null,
    @ApiModelProperty(value = "远程rest验证信息")
    var remoteToken: String? = null,
    @ApiModelProperty(
        value = "模块api，所有自定义对象该字段都为null，不为null表示该对象属于指定模块，通过添加模块对象的 core_biz_object 和 core_biz_object_field 数据来支持在触发器中使用DataService提供的通用操作 ;;fk:[core_module cascade]"
    )
    var moduleApi: String? = null,
    @ApiModelProperty(value = "", required = true)
    var remark: String,
    @ApiModelProperty(value = "模块对象及远程对象是否可以新增数据")
    var canInsert: Boolean? = null,
    @ApiModelProperty(value = "模块对象是及远程对象否可以更新数据")
    var canUpdate: Boolean? = null,
    @ApiModelProperty(value = "模块对象是及远程对象否可以删除数据")
    var canDelete: Boolean? = null,
    @ApiModelProperty(value = "是否支持软删除，用于标记模块对象的删除行为，模块对象表中有bool类型的deleted字段作为删除标记时 can_soft_delete 可以为true，当 can_soft_delete 为 true 时，dataService的数据删除动作为设置 deleted 的值为 true，当 can_soft_delete 为 false 时，数据删除时直接从表中删除数据")
    var canSoftDelete: Boolean? = null,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
