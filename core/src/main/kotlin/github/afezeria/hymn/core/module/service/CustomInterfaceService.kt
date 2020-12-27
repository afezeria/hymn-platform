package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.dto.CustomInterfaceDto
import github.afezeria.hymn.core.module.entity.CustomInterface

/**
 * @author afezeria
 */
interface CustomInterfaceService {

    fun removeById(id: String): Int

    fun update(id: String, dto: CustomInterfaceDto): Int

    fun create(dto: CustomInterfaceDto): String

    fun findAll(): MutableList<CustomInterface>

    fun findById(id: String): CustomInterface?

    fun findByIds(ids: List<String>): MutableList<CustomInterface>

    fun findByApi(
        api: String,
    ): CustomInterface?


}