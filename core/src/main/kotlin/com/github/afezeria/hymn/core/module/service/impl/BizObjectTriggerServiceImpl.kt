package com.github.afezeria.hymn.core.module.service.impl


import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.BizObjectTriggerDao
import com.github.afezeria.hymn.core.module.dto.BizObjectTriggerDto
import com.github.afezeria.hymn.core.module.entity.BizObjectTrigger
import com.github.afezeria.hymn.core.module.service.BizObjectTriggerService
import com.github.afezeria.hymn.core.platform.script.ScriptService
import com.github.afezeria.hymn.core.platform.script.ScriptType
import org.ktorm.dsl.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectTriggerServiceImpl : BizObjectTriggerService {

    @Autowired
    private lateinit var bizObjectTriggerDao: BizObjectTriggerDao

    @Autowired
    private lateinit var scriptService: ScriptService

    @Autowired
    private lateinit var dbService: DatabaseService


    override fun removeById(id: String): Int {
        bizObjectTriggerDao.selectAvailableType { it.id eq id }
        val i = bizObjectTriggerDao.deleteById(id)
        return i
    }

    /**
     * 更新脚本并提交事务
     */
    override fun update(id: String, dto: BizObjectTriggerDto): Int {
        return dbService.useTransaction {
            val e = bizObjectTriggerDao.selectAvailableTypeWithLock { it.id eq id }.firstOrNull()
                ?: throw DataNotFoundException("BizObjectTrigger".msgById(id))
            dto.update(e)
            scriptService.compile(
                type = ScriptType.TRIGGER,
                id = id,
                objectId = dto.bizObjectId,
                api = e.api,
                lang = dto.lang,
                option = dto.optionText,
                code = dto.code
            ) {
                bizObjectTriggerDao.update(e)
            }
        }
    }

    override fun create(dto: BizObjectTriggerDto): String {
        val e = dto.toEntity()
        val id = scriptService.compile(
            type = ScriptType.TRIGGER,
            id = null,
            objectId = dto.bizObjectId,
            api = e.api,
            lang = dto.lang,
            option = dto.optionText,
            code = dto.code,
        ) {
            bizObjectTriggerDao.insert(e)
        }
        return id
    }

    override fun findByBizObjectId(bizObjectId: String): MutableList<BizObjectTrigger> {
        return bizObjectTriggerDao.selectAvailableType { it.bizObjectId eq bizObjectId }
    }

    override fun findById(id: String): BizObjectTrigger? {
        return bizObjectTriggerDao.selectAvailableType { it.id eq id }.first()
    }

}