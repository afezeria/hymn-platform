package com.github.afezeria.hymn.core.module.controller


import com.github.afezeria.hymn.common.ann.Function
import com.github.afezeria.hymn.common.constant.AccountType
import com.github.afezeria.hymn.common.exception.ResourceNotFoundException
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dto.BizObjectDto
import com.github.afezeria.hymn.core.module.entity.BizObject
import com.github.afezeria.hymn.core.module.service.BizObjectService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * @author afezeria
 */
@RestController
@RequestMapping("/core/api/biz-object")
@Api(tags = ["BizObjectController"], description = "业务对象接口")
class BizObjectController {

    @Autowired
    private lateinit var bizObjectService: BizObjectService

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "分页查询数据", notes = "active 为 false 时返回所有未启用的对象，忽略pageSize和pageNum参数")
    @GetMapping
    fun findAll(
        @RequestParam("pageSize", defaultValue = "50") pageSize: Int,
        @RequestParam("pageNum", defaultValue = "1") pageNum: Int,
        @RequestParam("active", defaultValue = "true") active: Boolean,
    ): List<BizObject> {
        if (active) {
            return bizObjectService.pageFind(pageSize, pageNum)
        } else {
            return bizObjectService.findAllInactiveObject()
        }
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id查询", notes = "")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): BizObject {
        val entity = bizObjectService.findActiveObjectById(id)
            ?: throw ResourceNotFoundException("业务对象".msgById(id))
        return entity
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "停用对象", notes = "")
    @GetMapping("/{id}/inactivate")
    fun inactivateObject(@PathVariable("id") id: String): Int {
        return bizObjectService.inactivateById(id)
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "启用对象", notes = "")
    @GetMapping("/{id}/activate")
    fun activateObject(@PathVariable("id") id: String): Int {
        return bizObjectService.activateById(id)
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: BizObjectDto): String {
        val id = bizObjectService.create(dto)
        return id
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: String,
        @RequestBody dto: BizObjectDto
    ): Int {
        val count = bizObjectService.update(id, dto)
        return count
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): Int {
        val count = bizObjectService.removeById(id)
        return count
    }
}