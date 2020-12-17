package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.CustomInterface
import github.afezeria.hymn.core.module.dao.CustomInterfaceDao
import github.afezeria.hymn.core.module.dto.CustomInterfaceDto
import github.afezeria.hymn.core.module.service.CustomInterfaceService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author afezeria
 */
@Service
class CustomInterfaceServiceImpl : CustomInterfaceService {

    @Autowired
    lateinit var customInterfaceDao: CustomInterfaceDao


    override fun removeById(id: String): Int {
        customInterfaceDao.selectById(id)
            ?: throw DataNotFoundException("CustomInterface".msgById(id))
        val i = customInterfaceDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: CustomInterfaceDto): Int {
        val e = customInterfaceDao.selectById(id)
            ?: throw DataNotFoundException("CustomInterface".msgById(id))
        dto.update(e)
        val i = customInterfaceDao.update(e)
        return i
    }

    override fun create(dto: CustomInterfaceDto): String {
        val e = dto.toEntity()
        val id = customInterfaceDao.insert(e)
        return id
    }

    override fun findAll(): List<CustomInterface> {
        return customInterfaceDao.selectAll()
    }


    override fun findById(id: String): CustomInterface? {
        return customInterfaceDao.selectById(id)
    }

    override fun findByApi(
        api: String,
    ): CustomInterface? {
        return customInterfaceDao.selectByApi(api,)
    }


}