package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.Account
import github.afezeria.hymn.core.module.dao.AccountDao
import github.afezeria.hymn.core.module.dto.AccountDto
import github.afezeria.hymn.core.module.service.AccountService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class AccountServiceImpl(
    private val accountDao: AccountDao,
) : AccountService {
    override fun removeById(id: String): Int {
        accountDao.selectById(id)
            ?: throw DataNotFoundException("Account".msgById(id))
        val i = accountDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: AccountDto): Int {
        val e = accountDao.selectById(id)
            ?: throw DataNotFoundException("Account".msgById(id))
        dto.update(e)
        val i = accountDao.update(e)
        return i
    }

    override fun create(dto: AccountDto): String {
        val e = dto.toEntity()
        val id = accountDao.insert(e)
        return id
    }

    override fun findAll(): List<Account> {
        return accountDao.selectAll()
    }


    override fun findById(id: String): Account? {
        return accountDao.selectById(id)
    }

    override fun findByLeaderId(
        leaderId: String,
    ): List<Account> {
        return accountDao.selectByLeaderId(leaderId,)
    }

    override fun findByOrgId(
        orgId: String,
    ): List<Account> {
        return accountDao.selectByOrgId(orgId,)
    }

    override fun findByRoleId(
        roleId: String,
    ): List<Account> {
        return accountDao.selectByRoleId(roleId,)
    }


}