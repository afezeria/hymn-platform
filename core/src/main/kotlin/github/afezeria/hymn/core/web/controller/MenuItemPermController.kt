package github.afezeria.hymn.core.web.controller

import github.afezeria.hymn.core.module.service.MenuItemPermService
import github.afezeria.hymn.core.module.entity.MenuItemPerm
import github.afezeria.hymn.core.module.dto.MenuItemPermDto
import github.afezeria.hymn.common.util.*
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.common.exception.DataNotFoundException
import org.springframework.web.bind.annotation.*
import io.swagger.annotations.*
import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType


/**
 * @author afezeria
 */
@RestController
@RequestMapping("/module/core/api/menu-item-perm")
@Api(tags = ["菜单项权限接口"])
class MenuItemPermController {

    private lateinit var menuItemPermService: MenuItemPermService

    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "查询全部数据", notes = "")
    @GetMapping
    fun findAll(): ApiResp<List<MenuItemPerm>> {
        val all = menuItemPermService.findAll()
        return successApiResp(all)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "根据id查询", notes = "无参数时查询全部数据")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): ApiResp<MenuItemPerm> {
        val entity = menuItemPermService.findById(id)
            ?: throw DataNotFoundException("CoreAccount".msgById(id))
        return successApiResp(entity)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: MenuItemPermDto): ApiResp<String> {
        val id = menuItemPermService.create(dto)
        return successApiResp(id)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: String,
               @RequestBody dto: MenuItemPermDto): ApiResp<Int> {
        val count = menuItemPermService.update(id, dto)
        return successApiResp(count)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): ApiResp<Int> {
        val count = menuItemPermService.removeById(id)
        return successApiResp(count)
    }
}