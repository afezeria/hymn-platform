package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.BizObject
import java.time.LocalDateTime
import io.swagger.annotations.*
import java.util.*

/**
 * @author afezeria
 */
class BizObjectDto(
    @ApiModelProperty(value = "业务对象名称，用于页面显示")
    var name: String,
    @ApiModelProperty(value = "业务对象api，唯一标识 ;;uk")
    var api: String,
    @ApiModelProperty(value = "实际表名，例： core_data_table_500", required = true)
    var sourceTable: String? = null,
    @ApiModelProperty(value = "是否启用，停用后无法进行增删改查等操作")
    var active: Boolean,
    @ApiModelProperty(value = "对象类型, 模块对象不能在系统后台进行新增删除，底层表单和相关数据需要手动创建，外部对象没有底层表，通过url调用外部接口，只能在应用层脚本中使用 ;; optional_value:[custom(自定义对象),module(模块对象),remote(远程对象)]")
    var type: String,
    @ApiModelProperty(value = "远程rest接口地址，系统通过该地址调用远程数据", required = true)
    var remoteUrl: String? = null,
    @ApiModelProperty(value = "远程rest验证信息", required = true)
    var remoteToken: String? = null,
    @ApiModelProperty(value = "模块api，所有自定义对象该字段都为null，不为null表示该对象属于指定模块，通过添加模块对象的 core_biz_object 和 core_biz_object_field 数据来支持在触发器中使用DataService提供的通用操作 ;;fk:[core_module cascade]", required = true)
    var moduleApi: String? = null,
    @ApiModelProperty(value = "")
    var remark: String,
    @ApiModelProperty(value = "模块对象及远程对象是否可以新增数据", required = true)
    var canInsert: Boolean? = null,
    @ApiModelProperty(value = "模块对象是及远程对象否可以删除数据", required = true)
    var canUpdate: Boolean? = null,
    @ApiModelProperty(value = "", required = true)
    var canDelete: Boolean? = null,
){
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

    fun fromEntity(entity: BizObject): BizObjectDto {
        return entity.run {
            BizObjectDto(
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
