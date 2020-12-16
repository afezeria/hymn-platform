package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.AccountObjectView
import github.afezeria.hymn.core.module.dto.AccountObjectViewDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface AccountObjectViewService {

    fun removeById(id: String): Int

    fun update(id: String, dto: AccountObjectViewDto): Int

    fun create(dto: AccountObjectViewDto): String

    fun findAll(): List<AccountObjectView>

    fun findById(id: String): AccountObjectView?

    fun findByAccountId(
        accountId: String,
    ): List<AccountObjectView>

    fun findByBizObjectId(
        bizObjectId: String,
    ): List<AccountObjectView>


}