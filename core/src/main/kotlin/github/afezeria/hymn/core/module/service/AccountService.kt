package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.Account
import github.afezeria.hymn.core.module.dto.AccountDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface AccountService {

    fun removeById(id: String): Int

    fun update(id: String, dto: AccountDto): Int

    fun create(dto: AccountDto): String

    fun findAll(): List<Account>

    fun findById(id: String): Account?

    fun findByLeaderId(
        leaderId: String,
    ): List<Account>

    fun findByOrgId(
        orgId: String,
    ): List<Account>

    fun findByRoleId(
        roleId: String,
    ): List<Account>


}