package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.CustomInterfaceDao
import github.afezeria.hymn.core.module.dto.CustomInterfaceDto
import github.afezeria.hymn.core.module.entity.CustomInterface
import github.afezeria.hymn.core.module.service.CustomInterfaceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class CustomInterfaceServiceImpl : CustomInterfaceService {

    @Autowired
    private lateinit var customInterfaceDao: CustomInterfaceDao

    @Autowired
    private lateinit var dbService: DatabaseService


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

    override fun findAll(): MutableList<CustomInterface> {
        return customInterfaceDao.selectAll()
    }


    override fun findById(id: String): CustomInterface? {
        return customInterfaceDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<CustomInterface> {
        return customInterfaceDao.selectByIds(ids)
    }


    override fun findByApi(
        api: String,
    ): CustomInterface? {
        return customInterfaceDao.selectByApi(api)
    }

    override fun pageFind(pageSize: Int, pageNum: Int): List<CustomInterface> {
        return customInterfaceDao.pageSelect(null, pageSize, pageNum)
    }


}