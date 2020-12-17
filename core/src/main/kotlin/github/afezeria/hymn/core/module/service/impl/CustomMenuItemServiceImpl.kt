package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.CustomMenuItem
import github.afezeria.hymn.core.module.dao.CustomMenuItemDao
import github.afezeria.hymn.core.module.dto.CustomMenuItemDto
import github.afezeria.hymn.core.module.service.CustomMenuItemService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author afezeria
 */
@Service
class CustomMenuItemServiceImpl : CustomMenuItemService {

    @Autowired
    lateinit var customMenuItemDao: CustomMenuItemDao


    override fun removeById(id: String): Int {
        customMenuItemDao.selectById(id)
            ?: throw DataNotFoundException("CustomMenuItem".msgById(id))
        val i = customMenuItemDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: CustomMenuItemDto): Int {
        val e = customMenuItemDao.selectById(id)
            ?: throw DataNotFoundException("CustomMenuItem".msgById(id))
        dto.update(e)
        val i = customMenuItemDao.update(e)
        return i
    }

    override fun create(dto: CustomMenuItemDto): String {
        val e = dto.toEntity()
        val id = customMenuItemDao.insert(e)
        return id
    }

    override fun findAll(): List<CustomMenuItem> {
        return customMenuItemDao.selectAll()
    }


    override fun findById(id: String): CustomMenuItem? {
        return customMenuItemDao.selectById(id)
    }


}