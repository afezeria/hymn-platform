package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.constant.ROOT_ACCOUNT_ID
import github.afezeria.hymn.core.module.dao.AccountDao
import github.afezeria.hymn.core.module.dto.AccountDto
import github.afezeria.hymn.core.module.entity.Account
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class AccountService {

    @Autowired
    private lateinit var accountDao: AccountDao

    fun removeById(id: String): Int {
        if (id == ROOT_ACCOUNT_ID) {
            return 0
        }
        accountDao.selectById(id)
            ?: throw DataNotFoundException("Account".msgById(id))
        val i = accountDao.deleteById(id)
        return i
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<Account> {
        return accountDao.pageSelect(null, pageSize, pageNum)
    }

    fun update(id: String, dto: AccountDto): Int {
        val e = accountDao.selectById(id)
            ?: throw DataNotFoundException("Account".msgById(id))
        dto.update(e)
        val i = accountDao.update(e)
        return i
    }

    fun create(dto: AccountDto): String {
        val e = dto.toEntity()
        val id = accountDao.insert(e)
        return id
    }

    fun findAll(): MutableList<Account> {
        return accountDao.selectAll()
    }


    fun findById(id: String): Account? {
        return accountDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<Account> {
        return accountDao.selectByIds(ids)
    }


    fun findByLeaderId(
        leaderId: String,
    ): MutableList<Account> {
        return accountDao.selectByLeaderId(leaderId)
    }

    fun findByOrgId(
        orgId: String,
    ): MutableList<Account> {
        return accountDao.selectByOrgId(orgId)
    }

    fun findByRoleId(
        roleId: String,
    ): MutableList<Account> {
        return accountDao.selectByRoleId(roleId)
    }


}