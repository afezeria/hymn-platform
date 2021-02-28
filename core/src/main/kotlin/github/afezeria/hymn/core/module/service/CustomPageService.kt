package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.CustomPageDao
import github.afezeria.hymn.core.module.dto.CustomPageDto
import github.afezeria.hymn.core.module.entity.CustomPage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class CustomPageService {

    @Autowired
    private lateinit var customPageDao: CustomPageDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        customPageDao.selectById(id)
            ?: throw DataNotFoundException("CustomPage".msgById(id))
        val i = customPageDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: CustomPageDto): Int {
        val e = customPageDao.selectById(id)
            ?: throw DataNotFoundException("CustomPage".msgById(id))
        dto.update(e)
        val i = customPageDao.update(e)
        return i
    }

    fun create(dto: CustomPageDto): String {
        val e = dto.toEntity()
        val id = customPageDao.insert(e)
        return id
    }

    fun findAll(): MutableList<CustomPage> {
        return customPageDao.selectAll()
    }


    fun findById(id: String): CustomPage? {
        return customPageDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<CustomPage> {
        return customPageDao.selectByIds(ids)
    }


    fun findByApi(
        api: String,
    ): CustomPage? {
        return customPageDao.selectByApi(api)
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<CustomPage> {
        return customPageDao.pageSelect(null, pageSize, pageNum)
    }


}