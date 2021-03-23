package com.github.afezeria.hymn.core.module.service.impl

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.constant.ROOT_ACCOUNT_ID
import com.github.afezeria.hymn.core.module.dao.AccountDao
import com.github.afezeria.hymn.core.module.dto.AccountDto
import com.github.afezeria.hymn.core.module.entity.Account
import com.github.afezeria.hymn.core.module.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class AccountServiceImpl : AccountService {

    @Autowired
    private lateinit var accountDao: AccountDao

    override fun removeById(id: String): Int {
        if (id == ROOT_ACCOUNT_ID) {
            return 0
        }
        accountDao.selectById(id)
            ?: throw DataNotFoundException("Account".msgById(id))
        val i = accountDao.deleteById(id)
        return i
    }

    override fun pageFind(pageSize: Int, pageNum: Int): List<Account> {
        return accountDao.pageSelect(null, pageSize, pageNum)
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