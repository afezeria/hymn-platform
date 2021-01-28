package github.afezeria.hymn.core.web.controller

import github.afezeria.hymn.core.module.service.BizObjectTypeLayoutService
import github.afezeria.hymn.core.module.entity.BizObjectTypeLayout
import github.afezeria.hymn.core.module.dto.BizObjectTypeLayoutDto
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
@RequestMapping("/module/core/api/biz-object-type-layout")
@Api(tags = ["业务对象记录类型、角色和页面布局关联表接口"])
class BizObjectTypeLayoutController {

    private lateinit var bizObjectTypeLayoutService: BizObjectTypeLayoutService

    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "查询全部数据", notes = "")
    @GetMapping
    fun findAll(): ApiResp<List<BizObjectTypeLayout>> {
        val all = bizObjectTypeLayoutService.findAll()
        return successApiResp(all)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "根据id查询", notes = "无参数时查询全部数据")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): ApiResp<BizObjectTypeLayout> {
        val entity = bizObjectTypeLayoutService.findById(id)
            ?: throw DataNotFoundException("CoreAccount".msgById(id))
        return successApiResp(entity)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: BizObjectTypeLayoutDto): ApiResp<String> {
        val id = bizObjectTypeLayoutService.create(dto)
        return successApiResp(id)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: String,
               @RequestBody dto: BizObjectTypeLayoutDto): ApiResp<Int> {
        val count = bizObjectTypeLayoutService.update(id, dto)
        return successApiResp(count)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): ApiResp<Int> {
        val count = bizObjectTypeLayoutService.removeById(id)
        return successApiResp(count)
    }
}