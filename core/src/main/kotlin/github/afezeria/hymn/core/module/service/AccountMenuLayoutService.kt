package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.AccountMenuLayoutDao
import github.afezeria.hymn.core.module.dto.AccountMenuLayoutDto
import github.afezeria.hymn.core.module.entity.AccountMenuLayout
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class AccountMenuLayoutService {

    @Autowired
    private lateinit var accountMenuLayoutDao: AccountMenuLayoutDao

    fun pageFind(pageSize: Int, pageNum: Int): List<AccountMenuLayout> {
        return accountMenuLayoutDao.pageSelect(null, pageSize, pageNum)
    }

    fun removeById(id: String): Int {
        accountMenuLayoutDao.selectById(id)
            ?: throw DataNotFoundException("AccountMenuLayout".msgById(id))
        val i = accountMenuLayoutDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: AccountMenuLayoutDto): Int {
        val e = accountMenuLayoutDao.selectById(id)
            ?: throw DataNotFoundException("AccountMenuLayout".msgById(id))
        dto.update(e)
        val i = accountMenuLayoutDao.update(e)
        return i
    }

    fun create(dto: AccountMenuLayoutDto): String {
        val e = dto.toEntity()
        val id = accountMenuLayoutDao.insert(e)
        return id
    }

    fun findAll(): MutableList<AccountMenuLayout> {
        return accountMenuLayoutDao.selectAll()
    }


    fun findById(id: String): AccountMenuLayout? {
        return accountMenuLayoutDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<AccountMenuLayout> {
        return accountMenuLayoutDao.selectByIds(ids)
    }


    fun findByAccountId(
        accountId: String,
    ): MutableList<AccountMenuLayout> {
        return accountMenuLayoutDao.selectByAccountId(accountId)
    }


}