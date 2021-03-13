package com.github.afezeria.hymn.oss.module.service

import com.github.afezeria.hymn.oss.module.dto.PreSignedHistoryDto
import com.github.afezeria.hymn.oss.module.entity.PreSignedHistory
import java.time.LocalDateTime

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

    fun pageFindBetweenCreateDate(
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        pageSize: Int,
        pageNum: Int
    ): List<PreSignedHistory>

    fun pageFindByFileId(fileId: String, pageSize: Int, pageNum: Int): List<PreSignedHistory>

    fun removeByIds(ids: List<String>): Int


}