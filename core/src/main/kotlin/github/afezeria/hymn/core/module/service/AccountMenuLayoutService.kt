package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.dto.AccountMenuLayoutDto
import github.afezeria.hymn.core.module.entity.AccountMenuLayout

/**
 * @author afezeria
 */
interface AccountMenuLayoutService {

    fun removeById(id: String): Int

    fun update(id: String, dto: AccountMenuLayoutDto): Int

    fun create(dto: AccountMenuLayoutDto): String

    fun findAll(): MutableList<AccountMenuLayout>

    fun findById(id: String): AccountMenuLayout?

    fun findByIds(ids: List<String>): MutableList<AccountMenuLayout>

    fun findByAccountId(
        accountId: String,
    ): MutableList<AccountMenuLayout>


}