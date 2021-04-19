package com.github.afezeria.hymn.core.module.controller


import com.github.afezeria.hymn.common.ann.Function
import com.github.afezeria.hymn.common.constant.AccountType
import com.github.afezeria.hymn.common.exception.ResourceNotFoundException
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dto.BizObjectFieldDto
import com.github.afezeria.hymn.core.module.entity.BizObjectField
import com.github.afezeria.hymn.core.module.service.BizObjectFieldService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * @author afezeria
 */
@RestController
@RequestMapping("/core/api/biz-object-field")
@Api(tags = ["BizObjectFieldController"], description = "业务对象字段接口")
class BizObjectFieldController {

    @Autowired
    private lateinit var bizObjectFieldService: BizObjectFieldService

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id查询", notes = "")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): BizObjectField {
        val entity = bizObjectFieldService.findById(id)
            ?: throw ResourceNotFoundException("业务对象字段".msgById(id))
        return entity
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: BizObjectFieldDto): String {
        val id = bizObjectFieldService.create(dto)
        return id
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "停用字段", notes = "")
    @GetMapping("/{id}/inactivate")
    fun inactivateObject(@PathVariable("id") id: String): Int {
        return bizObjectFieldService.inactivateById(id)
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "启用启用", notes = "")
    @GetMapping("/{id}/activate")
    fun activateObject(@PathVariable("id") id: String): Int {
        return bizObjectFieldService.activateById(id)
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: String,
        @RequestBody dto: BizObjectFieldDto
    ): Int {
        val count = bizObjectFieldService.update(id, dto)
        return count
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): Int {
        val count = bizObjectFieldService.removeById(id)
        return count
    }
}