package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.RoleDao
import github.afezeria.hymn.core.module.dto.RoleDto
import github.afezeria.hymn.core.module.entity.Role
import org.ktorm.dsl.inList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class RoleService {

    @Autowired
    private lateinit var roleDao: RoleDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        roleDao.selectById(id)
            ?: throw DataNotFoundException("Role".msgById(id))
        val i = roleDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: RoleDto): Int {
        val e = roleDao.selectById(id)
            ?: throw DataNotFoundException("Role".msgById(id))
        dto.update(e)
        val i = roleDao.update(e)
        return i
    }

    fun create(dto: RoleDto): String {
        val e = dto.toEntity()
        val id = roleDao.insert(e)
        return id
    }

    fun findAll(): MutableList<Role> {
        return roleDao.selectAll()
    }


    fun findById(id: String): Role? {
        return roleDao.selectById(id)
    }

    fun findByIds(ids: Collection<String>): MutableList<Role> {
        return roleDao.selectByIds(ids)
    }

    fun findIdList(ids: List<String>? = null): MutableList<String> {
        return if (ids != null && ids.isEmpty()) {
            ArrayList()
        } else {
            roleDao.select(listOf(roleDao.table.id), ids?.let { { roleDao.table.id inList it } })
                .mapTo(ArrayList()) { it[roleDao.table.id.name]!! as String }
        }
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<Role> {
        return roleDao.pageSelect(null, pageSize, pageNum)
    }


}