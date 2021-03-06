package com.github.afezeria.hymn.core.module.service.impl

/**
 * @author afezeria
 */
import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dao.RoleDao
import com.github.afezeria.hymn.core.module.dto.RoleDto
import com.github.afezeria.hymn.core.module.entity.Role
import com.github.afezeria.hymn.core.module.service.RoleService
import org.ktorm.dsl.inList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RoleServiceImpl : RoleService {

    @Autowired
    private lateinit var roleDao: RoleDao

    @Autowired
    private lateinit var dbService: DatabaseService


    override fun removeById(id: String): Int {
        roleDao.selectById(id)
            ?: throw DataNotFoundException("Role".msgById(id))
        val i = roleDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: RoleDto): Int {
        val e = roleDao.selectById(id)
            ?: throw DataNotFoundException("Role".msgById(id))
        dto.update(e)
        val i = roleDao.update(e)
        return i
    }

    override fun create(dto: RoleDto): String {
        val e = dto.toEntity()
        val id = roleDao.insert(e)
        return id
    }

    override fun findAll(): MutableList<Role> {
        return roleDao.selectAll()
    }


    override fun findById(id: String): Role? {
        return roleDao.selectById(id)
    }

    override fun findByIds(ids: Collection<String>): MutableList<Role> {
        return roleDao.selectByIds(ids)
    }

    override fun findIdList(ids: List<String>?): MutableList<String> {
        return if (ids != null && ids.isEmpty()) {
            ArrayList()
        } else {
            roleDao.select(listOf(roleDao.table.id), ids?.let { { roleDao.table.id inList it } })
                .mapTo(ArrayList()) { it[roleDao.table.id.name]!! as String }
        }
    }

    override fun pageFind(pageSize: Int, pageNum: Int): List<Role> {
        return roleDao.pageSelect(null, pageSize, pageNum)
    }


}