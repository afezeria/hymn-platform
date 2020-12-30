package github.afezeria.hymn.core.web.controller

import github.afezeria.hymn.core.module.service.BizObjectService
import github.afezeria.hymn.core.module.entity.BizObject
import github.afezeria.hymn.core.module.dto.BizObjectDto
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
@RequestMapping("/module/core/api/biz-object")
@Api(tags = ["业务对象接口"])
class BizObjectController {

    private lateinit var bizObjectService: BizObjectService

    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "查询全部数据", notes = "")
    @GetMapping
    fun findAll(): ApiResp<List<BizObject>> {
        val all = bizObjectService.findAll()
        return successApiResp(all)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "根据id查询", notes = "无参数时查询全部数据")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): ApiResp<BizObject> {
        val entity = bizObjectService.findById(id)
            ?: throw DataNotFoundException("CoreAccount".msgById(id))
        return successApiResp(entity)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: BizObjectDto): ApiResp<String> {
        val id = bizObjectService.create(dto)
        return successApiResp(id)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: String,
               @RequestBody dto: BizObjectDto): ApiResp<Int> {
        val count = bizObjectService.update(id, dto)
        return successApiResp(count)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): ApiResp<Int> {
        val count = bizObjectService.removeById(id)
        return successApiResp(count)
    }
}