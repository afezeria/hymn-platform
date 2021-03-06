package com.github.afezeria.hymn.core.module.controller


import com.github.afezeria.hymn.common.ann.Function
import com.github.afezeria.hymn.common.constant.AccountType
import com.github.afezeria.hymn.common.exception.ResourceNotFoundException
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dto.BizObjectLayoutDto
import com.github.afezeria.hymn.core.module.entity.BizObjectLayout
import com.github.afezeria.hymn.core.module.service.BizObjectLayoutService
import com.github.afezeria.hymn.core.module.view.BizObjectLayoutListView
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * @author afezeria
 */
@RestController
@RequestMapping("/core/api/biz-object-layout")
@Api(tags = ["BizObjectLayoutController"], description = "业务对象详情页面布局接口")
class BizObjectLayoutController {

    @Autowired
    private lateinit var bizObjectLayoutService: BizObjectLayoutService

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据对象id查询页面布局", notes = "")
    @GetMapping
    fun find(
        @RequestParam("biz_object_id") bizObjectId: String,
    ): List<BizObjectLayoutListView> {
        return bizObjectLayoutService.findListViewByBizObjectId(bizObjectId)
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id查询", notes = "")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): BizObjectLayout {
        val entity = bizObjectLayoutService.findById(id)
            ?: throw ResourceNotFoundException("业务对象详情页面布局".msgById(id))
        return entity
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: BizObjectLayoutDto): String {
        val id = bizObjectLayoutService.create(dto)
        return id
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: String,
        @RequestBody dto: BizObjectLayoutDto
    ): Int {
        val count = bizObjectLayoutService.update(id, dto)
        return count
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): Int {
        val count = bizObjectLayoutService.removeById(id)
        return count
    }


}