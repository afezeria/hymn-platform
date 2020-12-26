package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.dto.ButtonPermDto
import github.afezeria.hymn.core.module.entity.ButtonPerm

/**
 * @author afezeria
 */
interface ButtonPermService {

    fun removeById(id: String): Int

    fun update(id: String, dto: ButtonPermDto): Int

    fun create(dto: ButtonPermDto): String

    fun findAll(): MutableList<ButtonPerm>

    fun findById(id: String): ButtonPerm?

    fun findByIds(ids: List<String>): MutableList<ButtonPerm>

    fun findByRoleIdAndButtonId(
        roleId: String,
        buttonId: String,
    ): ButtonPerm?

    fun findByRoleId(
        roleId: String,
    ): MutableList<ButtonPerm>

    fun findByButtonId(
        buttonId: String,
    ): MutableList<ButtonPerm>

    fun batchCreate(dtoList: List<ButtonPermDto>): MutableList<Int>

    fun batchSave(dtoList: List<ButtonPermDto>): MutableList<Int>


}