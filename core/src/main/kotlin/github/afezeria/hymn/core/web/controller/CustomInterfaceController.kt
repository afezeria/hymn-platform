package github.afezeria.hymn.core.web.controller

import github.afezeria.hymn.core.module.service.CustomInterfaceService
import github.afezeria.hymn.core.module.entity.CustomInterface
import github.afezeria.hymn.core.module.dto.CustomInterfaceDto
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
@RequestMapping("/module/core/api/custom-interface")
@Api(tags = ["自定义接口接口"])
class CustomInterfaceController {

    private lateinit var customInterfaceService: CustomInterfaceService

    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "查询全部数据", notes = "")
    @GetMapping
    fun findAll(): ApiResp<List<CustomInterface>> {
        val all = customInterfaceService.findAll()
        return successApiResp(all)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "根据id查询", notes = "无参数时查询全部数据")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): ApiResp<CustomInterface> {
        val entity = customInterfaceService.findById(id)
            ?: throw DataNotFoundException("CoreAccount".msgById(id))
        return successApiResp(entity)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: CustomInterfaceDto): ApiResp<String> {
        val id = customInterfaceService.create(dto)
        return successApiResp(id)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: String,
               @RequestBody dto: CustomInterfaceDto): ApiResp<Int> {
        val count = customInterfaceService.update(id, dto)
        return successApiResp(count)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): ApiResp<Int> {
        val count = customInterfaceService.removeById(id)
        return successApiResp(count)
    }
}