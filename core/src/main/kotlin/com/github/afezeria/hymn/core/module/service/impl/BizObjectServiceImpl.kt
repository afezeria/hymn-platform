package com.github.afezeria.hymn.core.module.service.impl

/**
 * @author afezeria
 */
import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.exception.InnerException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.BizObjectDao
import com.github.afezeria.hymn.core.module.dto.BizObjectDto
import com.github.afezeria.hymn.core.module.dto.BizObjectLayoutDto
import com.github.afezeria.hymn.core.module.dto.BizObjectTypeDto
import com.github.afezeria.hymn.core.module.entity.BizObject
import com.github.afezeria.hymn.core.module.service.BizObjectLayoutService
import com.github.afezeria.hymn.core.module.service.BizObjectService
import com.github.afezeria.hymn.core.module.service.BizObjectTypeService
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BizObjectServiceImpl : BizObjectService {

    @Autowired
    private lateinit var bizObjectDao: BizObjectDao

    @Autowired
    //todo 需要createdefaultfield方法的可见性
    private lateinit var fieldService: BizObjectFieldServiceImpl

    @Autowired
    private lateinit var typeService: BizObjectTypeService

    @Autowired
    private lateinit var layoutService: BizObjectLayoutService

    @Autowired
    private lateinit var dbService: DatabaseService

    override fun findAllActiveObject(): MutableList<BizObject> {
        return bizObjectDao.select({ it.active eq true })
    }

    override fun findActiveObjectById(id: String): BizObject? {
        return bizObjectDao.singleRowSelect({ (it.active eq true) and (it.id eq id) })
    }

    override fun findActiveObjectByIds(ids: Collection<String>): MutableList<BizObject> {
        return bizObjectDao.select({ (it.active eq true) and (it.id inList ids) })
    }

    override fun findActiveObjectByApi(
        api: String,
    ): BizObject? {
        return bizObjectDao.singleRowSelect({ (it.api eq api) and (it.active eq true) })
    }

    override fun findActiveObjectByApiList(apiList: Collection<String>): List<BizObject> {
        return bizObjectDao.select({ (it.api inList apiList) and (it.active eq true) })
    }

    override fun pageFind(pageSize: Int, pageNum: Int): List<BizObject> {
        return bizObjectDao.pageSelect({ it.active eq true }, pageSize, pageNum)
    }

    override fun findAllInactiveObject(): List<BizObject> {
        return bizObjectDao.select({ it.active eq false })
    }


    override fun removeById(id: String): Int {
        val bizObject = bizObjectDao.selectById(id)
            ?: throw DataNotFoundException("BizObject".msgById(id))
        if (bizObject.type == "remote")
            throw InnerException("不能删除模块对象")
        if (bizObject.active) {
            throw InnerException("不能删除启用的对象")
        }
        val i = bizObjectDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: BizObjectDto): Int {
        return dbService.useTransaction {
            val e = findActiveObjectById(id)
                ?: throw DataNotFoundException("BizObject".msgById(id))
            dto.update(e)
            val i = bizObjectDao.update(e)
            i
        }
    }

    override fun create(dto: BizObjectDto): String {
        if (dto.type != "module") {
            throw InnerException("无法在后台创建模块对象")
        }
        dbService.db().useConnection {
            val e = dto.toEntity()
            val id = bizObjectDao.insert(e)
//            创建默认字段
            fieldService.createDefaultField(id, dto.fieldName, dto.autoRule)
//            创建默认布局
            val layoutId =
                layoutService.create(
                    BizObjectLayoutDto.defaultLayout(id)
                )
//            创建默认类型
            typeService.create(
                BizObjectTypeDto(
                    bizObjectId = id,
                    name = "默认类型",
                    defaultLayoutId = layoutId,
                    active = true
                )
            )

            return id
        }

    }

    /**
     * 停用对象
     */
    override fun inactivateById(id: String): Int {
        val bizObject = findActiveObjectById(id)
            ?: throw DataNotFoundException("BizObject".msgById(id))
        if (bizObject.type == "module") {
            throw InnerException("不能修改模块对象启用状态")
        }
        bizObject.active = false
        return bizObjectDao.update(bizObject)
    }

    /**
     * 启用对象
     */
    override fun activateById(id: String): Int {
        val bizObject =
            bizObjectDao.singleRowSelect({ (it.id eq id) and (it.active eq false) })
                ?: throw DataNotFoundException("BizObject".msgById(id))
        if (bizObject.type == "module") {
            throw InnerException("不能修改模块对象启用状态")
        }
        bizObject.active = true
        return bizObjectDao.update(bizObject)
    }


}