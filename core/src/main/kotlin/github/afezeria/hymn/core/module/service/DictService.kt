package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.dto.DictDto
import github.afezeria.hymn.core.module.entity.Dict

/**
 * @author afezeria
 */
interface DictService {

    fun removeById(id: String): Int

    fun update(id: String, dto: DictDto): Int

    fun create(dto: DictDto): String

    fun findAll(): MutableList<Dict>

    fun findById(id: String): Dict?

    fun findByIds(ids: List<String>): MutableList<Dict>

    fun findByApi(
        api: String,
    ): Dict?


}