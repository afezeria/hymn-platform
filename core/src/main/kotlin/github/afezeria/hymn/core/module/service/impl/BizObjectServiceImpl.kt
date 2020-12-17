package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.InnerException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BizObjectDao
import github.afezeria.hymn.core.module.dto.BizObjectDto
import github.afezeria.hymn.core.module.dto.BizObjectLayoutDto
import github.afezeria.hymn.core.module.dto.BizObjectTypeDto
import github.afezeria.hymn.core.module.dto.BizObjectTypeLayoutDto
import github.afezeria.hymn.core.module.entity.BizObject
import github.afezeria.hymn.core.module.service.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectServiceImpl : BizObjectService {

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
    private lateinit var dbService: DataBaseService


    override fun removeById(id: String): Int {
        bizObjectDao.selectById(id)
            ?: throw DataNotFoundException("BizObject".msgById(id))
        val i = bizObjectDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: BizObjectDto): Int {
        val e = bizObjectDao.selectById(id)
            ?: throw DataNotFoundException("BizObject".msgById(id))
        dto.update(e)
        val i = bizObjectDao.update(e)
        return i
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
            val typeId = typeService.create(BizObjectTypeDto(id, "默认类型", true))
//            创建默认布局
            val layoutId =
                layoutService.create(BizObjectLayoutDto(id, "默认布局", "", "", "", "", "", ""))

//            创建对象权限数据
            val objPermDtoList = dto.permList
            val roleIdList = objPermDtoList.map { it.roleId }
            val roleIdSet = roleService.findIdList(roleIdList).toSet()
            objectPermService.batchCreate(
                objPermDtoList.filter { roleIdSet.contains(it.roleId) }
                    .onEach { it.roleId = id }
            )


//            创建角色、记录类型对应到布局的映射数据
            typeLayoutService.batchCreate(roleIdSet.map {
                BizObjectTypeLayoutDto(it, id, typeId, layoutId)
            })

            return id
        }

    }

    override fun findAll(): MutableList<BizObject> {
        return bizObjectDao.selectAll()
    }


    override fun findById(id: String): BizObject? {
        return bizObjectDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<BizObject> {
        return bizObjectDao.selectByIds(ids)
    }


    override fun findByApi(
        api: String,
    ): BizObject? {
        return bizObjectDao.selectByApi(api)
    }


}