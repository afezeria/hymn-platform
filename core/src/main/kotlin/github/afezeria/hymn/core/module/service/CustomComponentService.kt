package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.CustomComponentDao
import github.afezeria.hymn.core.module.dto.CustomComponentDto
import github.afezeria.hymn.core.module.entity.CustomComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class CustomComponentService {

    @Autowired
    private lateinit var customComponentDao: CustomComponentDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        customComponentDao.selectById(id)
            ?: throw DataNotFoundException("CustomComponent".msgById(id))
        val i = customComponentDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: CustomComponentDto): Int {
        val e = customComponentDao.selectById(id)
            ?: throw DataNotFoundException("CustomComponent".msgById(id))
        dto.update(e)
        val i = customComponentDao.update(e)
        return i
    }

    fun create(dto: CustomComponentDto): String {
        val e = dto.toEntity()
        val id = customComponentDao.insert(e)
        return id
    }

    fun findAll(): MutableList<CustomComponent> {
        return customComponentDao.selectAll()
    }


    fun findById(id: String): CustomComponent? {
        return customComponentDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<CustomComponent> {
        return customComponentDao.selectByIds(ids)
    }


    fun findByApi(
        api: String,
    ): CustomComponent? {
        return customComponentDao.selectByApi(api)
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<CustomComponent> {
        return customComponentDao.pageSelect(null, pageSize, pageNum)
    }


}