package github.afezeria.hymn.core.module.controller


import github.afezeria.hymn.common.ann.ApiVersion
import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.common.exception.ResourceNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dto.BusinessCodeRefDto
import github.afezeria.hymn.core.module.entity.BusinessCodeRef
import github.afezeria.hymn.core.module.service.BusinessCodeRefService
import github.afezeria.hymn.core.module.view.BusinessCodeRefListView
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * @author afezeria
 */
@ApiVersion
@RestController
@RequestMapping("/core/api/{version}/business-code-ref")
@Api(tags = ["BusinessCodeRefController"], description = "业务代码引用关系表接口")
class BusinessCodeRefController {

    @Autowired
    private lateinit var businessCodeRefService: BusinessCodeRefService

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "分页查询数据", notes = "")
    @GetMapping
    fun findAll(
        @RequestParam("byTriggerId", required = false) byTriggerId: String? = null,
        @RequestParam("byInterfaceId", required = false) byInterfaceId: String? = null,
        @RequestParam("byCustomFunctionId", required = false) byCustomFunctionId: String? = null,
        @RequestParam("bizObjectId", required = false) bizObjectId: String? = null,
        @RequestParam("fieldId", required = false) fieldId: String? = null,
        @RequestParam("customFunctionId", required = false) customFunctionId: String? = null,
        @RequestParam("pageSize", defaultValue = "50") pageSize: Int,
        @RequestParam("pageNum", defaultValue = "1") pageNum: Int,
    ): List<BusinessCodeRefListView> {
        return businessCodeRefService.pageFindView(
            byTriggerId,
            byInterfaceId,
            byCustomFunctionId,
            bizObjectId,
            fieldId,
            customFunctionId,
            pageSize,
            pageNum
        )
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id查询", notes = "")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): BusinessCodeRef {
        val entity = businessCodeRefService.findById(id)
            ?: throw ResourceNotFoundException("业务代码引用关系表".msgById(id))
        return entity
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: BusinessCodeRefDto): String {
        val id = businessCodeRefService.create(dto)
        return id
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): Int {
        val count = businessCodeRefService.removeById(id)
        return count
    }
}