package github.afezeria.hymn.oss.module.service

import github.afezeria.hymn.oss.module.entity.PreSignedHistory
import github.afezeria.hymn.oss.module.dto.PreSignedHistoryDto
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface PreSignedHistoryService {

    fun removeById(id: String): Int

    fun update(id: String, dto: PreSignedHistoryDto): Int

    fun create(dto: PreSignedHistoryDto): String

    fun findAll(): MutableList<PreSignedHistory>

    fun findById(id: String): PreSignedHistory?

    fun findByIds(ids: List<String>): MutableList<PreSignedHistory>

    fun findByFileId(
        fileId: String,
    ): MutableList<PreSignedHistory>


}