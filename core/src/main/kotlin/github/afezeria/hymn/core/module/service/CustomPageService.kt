package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.dto.CustomPageDto
import github.afezeria.hymn.core.module.entity.CustomPage

/**
 * @author afezeria
 */
internal interface CustomPageService {

    fun removeById(id: String): Int

    fun update(id: String, dto: CustomPageDto): Int

    fun create(dto: CustomPageDto): String

    fun findAll(): MutableList<CustomPage>

    fun findById(id: String): CustomPage?

    fun findByIds(ids: List<String>): MutableList<CustomPage>

    fun findByApi(
        api: String,
    ): CustomPage?

    fun pageFind(pageSize: Int, pageNum: Int): List<CustomPage>


}