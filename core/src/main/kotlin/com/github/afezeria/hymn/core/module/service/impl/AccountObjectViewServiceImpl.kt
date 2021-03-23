package com.github.afezeria.hymn.core.module.service.impl

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.AccountObjectViewDao
import com.github.afezeria.hymn.core.module.dto.AccountObjectViewDto
import com.github.afezeria.hymn.core.module.entity.AccountObjectView
import com.github.afezeria.hymn.core.module.service.AccountObjectViewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class AccountObjectViewServiceImpl : AccountObjectViewService {

    @Autowired
    private lateinit var accountObjectViewDao: AccountObjectViewDao

    @Autowired
    private lateinit var dbService: DatabaseService


    override fun removeById(id: String): Int {
        accountObjectViewDao.selectById(id)
            ?: throw DataNotFoundException("AccountObjectView".msgById(id))
        val i = accountObjectViewDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: AccountObjectViewDto): Int {
        val e = accountObjectViewDao.selectById(id)
            ?: throw DataNotFoundException("AccountObjectView".msgById(id))
        dto.update(e)
        val i = accountObjectViewDao.update(e)
        return i
    }

    override fun create(dto: AccountObjectViewDto): String {
        val e = dto.toEntity()
        val id = accountObjectViewDao.insert(e)
        return id
    }

    override fun findAll(): MutableList<AccountObjectView> {
        return accountObjectViewDao.selectAll()
    }


    override fun findById(id: String): AccountObjectView? {
        return accountObjectViewDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<AccountObjectView> {
        return accountObjectViewDao.selectByIds(ids)
    }


    override fun findByAccountId(
        accountId: String,
    ): MutableList<AccountObjectView> {
        return accountObjectViewDao.selectByAccountId(accountId)
    }

    override fun findByBizObjectId(
        bizObjectId: String,
    ): MutableList<AccountObjectView> {
        return accountObjectViewDao.selectByBizObjectId(bizObjectId)
    }

    override fun pageFind(pageSize: Int, pageNum: Int): List<AccountObjectView> {
        return accountObjectViewDao.pageSelect(null, pageSize, pageNum)
    }


}