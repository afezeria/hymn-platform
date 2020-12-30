package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.dto.CustomButtonDto
import github.afezeria.hymn.core.module.entity.CustomButton

/**
 * @author afezeria
 */
internal interface CustomButtonService {

    fun removeById(id: String): Int

    fun update(id: String, dto: CustomButtonDto): Int

    fun create(dto: CustomButtonDto): String

    fun findAll(): MutableList<CustomButton>

    fun findById(id: String): CustomButton?

    fun findByIds(ids: List<String>): MutableList<CustomButton>

    fun findByApi(
        api: String,
    ): CustomButton?


}