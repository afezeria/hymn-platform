package com.github.afezeria.hymn.core.module.dto

import com.github.afezeria.hymn.core.module.entity.BizObject
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BizObjectDto(
    @ApiModelProperty(value = "业务对象名称，用于页面显示", required = true)
    var name: String,
    @ApiModelProperty(value = "业务对象api，唯一标识", required = true)
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
        value = "模块api，所有自定义对象该字段都为null，不为null表示该对象属于指定模块，通过添加模块对象的 core_biz_object 和 core_biz_object_field 数据来支持在触发器中使用DataService提供的通用操作"
    )
    var moduleApi: String? = null,
    @ApiModelProperty(value = "", required = true)
    var remark: String,
    @ApiModelProperty(value = "模块对象及远程对象是否可以新增数据")
    var canInsert: Boolean? = null,
    @ApiModelProperty(value = "模块对象是及远程对象否可以删除数据")
    var canUpdate: Boolean? = null,
    @ApiModelProperty(value = "")
    var canDelete: Boolean? = null,
    @ApiModelProperty(value = "name字段名称", required = true)
    var fieldName: String,
    @ApiModelProperty(value = "自动编号规则，name字段为文本类型时为空")
    var autoRule: String? = null
) {
    fun toEntity(): BizObject {
        return BizObject(
            name = name,
            api = api,
            sourceTable = sourceTable,
            active = active,
            type = type,
            remoteUrl = remoteUrl,
            remoteToken = remoteToken,
            moduleApi = moduleApi,
            remark = remark,
            canInsert = canInsert,
            canUpdate = canUpdate,
            canDelete = canDelete,
        )
    }

    fun update(entity: BizObject) {
        entity.also {
            it.name = name
            it.api = api
            it.sourceTable = sourceTable
            it.active = active
            it.type = type
            it.remoteUrl = remoteUrl
            it.remoteToken = remoteToken
            it.moduleApi = moduleApi
            it.remark = remark
            it.canInsert = canInsert
            it.canUpdate = canUpdate
            it.canDelete = canDelete
        }
    }
}
