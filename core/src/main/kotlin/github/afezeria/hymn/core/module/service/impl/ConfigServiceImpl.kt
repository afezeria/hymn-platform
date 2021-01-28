package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.ConfigDao
import github.afezeria.hymn.core.module.dto.ConfigDto
import github.afezeria.hymn.core.module.entity.Config
import github.afezeria.hymn.core.module.service.ConfigService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class ConfigServiceImpl : ConfigService {

    @Autowired
    private lateinit var configDao: ConfigDao

    @Autowired
    private lateinit var dbService: DatabaseService


    override fun removeById(id: String): Int {
        configDao.selectById(id)
            ?: throw DataNotFoundException("Config".msgById(id))
        val i = configDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: ConfigDto): Int {
        val e = configDao.selectById(id)
            ?: throw DataNotFoundException("Config".msgById(id))
        dto.update(e)
        val i = configDao.update(e)
        return i
    }

    override fun create(dto: ConfigDto): String {
        val e = dto.toEntity()
        val id = configDao.insert(e)
        return id
    }

    override fun findAll(): MutableList<Config> {
        return configDao.selectAll()
    }


    override fun findById(id: String): Config? {
        return configDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<Config> {
        return configDao.selectByIds(ids)
    }


    override fun findByKey(
        key: String,
    ): MutableList<Config> {
        return configDao.selectByKey(key)
    }


}