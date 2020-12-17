package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.Config
import github.afezeria.hymn.core.module.dao.ConfigDao
import github.afezeria.hymn.core.module.dto.ConfigDto
import github.afezeria.hymn.core.module.service.ConfigService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author afezeria
 */
@Service
class ConfigServiceImpl : ConfigService {

    @Autowired
    private lateinit var configDao: ConfigDao


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

    override fun findAll(): List<Config> {
        return configDao.selectAll()
    }


    override fun findById(id: String): Config? {
        return configDao.selectById(id)
    }

    override fun findByKey(
        key: String,
    ): List<Config> {
        return configDao.selectByKey(key,)
    }


}