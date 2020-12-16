package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.AccountMenuLayout
import github.afezeria.hymn.core.module.dao.AccountMenuLayoutDao
import github.afezeria.hymn.core.module.dto.AccountMenuLayoutDto
import github.afezeria.hymn.core.module.service.AccountMenuLayoutService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class AccountMenuLayoutServiceImpl(
    private val accountMenuLayoutDao: AccountMenuLayoutDao,
) : AccountMenuLayoutService {
    override fun removeById(id: String): Int {
        accountMenuLayoutDao.selectById(id)
            ?: throw DataNotFoundException("AccountMenuLayout".msgById(id))
        val i = accountMenuLayoutDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: AccountMenuLayoutDto): Int {
        val e = accountMenuLayoutDao.selectById(id)
            ?: throw DataNotFoundException("AccountMenuLayout".msgById(id))
        dto.update(e)
        val i = accountMenuLayoutDao.update(e)
        return i
    }

    override fun create(dto: AccountMenuLayoutDto): String {
        val e = dto.toEntity()
        val id = accountMenuLayoutDao.insert(e)
        return id
    }

    override fun findAll(): List<AccountMenuLayout> {
        return accountMenuLayoutDao.selectAll()
    }


    override fun findById(id: String): AccountMenuLayout? {
        return accountMenuLayoutDao.selectById(id)
    }

    override fun findByAccountId(
        accountId: String,
    ): List<AccountMenuLayout> {
        return accountMenuLayoutDao.selectByAccountId(accountId,)
    }


}