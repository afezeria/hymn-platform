package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.ButtonPermDao
import github.afezeria.hymn.core.module.dto.ButtonPermDto
import github.afezeria.hymn.core.module.entity.ButtonPerm
import github.afezeria.hymn.core.module.service.ButtonPermService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class ButtonPermServiceImpl : ButtonPermService {

    @Autowired
    private lateinit var buttonPermDao: ButtonPermDao

    @Autowired
    private lateinit var dbService: DatabaseService


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

    override fun findAll(): MutableList<ButtonPerm> {
        return buttonPermDao.selectAll()
    }


    override fun findById(id: String): ButtonPerm? {
        return buttonPermDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<ButtonPerm> {
        return buttonPermDao.selectByIds(ids)
    }


    override fun findByRoleIdAndButtonId(
        roleId: String,
        buttonId: String,
    ): ButtonPerm? {
        return buttonPermDao.selectByRoleIdAndButtonId(roleId, buttonId)
    }

    override fun findByRoleId(
        roleId: String,
    ): MutableList<ButtonPerm> {
        return buttonPermDao.selectByRoleId(roleId)
    }

    override fun findByButtonId(
        buttonId: String,
    ): MutableList<ButtonPerm> {
        return buttonPermDao.selectByButtonId(buttonId)
    }

    override fun batchCreate(dtoList: List<ButtonPermDto>): MutableList<Int> {
        return buttonPermDao.batchInsert(dtoList.map { it.toEntity() })
    }

    override fun batchSave(dtoList: List<ButtonPermDto>): MutableList<Int> {
        return buttonPermDao.batchInsertOrUpdate(dtoList.map { it.toEntity() })
    }


}