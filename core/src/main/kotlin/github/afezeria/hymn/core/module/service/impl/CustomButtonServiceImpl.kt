package github.afezeria.hymn.core.module.service.impl

import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dao.CustomButtonDao
import github.afezeria.hymn.core.module.dto.ButtonPermDto
import github.afezeria.hymn.core.module.dto.CustomButtonDto
import github.afezeria.hymn.core.module.entity.CustomButton
import github.afezeria.hymn.core.module.service.ButtonPermService
import github.afezeria.hymn.core.module.service.CustomButtonService
import github.afezeria.hymn.core.module.service.RoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class CustomButtonServiceImpl : CustomButtonService {

    @Autowired
    private lateinit var customButtonDao: CustomButtonDao

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var buttonPermService: ButtonPermService

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

//        更新按钮权限数据
        val allRoleId = roleService.findIdList()
        val roleIdSet = allRoleId.toMutableSet()
        val buttonPermDtoList = mutableListOf<ButtonPermDto>()
        dto.permList.forEach {
            if (roleIdSet.remove(it.roleId)) {
                it.buttonId = id
                buttonPermDtoList.add(it)
            }
        }
        roleIdSet.forEach {
            buttonPermDtoList.add(ButtonPermDto(it, id))
        }
        buttonPermService.batchSave(buttonPermDtoList)

        return i
    }

    override fun create(dto: CustomButtonDto): String {
        val e = dto.toEntity()
        val id = customButtonDao.insert(e)

//        创建按钮权限数据
        val allRoleId = roleService.findIdList()
        val roleIdSet = allRoleId.toMutableSet()
        val buttonPermDtoList = mutableListOf<ButtonPermDto>()
        dto.permList.forEach {
            if (roleIdSet.remove(it.roleId)) {
                it.buttonId = id
                buttonPermDtoList.add(it)
            }
        }
        roleIdSet.forEach {
            buttonPermDtoList.add(ButtonPermDto(it, id))
        }
        buttonPermService.batchCreate(buttonPermDtoList)

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
        return customButtonDao.selectByApi(api)
    }


}