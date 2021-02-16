package github.afezeria.hymn.core.module.controller


import github.afezeria.hymn.common.ann.ApiVersion
import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.common.exception.ResourceNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dto.BizObjectTypeOptionsDto
import github.afezeria.hymn.core.module.entity.BizObjectTypeOptions
import github.afezeria.hymn.core.module.service.BizObjectTypeOptionsService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * @author afezeria
 */
@ApiVersion
@RestController
@RequestMapping("/core/api/{version}/biz-object-type-options")
@Api(tags = ["BizObjectTypeOptionsController"], description = "业务对象记录类型可选项限制接口")
class BizObjectTypeOptionsController {

    @Autowired
    private lateinit var bizObjectTypeOptionsService: BizObjectTypeOptionsService

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "分页查询数据", notes = "")
    @GetMapping
    fun findAll(
        @RequestParam("pageSize", defaultValue = "50") pageSize: Int,
        @RequestParam("pageNum", defaultValue = "1") pageNum: Int,
    ): List<BizObjectTypeOptions> {
        val list = bizObjectTypeOptionsService.pageFind(pageSize, pageNum)
        return list
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id查询", notes = "")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): BizObjectTypeOptions {
        val entity = bizObjectTypeOptionsService.findById(id)
            ?: throw ResourceNotFoundException("业务对象记录类型可选项限制".msgById(id))
        return entity
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: BizObjectTypeOptionsDto): String {
        val id = bizObjectTypeOptionsService.create(dto)
        return id
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: String,
        @RequestBody dto: BizObjectTypeOptionsDto
    ): Int {
        val count = bizObjectTypeOptionsService.update(id, dto)
        return count
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): Int {
        val count = bizObjectTypeOptionsService.removeById(id)
        return count
    }
}