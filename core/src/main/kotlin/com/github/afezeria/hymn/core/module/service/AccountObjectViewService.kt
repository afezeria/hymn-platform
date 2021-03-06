package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.AccountObjectViewDto
import com.github.afezeria.hymn.core.module.entity.AccountObjectView

/**
 * @author afezeria
 */
interface AccountObjectViewService {
    fun removeById(id: String): Int
    fun update(id: String, dto: AccountObjectViewDto): Int
    fun create(dto: AccountObjectViewDto): String
    fun findAll(): MutableList<AccountObjectView>
    fun findById(id: String): AccountObjectView?
    fun findByIds(ids: List<String>): MutableList<AccountObjectView>
    fun findByAccountId(
        accountId: String,
    ): MutableList<AccountObjectView>

    fun findByBizObjectId(
        bizObjectId: String,
    ): MutableList<AccountObjectView>

    fun pageFind(pageSize: Int, pageNum: Int): List<AccountObjectView>
}