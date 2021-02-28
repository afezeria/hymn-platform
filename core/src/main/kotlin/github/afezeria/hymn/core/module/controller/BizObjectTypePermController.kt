package github.afezeria.hymn.core.module.controller


import github.afezeria.hymn.common.ann.ApiVersion
import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.common.exception.ResourceNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dto.BizObjectTypePermDto
import github.afezeria.hymn.core.module.entity.BizObjectTypePerm
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * @author afezeria
 */
@ApiVersion
@RestController
@RequestMapping("/core/api/{version}/biz-object-type-perm")
@Api(tags = ["BizObjectTypePermController"], description = "记录类型权限接口")
class BizObjectTypePermController {

    @Autowired
    private lateinit var bizObjectTypePermService: BizObjectTypePermService

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "分页查询数据", notes = "")
    @GetMapping
    fun findAll(
        @RequestParam("pageSize", defaultValue = "50") pageSize: Int,
        @RequestParam("pageNum", defaultValue = "1") pageNum: Int,
    ): List<BizObjectTypePerm> {
        val list = bizObjectTypePermService.pageFind(pageSize, pageNum)
        return list
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id查询", notes = "")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): BizObjectTypePerm {
        val entity = bizObjectTypePermService.findById(id)
            ?: throw ResourceNotFoundException("记录类型权限".msgById(id))
        return entity
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: BizObjectTypePermDto): String {
        val id = bizObjectTypePermService.create(dto)
        return id
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: String,
        @RequestBody dto: BizObjectTypePermDto
    ): Int {
        val count = bizObjectTypePermService.update(id, dto)
        return count
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): Int {
        val count = bizObjectTypePermService.removeById(id)
        return count
    }
}