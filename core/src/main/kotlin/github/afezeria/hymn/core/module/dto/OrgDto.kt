package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.Org
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class OrgDto(
    @ApiModelProperty(value = "", required = true)
    var name: String,
    @ApiModelProperty(value = "部门领导id")
    var directorId: String? = null,
    @ApiModelProperty(value = "部门副领导id")
    var deputyDirectorId: String? = null,
    @ApiModelProperty(value = "上级组织id")
    var parentId: String? = null,
) {
    fun toEntity(): Org {
        return Org(
            name = name,
            directorId = directorId,
            deputyDirectorId = deputyDirectorId,
            parentId = parentId,
        )
    }

    fun update(entity: Org) {
        entity.also {
            it.name = name
            it.directorId = directorId
            it.deputyDirectorId = deputyDirectorId
            it.parentId = parentId
        }
    }
}
