package github.afezeria.hymn.core.web.controller

import github.afezeria.hymn.core.module.service.DictService
import github.afezeria.hymn.core.module.entity.Dict
import github.afezeria.hymn.core.module.dto.DictDto
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
@RequestMapping("/module/core/api/dict")
@Api(tags = ["数据字典接口"])
class DictController {

    private lateinit var dictService: DictService

    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "查询全部数据", notes = "")
    @GetMapping
    fun findAll(): ApiResp<List<Dict>> {
        val all = dictService.findAll()
        return successApiResp(all)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "根据id查询", notes = "无参数时查询全部数据")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): ApiResp<Dict> {
        val entity = dictService.findById(id)
            ?: throw DataNotFoundException("CoreAccount".msgById(id))
        return successApiResp(entity)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: DictDto): ApiResp<String> {
        val id = dictService.create(dto)
        return successApiResp(id)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: String,
               @RequestBody dto: DictDto): ApiResp<Int> {
        val count = dictService.update(id, dto)
        return successApiResp(count)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): ApiResp<Int> {
        val count = dictService.removeById(id)
        return successApiResp(count)
    }
}