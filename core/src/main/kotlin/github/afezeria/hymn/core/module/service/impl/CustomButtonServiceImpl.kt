package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.CustomButton
import github.afezeria.hymn.core.module.dao.CustomButtonDao
import github.afezeria.hymn.core.module.dto.CustomButtonDto
import github.afezeria.hymn.core.module.service.CustomButtonService
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
    lateinit var customButtonDao: CustomButtonDao


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

    override fun findAll(): List<CustomButton> {
        return customButtonDao.selectAll()
    }


    override fun findById(id: String): CustomButton? {
        return customButtonDao.selectById(id)
    }

    override fun findByApi(
        api: String,
    ): CustomButton? {
        return customButtonDao.selectByApi(api,)
    }


}