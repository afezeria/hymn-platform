package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.BizObjectTriggerDao
import com.github.afezeria.hymn.core.module.dto.BizObjectTriggerDto
import com.github.afezeria.hymn.core.module.entity.BizObjectTrigger
import com.github.afezeria.hymn.core.platform.script.CompileType
import com.github.afezeria.hymn.core.platform.script.ScriptService
import org.ktorm.dsl.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectTriggerService {

    @Autowired
    private lateinit var bizObjectTriggerDao: BizObjectTriggerDao

    @Autowired
    private lateinit var scriptService: ScriptService

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        bizObjectTriggerDao.selectAvailableType { it.id eq id }
        val i = bizObjectTriggerDao.deleteById(id)
        return i
    }

    /**
     * 更新脚本并提交事务
     */
    fun update(id: String, dto: BizObjectTriggerDto): Int {
        return dbService.useTransaction {
            val e = bizObjectTriggerDao.selectAvailableTypeWithLock { it.id eq id }.firstOrNull()
                ?: throw DataNotFoundException("BizObjectTrigger".msgById(id))
            dto.update(e)
            scriptService.compile(
                type = CompileType.TRIGGER,
                id = id,
                lang = dto.lang,
                option = dto.optionText,
                code = dto.code
            ) {
                bizObjectTriggerDao.update(e)
            }
        }
    }

    fun create(dto: BizObjectTriggerDto): String {
        val e = dto.toEntity()
        val id = scriptService.compile(
            type = CompileType.TRIGGER,
            id = null,
            lang = dto.lang,
            option = dto.optionText,
            code = dto.code,
        ) {
            bizObjectTriggerDao.insert(e)
        }
        return id
    }

    fun findByBizObjectId(bizObjectId: String): MutableList<BizObjectTrigger> {
        return bizObjectTriggerDao.selectAvailableType { it.bizObjectId eq bizObjectId }
    }

    fun findById(id: String): BizObjectTrigger? {
        return bizObjectTriggerDao.selectAvailableType { it.id eq id }.first()
    }

}