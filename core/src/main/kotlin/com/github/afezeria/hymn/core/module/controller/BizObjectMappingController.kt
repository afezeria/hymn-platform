package com.github.afezeria.hymn.core.module.controller


import com.github.afezeria.hymn.common.ann.ApiVersion
import com.github.afezeria.hymn.common.ann.Function
import com.github.afezeria.hymn.common.constant.AccountType
import com.github.afezeria.hymn.common.exception.ResourceNotFoundException
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dto.BizObjectMappingDto
import com.github.afezeria.hymn.core.module.service.BizObjectMappingService
import com.github.afezeria.hymn.core.module.view.BizObjectMappingListView
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * @author afezeria
 */
@ApiVersion
@RestController
@RequestMapping("/core/api/{version}/biz-object-mapping")
@Api(tags = ["BizObjectMappingController"], description = "对象映射关系接口")
class BizObjectMappingController {

    @Autowired
    private lateinit var bizObjectMappingService: BizObjectMappingService

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "分页查询数据", notes = "")
    @GetMapping
    fun findAll(
        @RequestParam("source_biz_object_id", required = false) sourceBizObjectId: String?,
        @RequestParam("target_biz_object_id", required = false) targetBizObjectId: String?,
        @RequestParam("pageSize", defaultValue = "50") pageSize: Int,
        @RequestParam("pageNum", defaultValue = "1") pageNum: Int,
    ): MutableList<BizObjectMappingListView> {
        return bizObjectMappingService.pageFindView(
            sourceBizObjectId,
            targetBizObjectId,
            pageSize,
            pageNum
        )
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id查询", notes = "")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): BizObjectMappingListView {
        val result = bizObjectMappingService.findViewById(id)
            ?: throw ResourceNotFoundException("对象映射关系".msgById(id))
        return result
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: BizObjectMappingDto): String {
        val id = bizObjectMappingService.create(dto)
        return id
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): Int {
        val count = bizObjectMappingService.removeById(id)
        return count
    }
}