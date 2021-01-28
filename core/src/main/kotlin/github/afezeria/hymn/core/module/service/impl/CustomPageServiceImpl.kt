package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.CustomPageDao
import github.afezeria.hymn.core.module.dto.CustomPageDto
import github.afezeria.hymn.core.module.entity.CustomPage
import github.afezeria.hymn.core.module.service.CustomPageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class CustomPageServiceImpl : CustomPageService {

    @Autowired
    private lateinit var customPageDao: CustomPageDao

    @Autowired
    private lateinit var dbService: DatabaseService


    override fun removeById(id: String): Int {
        customPageDao.selectById(id)
            ?: throw DataNotFoundException("CustomPage".msgById(id))
        val i = customPageDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: CustomPageDto): Int {
        val e = customPageDao.selectById(id)
            ?: throw DataNotFoundException("CustomPage".msgById(id))
        dto.update(e)
        val i = customPageDao.update(e)
        return i
    }

    override fun create(dto: CustomPageDto): String {
        val e = dto.toEntity()
        val id = customPageDao.insert(e)
        return id
    }

    override fun findAll(): MutableList<CustomPage> {
        return customPageDao.selectAll()
    }


    override fun findById(id: String): CustomPage? {
        return customPageDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<CustomPage> {
        return customPageDao.selectByIds(ids)
    }


    override fun findByApi(
        api: String,
    ): CustomPage? {
        return customPageDao.selectByApi(api)
    }


}