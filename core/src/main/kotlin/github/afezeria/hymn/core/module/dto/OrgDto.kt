package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.Org
import java.time.LocalDateTime
import io.swagger.annotations.*
import java.util.*

/**
 * @author afezeria
 */
class OrgDto(
    @ApiModelProperty(value = "")
    var name: String,
    @ApiModelProperty(value = "部门领导id", required = true)
    var directorId: String? = null,
    @ApiModelProperty(value = "部门副领导id", required = true)
    var deputyDirectorId: String? = null,
    @ApiModelProperty(value = "上级组织id ;; idx", required = true)
    var parentId: String? = null,
){
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
