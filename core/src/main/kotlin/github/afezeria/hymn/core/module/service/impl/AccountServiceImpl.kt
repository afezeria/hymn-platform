package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.AccountDao
import github.afezeria.hymn.core.module.dto.AccountDto
import github.afezeria.hymn.core.module.entity.Account
import github.afezeria.hymn.core.module.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class AccountServiceImpl : AccountService {

    @Autowired
    private lateinit var accountDao: AccountDao

    @Autowired
    private lateinit var dbService: DataBaseService


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

    override fun findAll(): MutableList<Account> {
        return accountDao.selectAll()
    }


    override fun findById(id: String): Account? {
        return accountDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<Account> {
        return accountDao.selectByIds(ids)
    }


    override fun findByLeaderId(
        leaderId: String,
    ): MutableList<Account> {
        return accountDao.selectByLeaderId(leaderId)
    }

    override fun findByOrgId(
        orgId: String,
    ): MutableList<Account> {
        return accountDao.selectByOrgId(orgId)
    }

    override fun findByRoleId(
        roleId: String,
    ): MutableList<Account> {
        return accountDao.selectByRoleId(roleId)
    }


}