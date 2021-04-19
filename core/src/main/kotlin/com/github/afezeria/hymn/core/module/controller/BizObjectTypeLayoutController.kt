package com.github.afezeria.hymn.core.module.controller


import com.github.afezeria.hymn.common.ann.Function
import com.github.afezeria.hymn.common.constant.AccountType
import com.github.afezeria.hymn.core.module.dto.BizObjectTypeLayoutDto
import com.github.afezeria.hymn.core.module.service.BizObjectTypeLayoutService
import com.github.afezeria.hymn.core.module.view.BizObjectTypeLayoutListView
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * @author afezeria
 */
@RestController
@RequestMapping("/core/api/biz-object-type-layout")
@Api(tags = ["BizObjectTypeLayoutController"], description = "业务对象的每个角色的 记录类型-页面布局 映射接口")
class BizObjectTypeLayoutController {

    @Autowired
    private lateinit var bizObjectTypeLayoutService: BizObjectTypeLayoutService

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "分页查询数据", notes = "")
    @GetMapping
    fun findAll(
        @RequestParam("biz_object_id") bizObjectId: String,
    ): MutableList<BizObjectTypeLayoutListView> {
        return bizObjectTypeLayoutService.findViewByBizObjectId(bizObjectId)
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "更新", notes = "")
    @PutMapping
    fun save(@RequestBody dtoList: List<BizObjectTypeLayoutDto>): Int {
        return bizObjectTypeLayoutService.save(dtoList)
    }
}