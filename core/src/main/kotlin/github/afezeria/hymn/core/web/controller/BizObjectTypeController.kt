package github.afezeria.hymn.core.web.controller

import github.afezeria.hymn.core.module.service.BizObjectTypeService
import github.afezeria.hymn.core.module.entity.BizObjectType
import github.afezeria.hymn.core.module.dto.BizObjectTypeDto
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
@RequestMapping("/module/core/api/biz-object-type")
@Api(tags = ["业务对象记录类型接口"])
class BizObjectTypeController {

    private lateinit var bizObjectTypeService: BizObjectTypeService

    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "查询全部数据", notes = "")
    @GetMapping
    fun findAll(): ApiResp<List<BizObjectType>> {
        val all = bizObjectTypeService.findAll()
        return successApiResp(all)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "根据id查询", notes = "无参数时查询全部数据")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): ApiResp<BizObjectType> {
        val entity = bizObjectTypeService.findById(id)
            ?: throw DataNotFoundException("CoreAccount".msgById(id))
        return successApiResp(entity)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: BizObjectTypeDto): ApiResp<String> {
        val id = bizObjectTypeService.create(dto)
        return successApiResp(id)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: String,
               @RequestBody dto: BizObjectTypeDto): ApiResp<Int> {
        val count = bizObjectTypeService.update(id, dto)
        return successApiResp(count)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): ApiResp<Int> {
        val count = bizObjectTypeService.removeById(id)
        return successApiResp(count)
    }
}