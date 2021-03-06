package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.AccountDto
import com.github.afezeria.hymn.core.module.entity.Account

/**
 * @author afezeria
 */
interface AccountService {
    fun removeById(id: String): Int
    fun pageFind(pageSize: Int, pageNum: Int): List<Account>
    fun update(id: String, dto: AccountDto): Int
    fun create(dto: AccountDto): String
    fun findAll(): MutableList<Account>
    fun findById(id: String): Account?
    fun findByIds(ids: List<String>): MutableList<Account>
    fun findByLeaderId(
        leaderId: String,
    ): MutableList<Account>

    fun findByOrgId(
        orgId: String,
    ): MutableList<Account>

    fun findByRoleId(
        roleId: String,
    ): MutableList<Account>

}