package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.AccountObjectViewDao
import com.github.afezeria.hymn.core.module.dto.AccountObjectViewDto
import com.github.afezeria.hymn.core.module.entity.AccountObjectView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class AccountObjectViewService {

    @Autowired
    private lateinit var accountObjectViewDao: AccountObjectViewDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        accountObjectViewDao.selectById(id)
            ?: throw DataNotFoundException("AccountObjectView".msgById(id))
        val i = accountObjectViewDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: AccountObjectViewDto): Int {
        val e = accountObjectViewDao.selectById(id)
            ?: throw DataNotFoundException("AccountObjectView".msgById(id))
        dto.update(e)
        val i = accountObjectViewDao.update(e)
        return i
    }

    fun create(dto: AccountObjectViewDto): String {
        val e = dto.toEntity()
        val id = accountObjectViewDao.insert(e)
        return id
    }

    fun findAll(): MutableList<AccountObjectView> {
        return accountObjectViewDao.selectAll()
    }


    fun findById(id: String): AccountObjectView? {
        return accountObjectViewDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<AccountObjectView> {
        return accountObjectViewDao.selectByIds(ids)
    }


    fun findByAccountId(
        accountId: String,
    ): MutableList<AccountObjectView> {
        return accountObjectViewDao.selectByAccountId(accountId)
    }

    fun findByBizObjectId(
        bizObjectId: String,
    ): MutableList<AccountObjectView> {
        return accountObjectViewDao.selectByBizObjectId(bizObjectId)
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<AccountObjectView> {
        return accountObjectViewDao.pageSelect(null, pageSize, pageNum)
    }


}