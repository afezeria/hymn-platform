package github.afezeria.hymn.core.web.controller

import github.afezeria.hymn.core.module.service.ModuleFunctionPermService
import github.afezeria.hymn.core.module.entity.ModuleFunctionPerm
import github.afezeria.hymn.core.module.dto.ModuleFunctionPermDto
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
@RequestMapping("/module/core/api/module-function-perm")
@Api(tags = ["模块功能权限表接口"])
class ModuleFunctionPermController {

    private lateinit var moduleFunctionPermService: ModuleFunctionPermService

    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "查询全部数据", notes = "")
    @GetMapping
    fun findAll(): ApiResp<List<ModuleFunctionPerm>> {
        val all = moduleFunctionPermService.findAll()
        return successApiResp(all)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "根据id查询", notes = "无参数时查询全部数据")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): ApiResp<ModuleFunctionPerm> {
        val entity = moduleFunctionPermService.findById(id)
            ?: throw DataNotFoundException("CoreAccount".msgById(id))
        return successApiResp(entity)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: ModuleFunctionPermDto): ApiResp<String> {
        val id = moduleFunctionPermService.create(dto)
        return successApiResp(id)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: String,
               @RequestBody dto: ModuleFunctionPermDto): ApiResp<Int> {
        val count = moduleFunctionPermService.update(id, dto)
        return successApiResp(count)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): ApiResp<Int> {
        val count = moduleFunctionPermService.removeById(id)
        return successApiResp(count)
    }
}