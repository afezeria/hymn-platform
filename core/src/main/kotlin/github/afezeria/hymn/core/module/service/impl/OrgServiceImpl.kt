package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.core.module.entity.Org
import github.afezeria.hymn.core.module.dao.OrgDao
import github.afezeria.hymn.core.module.dto.OrgDto
import github.afezeria.hymn.core.module.service.OrgService
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author afezeria
 */
@Service
class OrgServiceImpl : OrgService {

    @Autowired
    private lateinit var orgDao: OrgDao

    @Autowired
    private lateinit var dbService: DataBaseService


    override fun removeById(id: String): Int {
        orgDao.selectById(id)
            ?: throw DataNotFoundException("Org".msgById(id))
        val i = orgDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: OrgDto): Int {
        val e = orgDao.selectById(id)
            ?: throw DataNotFoundException("Org".msgById(id))
        dto.update(e)
        val i = orgDao.update(e)
        return i
    }

    override fun create(dto: OrgDto): String {
        val e = dto.toEntity()
        val id = orgDao.insert(e)
        return id
    }

    override fun findAll(): MutableList<Org> {
        return orgDao.selectAll()
    }


    override fun findById(id: String): Org? {
        return orgDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<Org> {
        return orgDao.selectByIds(ids)
    }


    override fun findByParentId(
        parentId: String,
    ): MutableList<Org> {
        return orgDao.selectByParentId(parentId,)
    }


}