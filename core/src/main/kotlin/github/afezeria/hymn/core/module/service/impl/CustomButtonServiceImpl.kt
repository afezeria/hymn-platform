package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.CustomButton
import github.afezeria.hymn.core.module.dao.CustomButtonDao
import github.afezeria.hymn.core.module.dto.CustomButtonDto
import github.afezeria.hymn.core.module.service.CustomButtonService
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author afezeria
 */
@Service
class CustomButtonServiceImpl : CustomButtonService {

    @Autowired
    private lateinit var customButtonDao: CustomButtonDao

    @Autowired
    private lateinit var dbService: DataBaseService


    override fun removeById(id: String): Int {
        customButtonDao.selectById(id)
            ?: throw DataNotFoundException("CustomButton".msgById(id))
        val i = customButtonDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: CustomButtonDto): Int {
        val e = customButtonDao.selectById(id)
            ?: throw DataNotFoundException("CustomButton".msgById(id))
        dto.update(e)
        val i = customButtonDao.update(e)
        return i
    }

    override fun create(dto: CustomButtonDto): String {
        val e = dto.toEntity()
        val id = customButtonDao.insert(e)
        return id
    }

    override fun findAll(): MutableList<CustomButton> {
        return customButtonDao.selectAll()
    }


    override fun findById(id: String): CustomButton? {
        return customButtonDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<CustomButton> {
        return customButtonDao.selectByIds(ids)
    }


    override fun findByApi(
        api: String,
    ): CustomButton? {
        return customButtonDao.selectByApi(api,)
    }


}