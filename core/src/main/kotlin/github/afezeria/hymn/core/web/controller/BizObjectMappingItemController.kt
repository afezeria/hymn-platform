package github.afezeria.hymn.core.web.controller

import github.afezeria.hymn.core.module.service.BizObjectMappingItemService
import github.afezeria.hymn.core.module.entity.BizObjectMappingItem
import github.afezeria.hymn.core.module.dto.BizObjectMappingItemDto
import github.afezeria.hymn.common.util.*
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.common.util.DataNotFoundException
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import io.swagger.annotations.*
import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import java.util.*


/**
 * @author afezeria
 */
@RestController
@RequestMapping("/module/core/api/biz-object-mapping-item")
@Api(tags = ["对象映射关系表明细接口"])
class BizObjectMappingItemController {

    private lateinit var bizObjectMappingItemService: BizObjectMappingItemService

    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "查询全部数据", notes = "")
    @GetMapping
    fun findAll(): ApiResp<List<BizObjectMappingItem>> {
        val all = bizObjectMappingItemService.findAll()
        return successApiResp(all)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "根据id查询", notes = "无参数时查询全部数据")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): ApiResp<BizObjectMappingItem> {
        val entity = bizObjectMappingItemService.findById(id)
            ?: throw DataNotFoundException("CoreAccount".msgById(id))
        return successApiResp(entity)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: BizObjectMappingItemDto): ApiResp<String> {
        val id = bizObjectMappingItemService.create(dto)
        return successApiResp(id)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: String,
               @RequestBody dto: BizObjectMappingItemDto): ApiResp<Int> {
        val count = bizObjectMappingItemService.update(id, dto)
        return successApiResp(count)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): ApiResp<Int> {
        val count = bizObjectMappingItemService.removeById(id)
        return successApiResp(count)
    }
}