package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.exception.InnerException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BizObjectDao
import github.afezeria.hymn.core.module.dto.BizObjectDto
import github.afezeria.hymn.core.module.dto.BizObjectLayoutDto
import github.afezeria.hymn.core.module.dto.BizObjectTypeDto
import github.afezeria.hymn.core.module.dto.BizObjectTypeLayoutDto
import github.afezeria.hymn.core.module.entity.BizObject
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectService {

    @Autowired
    private lateinit var bizObjectDao: BizObjectDao

    @Autowired
    private lateinit var fieldService: BizObjectFieldService

    @Autowired
    private lateinit var typeService: BizObjectTypeService

    @Autowired
    private lateinit var layoutService: BizObjectLayoutService

    @Autowired
    private lateinit var typeLayoutService: BizObjectTypeLayoutService

    @Autowired
    private lateinit var objectPermService: BizObjectPermService

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var dbService: DatabaseService

    fun findAll(): MutableList<BizObject> {
        return bizObjectDao.select({ it.active eq true })
    }

    fun findById(id: String): BizObject? {
        return bizObjectDao.singleRowSelect({ (it.active eq true) and (it.id eq id) })
    }

    fun findByIds(ids: List<String>): MutableList<BizObject> {
        return bizObjectDao.select({ (it.active eq true) and (it.id inList ids) })
    }

    fun findByApi(
        api: String,
    ): BizObject? {
        return bizObjectDao.singleRowSelect({ (it.api eq api) and (it.active eq true) })
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<BizObject> {
        return bizObjectDao.pageSelect({ it.active eq true }, pageSize, pageNum)
    }

    fun findInactiveObjectById(id: String): BizObject? {
        return bizObjectDao.singleRowSelect({ (it.active eq false) and (it.id eq id) })
    }

    fun findAllInactiveObject(id: String): List<BizObject> {
        return bizObjectDao.select({ it.active eq false })
    }


    fun removeById(id: String): Int {
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

    fun update(id: String, dto: BizObjectDto): Int {
        return dbService.useTransaction {

            val e = bizObjectDao.selectById(id)
                ?: throw DataNotFoundException("BizObject".msgById(id))
            dto.update(e)
            val i = bizObjectDao.update(e)
            i
        }
    }

    fun create(dto: BizObjectDto): String {
        if (dto.type != "module") {
            throw InnerException("无法在后台创建模块对象")
        }
        dbService.db().useConnection {
            val e = dto.toEntity()
            val id = bizObjectDao.insert(e)
//            创建默认字段
            fieldService.createDefaultField(id, dto.fieldName, dto.autoRule)
            val typeId = typeService.create(BizObjectTypeDto(id, "默认类型", true))
//            创建默认布局
            val layoutId =
                layoutService.create(BizObjectLayoutDto(id, "默认布局", "", "", "", "", "", ""))

//            创建角色、记录类型对应到布局的映射数据
            val roleList = roleService.findAll()
            typeLayoutService.batchCreate(roleList.map {
                BizObjectTypeLayoutDto(it.id, id, typeId, layoutId)
            })

            return id
        }

    }


    fun inactivateObjectById(id: String): Int {
        val bizObject = bizObjectDao.selectById(id)
            ?: throw DataNotFoundException("BizObject".msgById(id))
        if (bizObject.type == "module") {
            throw InnerException("不能修改模块对象启用状态")
        }
        if (bizObject.active) {
            bizObject.active = false
            return bizObjectDao.update(bizObject)
        }
        return 0
    }

    fun activateObjectById(id: String): Int {
        val bizObject = bizObjectDao.selectById(id)
            ?: throw DataNotFoundException("BizObject".msgById(id))
        if (bizObject.type == "module") {
            throw InnerException("不能修改模块对象启用状态")
        }
        if (bizObject.active) {
            bizObject.active = true
            return bizObjectDao.update(bizObject)
        }
        return 0
    }

}