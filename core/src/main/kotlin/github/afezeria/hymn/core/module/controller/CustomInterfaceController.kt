package github.afezeria.hymn.core.module.controller


import github.afezeria.hymn.common.ann.ApiVersion
import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.common.exception.ResourceNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dto.CustomInterfaceDto
import github.afezeria.hymn.core.module.entity.CustomInterface
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * @author afezeria
 */
@ApiVersion
@RestController
@RequestMapping("/core/api/{version}/custom-interface")
@Api(tags = ["CustomInterfaceController"], description = "自定义接口接口")
class CustomInterfaceController {

    @Autowired
    private lateinit var customInterfaceService: CustomInterfaceService

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "分页查询数据", notes = "")
    @GetMapping
    fun findAll(
        @RequestParam("pageSize", defaultValue = "50") pageSize: Int,
        @RequestParam("pageNum", defaultValue = "1") pageNum: Int,
    ): List<CustomInterface> {
        val list = customInterfaceService.pageFind(pageSize, pageNum)
        return list
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id查询", notes = "")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): CustomInterface {
        val entity = customInterfaceService.findById(id)
            ?: throw ResourceNotFoundException("自定义接口".msgById(id))
        return entity
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: CustomInterfaceDto): String {
        val id = customInterfaceService.create(dto)
        return id
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: String,
        @RequestBody dto: CustomInterfaceDto
    ): Int {
        val count = customInterfaceService.update(id, dto)
        return count
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): Int {
        val count = customInterfaceService.removeById(id)
        return count
    }
}