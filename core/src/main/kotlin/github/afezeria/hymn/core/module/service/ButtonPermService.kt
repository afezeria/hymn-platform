package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.ButtonPermDao
import github.afezeria.hymn.core.module.dto.ButtonPermDto
import github.afezeria.hymn.core.module.entity.ButtonPerm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class ButtonPermService {

    @Autowired
    private lateinit var buttonPermDao: ButtonPermDao

    @Autowired
    private lateinit var dbService: DatabaseService


    fun removeById(id: String): Int {
        buttonPermDao.selectById(id)
            ?: throw DataNotFoundException("ButtonPerm".msgById(id))
        val i = buttonPermDao.deleteById(id)
        return i
    }

    fun update(id: String, dto: ButtonPermDto): Int {
        val e = buttonPermDao.selectById(id)
            ?: throw DataNotFoundException("ButtonPerm".msgById(id))
        dto.update(e)
        val i = buttonPermDao.update(e)
        return i
    }

    fun create(dto: ButtonPermDto): String {
        val e = dto.toEntity()
        val id = buttonPermDao.insert(e)
        return id
    }

    fun findAll(): MutableList<ButtonPerm> {
        return buttonPermDao.selectAll()
    }


    fun findById(id: String): ButtonPerm? {
        return buttonPermDao.selectById(id)
    }

    fun findByIds(ids: List<String>): MutableList<ButtonPerm> {
        return buttonPermDao.selectByIds(ids)
    }


    fun findByRoleIdAndButtonId(
        roleId: String,
        buttonId: String,
    ): ButtonPerm? {
        return buttonPermDao.selectByRoleIdAndButtonId(roleId, buttonId)
    }

    fun findByRoleId(
        roleId: String,
    ): MutableList<ButtonPerm> {
        return buttonPermDao.selectByRoleId(roleId)
    }

    fun findByButtonId(
        buttonId: String,
    ): MutableList<ButtonPerm> {
        return buttonPermDao.selectByButtonId(buttonId)
    }

    fun batchCreate(dtoList: List<ButtonPermDto>): Int {
        return buttonPermDao.bulkInsert(dtoList.map { it.toEntity() })
    }

    fun batchSave(dtoList: List<ButtonPermDto>): Int {
        return buttonPermDao.bulkInsertOrUpdate(dtoList.map { it.toEntity() })
    }

    fun pageFind(pageSize: Int, pageNum: Int): List<ButtonPerm> {
        return buttonPermDao.pageSelect(null, pageSize, pageNum)
    }


}