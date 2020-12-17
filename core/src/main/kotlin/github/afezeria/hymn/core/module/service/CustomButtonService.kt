package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.CustomButton
import github.afezeria.hymn.core.module.dto.CustomButtonDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface CustomButtonService {

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