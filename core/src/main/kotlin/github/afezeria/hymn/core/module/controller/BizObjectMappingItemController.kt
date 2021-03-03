package github.afezeria.hymn.core.module.controller


import github.afezeria.hymn.common.ann.ApiVersion
import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.core.module.dto.BizObjectMappingItemDto
import github.afezeria.hymn.core.module.entity.BizObjectMappingItem
import github.afezeria.hymn.core.module.service.BizObjectMappingItemService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * @author afezeria
 */
@ApiVersion
@RestController
@RequestMapping("/core/api/{version}/biz-object-mapping-item")
@Api(tags = ["BizObjectMappingItemController"], description = "对象映射关系表明细接口")
class BizObjectMappingItemController {

    @Autowired
    private lateinit var bizObjectMappingItemService: BizObjectMappingItemService

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据映射id查询", notes = "")
    @GetMapping
    fun findByMappingId(
        @RequestParam("mapping_id") mappingId: String,
    ): List<BizObjectMappingItem> {
        val list = bizObjectMappingItemService.findByMappingId(mappingId)
        return list
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "更新对象映射", notes = "一次只能更新一个对象映射表")
    @PostMapping
    fun save(@RequestBody list: List<BizObjectMappingItemDto>): Int {
        val count = bizObjectMappingItemService.save(list)
        return count
    }
}