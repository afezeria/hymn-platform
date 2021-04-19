package com.github.afezeria.hymn.core.module.controller


import com.github.afezeria.hymn.common.ann.Function
import com.github.afezeria.hymn.common.constant.AccountType
import com.github.afezeria.hymn.common.exception.ResourceNotFoundException
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dto.BizObjectTypeDto
import com.github.afezeria.hymn.core.module.entity.BizObjectType
import com.github.afezeria.hymn.core.module.service.BizObjectTypeService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * @author afezeria
 */
@RestController
@RequestMapping("/core/api/biz-object-type")
@Api(tags = ["BizObjectTypeController"], description = "业务对象记录类型接口")
class BizObjectTypeController {

    @Autowired
    private lateinit var bizObjectTypeService: BizObjectTypeService

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据对象id查询数据", notes = "")
    @GetMapping
    fun find(
        @RequestParam("biz_object_id") bizObjectId: String
    ): List<BizObjectType> {
        val list = bizObjectTypeService.findAvailableTypeByBizObjectId(bizObjectId)
        return list
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id查询", notes = "")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): BizObjectType {
        val entity = bizObjectTypeService.findAvailableById(id)
            ?: throw ResourceNotFoundException("业务对象记录类型".msgById(id))
        return entity
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: BizObjectTypeDto): String {
        val id = bizObjectTypeService.create(dto)
        return id
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: String,
        @RequestBody dto: BizObjectTypeDto
    ): Int {
        val count = bizObjectTypeService.update(id, dto)
        return count
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): Int {
        val count = bizObjectTypeService.removeById(id)
        return count
    }
}