package github.afezeria.hymn.core.module.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 字典项 ;;uk:[[dict_id code]]
 * @author afezeria
 */
@ApiModel(value = "字典项", description = """字典项 ;;uk:[[dict_id code]]""")
data class DictItem(

    @ApiModelProperty(value = "所属字典id ;;fk:[core_dict cascade];idx", required = true)
    var dictId: String,
    @ApiModelProperty(value = "字典项名称", required = true)
    var name: String,
    @ApiModelProperty(value = "字典项编码", required = true)
    var code: String,
    @ApiModelProperty(value = "父字典中的字典项编码，用于表示多个选项列表的级联关系")
    var parentCode: String? = null,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
