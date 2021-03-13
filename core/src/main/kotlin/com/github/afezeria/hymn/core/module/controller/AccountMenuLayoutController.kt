package com.github.afezeria.hymn.core.module.controller


import com.github.afezeria.hymn.common.ann.ApiVersion
import com.github.afezeria.hymn.common.ann.Function
import com.github.afezeria.hymn.common.constant.AccountType
import com.github.afezeria.hymn.common.exception.ResourceNotFoundException
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dto.AccountMenuLayoutDto
import com.github.afezeria.hymn.core.module.entity.AccountMenuLayout
import com.github.afezeria.hymn.core.module.service.AccountMenuLayoutService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * @author afezeria
 */
@ApiVersion
@RestController
@RequestMapping("/core/api/{version}/account-menu-layout")
@Api(tags = ["AccountMenuLayoutController"], description = "用户侧边栏菜单布局接口")
class AccountMenuLayoutController {

    @Autowired
    private lateinit var accountMenuLayoutService: AccountMenuLayoutService

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "分页查询数据", notes = "")
    @GetMapping
    fun findAll(
        @RequestParam("pageSize", defaultValue = "50") pageSize: Int,
        @RequestParam("pageNum", defaultValue = "1") pageNum: Int,
    ): List<AccountMenuLayout> {
        val list = accountMenuLayoutService.pageFind(pageSize, pageNum)
        return list
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id查询", notes = "")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): AccountMenuLayout {
        val entity = accountMenuLayoutService.findById(id)
            ?: throw ResourceNotFoundException("用户侧边栏菜单布局".msgById(id))
        return entity
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: AccountMenuLayoutDto): String {
        val id = accountMenuLayoutService.create(dto)
        return id
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: String,
        @RequestBody dto: AccountMenuLayoutDto
    ): Int {
        val count = accountMenuLayoutService.update(id, dto)
        return count
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): Int {
        val count = accountMenuLayoutService.removeById(id)
        return count
    }
}