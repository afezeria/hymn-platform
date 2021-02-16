package github.afezeria.hymn.core.module.entity

import github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 业务代码引用关系表
 * @author afezeria
 */
@ApiModel(value = "业务代码引用关系表", description = """业务代码引用关系表""")
data class BusinessCodeRef(

    @ApiModelProperty(value = "触发器id ;;fk:[core_biz_object_trigger cascade]")
    var triggerId: String? = null,
    @ApiModelProperty(value = "接口id ;;fk:[core_custom_interface cascade]")
    var interfaceId: String? = null,
    @ApiModelProperty(value = "共享代码id ;;fk:[core_shared_code cascade]")
    var sharedCodeId: String? = null,
    @ApiModelProperty(value = "被引用对象id ;;fk:[core_biz_object cascade]")
    var bizObjectId: String? = null,
    @ApiModelProperty(value = "被引用字段id ;;fk:[core_biz_object_field cascade];idx")
    var fieldId: String? = null,
    @ApiModelProperty(value = "被引用组织id ;;fk:[core_org cascade];idx")
    var orgId: String? = null,
    @ApiModelProperty(value = "被引用角色id ;;fk:[core_role cascade];idx")
    var roleId: String? = null,
    @ApiModelProperty(value = "被引用共享代码id ;;fk:[core_shared_code cascade];idx")
    var refSharedCodeId: String? = null,
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
