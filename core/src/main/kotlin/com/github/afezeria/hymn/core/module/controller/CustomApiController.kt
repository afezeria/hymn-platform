package com.github.afezeria.hymn.core.module.controller


import com.github.afezeria.hymn.common.ann.Function
import com.github.afezeria.hymn.common.constant.AccountType
import com.github.afezeria.hymn.common.exception.ResourceNotFoundException
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dto.CustomApiDto
import com.github.afezeria.hymn.core.module.entity.CustomApi
import com.github.afezeria.hymn.core.module.service.CustomApiService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * @author afezeria
 */
@RestController
@RequestMapping("/core/api/custom-api")
@Api(tags = ["CustomApiController"], description = "自定义接口接口")
class CustomApiController {

    @Autowired
    private lateinit var customApiService: CustomApiService

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "分页查询数据", notes = "")
    @GetMapping
    fun findAll(
        @RequestParam("pageSize", defaultValue = "50") pageSize: Int,
        @RequestParam("pageNum", defaultValue = "1") pageNum: Int,
    ): List<CustomApi> {
        val list = customApiService.pageFind(pageSize, pageNum)
        return list
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id查询", notes = "")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): CustomApi {
        val entity = customApiService.findById(id)
            ?: throw ResourceNotFoundException("自定义接口".msgById(id))
        return entity
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: CustomApiDto): String {
        val id = customApiService.create(dto)
        return id
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: String,
        @RequestBody dto: CustomApiDto
    ): Int {
        val count = customApiService.update(id, dto)
        return count
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): Int {
        val count = customApiService.removeById(id)
        return count
    }
}