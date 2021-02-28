package github.afezeria.hymn.core.module.controller


import github.afezeria.hymn.common.ann.ApiVersion
import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.common.exception.ResourceNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dto.CustomPageDto
import github.afezeria.hymn.core.module.entity.CustomPage
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * @author afezeria
 */
@ApiVersion
@RestController
@RequestMapping("/core/api/{version}/custom-page")
@Api(
    tags = ["CustomPageController"],
    description = "自定义页面，上传压缩包，解压后存放在工作目录的static-resource/{api}目录下,访问路径为接口"
)
class CustomPageController {

    @Autowired
    private lateinit var customPageService: CustomPageService

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "分页查询数据", notes = "")
    @GetMapping
    fun findAll(
        @RequestParam("pageSize", defaultValue = "50") pageSize: Int,
        @RequestParam("pageNum", defaultValue = "1") pageNum: Int,
    ): List<CustomPage> {
        val list = customPageService.pageFind(pageSize, pageNum)
        return list
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id查询", notes = "")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): CustomPage {
        val entity = customPageService.findById(id)
            ?: throw ResourceNotFoundException(
                "自定义页面，上传压缩包，解压后存放在工作目录的static-resource/{api}目录下,访问路径为".msgById(
                    id
                )
            )
        return entity
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: CustomPageDto): String {
        val id = customPageService.create(dto)
        return id
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: String,
        @RequestBody dto: CustomPageDto
    ): Int {
        val count = customPageService.update(id, dto)
        return count
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): Int {
        val count = customPageService.removeById(id)
        return count
    }
}