package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.Role
import github.afezeria.hymn.core.module.dao.RoleDao
import github.afezeria.hymn.core.module.dto.RoleDto
import github.afezeria.hymn.core.module.service.RoleService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author afezeria
 */
@Service
class RoleServiceImpl : RoleService {

    @Autowired
    lateinit var roleDao: RoleDao


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

    override fun findAll(): List<Role> {
        return roleDao.selectAll()
    }


    override fun findById(id: String): Role? {
        return roleDao.selectById(id)
    }


}