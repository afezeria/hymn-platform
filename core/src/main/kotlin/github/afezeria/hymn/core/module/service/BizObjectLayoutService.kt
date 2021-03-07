package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.BizObjectLayoutDao
import github.afezeria.hymn.core.module.dto.BizObjectLayoutDto
import github.afezeria.hymn.core.module.entity.BizObjectLayout
import github.afezeria.hymn.core.module.view.BizObjectLayoutListView
import org.ktorm.dsl.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class BizObjectLayoutService {

    @Autowired
    private lateinit var bizObjectLayoutDao: BizObjectLayoutDao

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var typeService: BizObjectTypeService

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        bizObjectLayoutDao.selectAvailableLayout { it.id eq id }.firstOrNull()
            ?: throw DataNotFoundException("BizObjectLayout".msgById(id))
        val i = bizObjectLayoutDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: BizObjectLayoutDto): Int {
        val e = bizObjectLayoutDao.selectAvailableLayout { it.id eq id }.firstOrNull()
            ?: throw DataNotFoundException("BizObjectLayout".msgById(id))
        dto.update(e)
        val i = bizObjectLayoutDao.update(e)
        return i
    }

    fun create(dto: BizObjectLayoutDto): String {
        val e = dto.toEntity()
        val id = bizObjectLayoutDao.insert(e)
        return id
    }

    fun findById(id: String): BizObjectLayout? {
        return bizObjectLayoutDao.selectAvailableLayout { it.id eq id }
            .firstOrNull()
    }

    fun findByBizObjectId(bizObjectId: String): MutableList<BizObjectLayout> {
        return bizObjectLayoutDao.select({ it.bizObjectId eq bizObjectId })
    }

    /**
     * 根据当前角色和数据的业务类型返回对应的布局
     */
    fun findByRoleIdAndTypeId(roleId: String, typeId: String): BizObjectLayout {
        roleService.findById(roleId) ?: throw DataNotFoundException("角色".msgById(roleId))
        val type =
            typeService.findById(typeId) ?: throw DataNotFoundException("业务类型".msgById(roleId))
        val layout = bizObjectLayoutDao.selectByRoleIdAndTypeId(roleId, typeId)
            ?: requireNotNull(bizObjectLayoutDao.selectById(type.defaultLayoutId))
        return layout
    }

    fun findListViewByBizObjectId(bizObjectId: String): MutableList<BizObjectLayoutListView> {
        return bizObjectLayoutDao.selectView { it.bizObjectId eq bizObjectId }
    }

}