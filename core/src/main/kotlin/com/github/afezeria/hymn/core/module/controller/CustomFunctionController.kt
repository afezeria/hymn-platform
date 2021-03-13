package com.github.afezeria.hymn.core.module.controller


import com.github.afezeria.hymn.common.ann.ApiVersion
import com.github.afezeria.hymn.common.ann.Function
import com.github.afezeria.hymn.common.constant.AccountType
import com.github.afezeria.hymn.common.exception.ResourceNotFoundException
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dto.CustomFunctionDto
import com.github.afezeria.hymn.core.module.entity.CustomFunction
import com.github.afezeria.hymn.core.module.service.CustomFunctionService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * @author afezeria
 */
@ApiVersion
@RestController
@RequestMapping("/core/api/{version}/custom-function")
@Api(tags = ["CustomFunctionController"], description = "自定义函数接口")
class CustomFunctionController {

    @Autowired
    private lateinit var customFunctionService: CustomFunctionService

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "分页查询数据", notes = "")
    @GetMapping
    fun findAll(
        @RequestParam("pageSize", defaultValue = "50") pageSize: Int,
        @RequestParam("pageNum", defaultValue = "1") pageNum: Int,
    ): List<CustomFunction> {
        val list = customFunctionService.pageFind(pageSize, pageNum)
        return list
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id查询", notes = "")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): CustomFunction {
        val entity = customFunctionService.findById(id)
            ?: throw ResourceNotFoundException("共享代码".msgById(id))
        return entity
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: CustomFunctionDto): String {
        val id = customFunctionService.create(dto)
        return id
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: String,
        @RequestBody dto: CustomFunctionDto
    ): Int {
        val count = customFunctionService.update(id, dto)
        return count
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): Int {
        val count = customFunctionService.removeById(id)
        return count
    }
}