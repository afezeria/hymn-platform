package github.afezeria.hymn.core.module.controller


import github.afezeria.hymn.common.ann.ApiVersion
import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.common.exception.ResourceNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.core.module.dto.BizObjectTriggerDto
import github.afezeria.hymn.core.module.entity.BizObjectTrigger
import github.afezeria.hymn.core.module.service.BizObjectTriggerService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * @author afezeria
 */
@ApiVersion
@RestController
@RequestMapping("/core/api/{version}/biz-object-trigger")
@Api(tags = ["BizObjectTriggerController"], description = "触发器接口")
class BizObjectTriggerController {

    @Autowired
    private lateinit var bizObjectTriggerService: BizObjectTriggerService

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据对象id查询", notes = "")
    @GetMapping
    fun find(
        @RequestParam("biz_object_id") bizObjectId: String,
    ): List<BizObjectTrigger> {
        val list = bizObjectTriggerService.findByBizObjectId(bizObjectId)
        return list
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id查询", notes = "")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): BizObjectTrigger {
        val entity = bizObjectTriggerService.findById(id)
            ?: throw ResourceNotFoundException("触发器".msgById(id))
        return entity
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: BizObjectTriggerDto): String {
        val id = bizObjectTriggerService.create(dto)
        return id
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: String,
        @RequestBody dto: BizObjectTriggerDto
    ): Int {
        val count = bizObjectTriggerService.update(id, dto)
        return count
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): Int {
        val count = bizObjectTriggerService.removeById(id)
        return count
    }
}