package github.afezeria.hymn.core.module.entity

import java.time.LocalDateTime
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 *
 * 组织
 * @author afezeria
 */
@ApiModel(value="组织",description = """组织""")
data class Org(

    @ApiModelProperty(value = "")
    var name: String,
    @ApiModelProperty(value = "部门领导id", required = true)
    var directorId: String? = null,
    @ApiModelProperty(value = "部门副领导id", required = true)
    var deputyDirectorId: String? = null,
    @ApiModelProperty(value = "上级组织id ;; idx", required = true)
    var parentId: String? = null,
) {

    lateinit var id: String
    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
