package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.dto.SharedCodeDto
import github.afezeria.hymn.core.module.entity.SharedCode

/**
 * @author afezeria
 */
interface SharedCodeService {

    fun removeById(id: String): Int

    fun update(id: String, dto: SharedCodeDto): Int

    fun create(dto: SharedCodeDto): String

    fun findAll(): MutableList<SharedCode>

    fun findById(id: String): SharedCode?

    fun findByIds(ids: List<String>): MutableList<SharedCode>

    fun findByApi(
        api: String,
    ): SharedCode?


}