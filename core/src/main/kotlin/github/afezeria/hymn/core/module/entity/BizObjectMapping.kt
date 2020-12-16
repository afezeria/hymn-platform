package github.afezeria.hymn.core.module.entity

import java.time.LocalDateTime
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 *
 * 对象映射关系 描述以一个对象的数据为基础新建其他对象的数据时字段间的映射关系，比如根据订单创建发货单时将订单中的字段映射到发货单中 ;;uk[[source_biz_object_id source_type_id target_biz_object_id target_type_id]]
 * @author afezeria
 */
@ApiModel(value="对象映射关系",description = """对象映射关系 描述以一个对象的数据为基础新建其他对象的数据时字段间的映射关系，比如根据订单创建发货单时将订单中的字段映射到发货单中 ;;uk[[source_biz_object_id source_type_id target_biz_object_id target_type_id]]""")
data class BizObjectMapping(

    @ApiModelProperty(value = "源对象id ;;fk:[core_biz_object cascade];idx")
    var sourceBizObjectId: String,
    @ApiModelProperty(value = "源对象记录类型id ;;fk:[core_biz_object_type cascade]")
    var sourceTypeId: String,
    @ApiModelProperty(value = "目标对象id ;;fk:[core_biz_object cascade]")
    var targetBizObjectId: String,
    @ApiModelProperty(value = "目标对象记录类型id ;;fk:[core_biz_object_type cascade]")
    var targetTypeId: String,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
