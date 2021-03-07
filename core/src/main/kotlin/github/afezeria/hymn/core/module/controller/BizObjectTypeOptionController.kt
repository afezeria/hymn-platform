package github.afezeria.hymn.core.module.controller


import github.afezeria.hymn.common.ann.ApiVersion
import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.core.module.dto.BizObjectTypeFieldOptionDto
import github.afezeria.hymn.core.module.service.BizObjectTypeFieldOptionService
import github.afezeria.hymn.core.module.view.BizObjectTypeFieldOptionListView
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
class BizObjectTypeOptionController {

    @Autowired
    private lateinit var service: BizObjectTypeFieldOptionService

    @Function(AccountType.ADMIN)
    @ApiOperation(
        value = "查询业务类型可选值",
        notes = "field_id不为null时返回该类型下指定字段的限制选项，为null时返回所有字段的限制选项，限制选项列表中没有特定字段id的项时表示该类型中该字段可以选择所有值"
    )
    @GetMapping
    fun find(
        @RequestParam("type_id") typeId: String,
        @RequestParam("field_id", required = false) field: String?,
    ): List<BizObjectTypeFieldOptionListView> {
        return service.findView(typeId, field?.run { listOf(this) } ?: emptyList())
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "更新字段选项限制列表", notes = "一次只能更新一个类型的一个字段的列表")
    @PutMapping
    fun save(@RequestBody dto: List<BizObjectTypeFieldOptionDto>): Int {
        return service.save(dto)
    }
}