package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.ButtonPerm
import github.afezeria.hymn.core.module.dao.ButtonPermDao
import github.afezeria.hymn.core.module.dto.ButtonPermDto
import github.afezeria.hymn.core.module.service.ButtonPermService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author afezeria
 */
@Service
class ButtonPermServiceImpl : ButtonPermService {

    @Autowired
    private lateinit var buttonPermDao: ButtonPermDao


    override fun removeById(id: String): Int {
        buttonPermDao.selectById(id)
            ?: throw DataNotFoundException("ButtonPerm".msgById(id))
        val i = buttonPermDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: ButtonPermDto): Int {
        val e = buttonPermDao.selectById(id)
            ?: throw DataNotFoundException("ButtonPerm".msgById(id))
        dto.update(e)
        val i = buttonPermDao.update(e)
        return i
    }

    override fun create(dto: ButtonPermDto): String {
        val e = dto.toEntity()
        val id = buttonPermDao.insert(e)
        return id
    }

    override fun findAll(): List<ButtonPerm> {
        return buttonPermDao.selectAll()
    }


    override fun findById(id: String): ButtonPerm? {
        return buttonPermDao.selectById(id)
    }

    override fun findByRoleIdAndButtonId(
        roleId: String,
        buttonId: String,
    ): ButtonPerm? {
        return buttonPermDao.selectByRoleIdAndButtonId(roleId,buttonId,)
    }

    override fun findByRoleId(
        roleId: String,
    ): List<ButtonPerm> {
        return buttonPermDao.selectByRoleId(roleId,)
    }

    override fun findByButtonId(
        buttonId: String,
    ): List<ButtonPerm> {
        return buttonPermDao.selectByButtonId(buttonId,)
    }


}