package github.afezeria.hymn.core.web.controller

import github.afezeria.hymn.core.module.service.BizObjectPermService
import github.afezeria.hymn.core.module.entity.BizObjectPerm
import github.afezeria.hymn.core.module.dto.BizObjectPermDto
import github.afezeria.hymn.common.util.*
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.common.util.DataNotFoundException
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import io.swagger.annotations.*
import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import java.util.*


/**
 * @author afezeria
 */
@RestController
@RequestMapping("/module/core/api/biz-object-perm")
@Api(tags = ["对象权限接口"])
class BizObjectPermController {

    private lateinit var bizObjectPermService: BizObjectPermService

    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "查询全部数据", notes = "")
    @GetMapping
    fun findAll(): ApiResp<List<BizObjectPerm>> {
        val all = bizObjectPermService.findAll()
        return successApiResp(all)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "根据id查询", notes = "无参数时查询全部数据")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): ApiResp<BizObjectPerm> {
        val entity = bizObjectPermService.findById(id)
            ?: throw DataNotFoundException("CoreAccount".msgById(id))
        return successApiResp(entity)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: BizObjectPermDto): ApiResp<String> {
        val id = bizObjectPermService.create(dto)
        return successApiResp(id)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: String,
               @RequestBody dto: BizObjectPermDto): ApiResp<Int> {
        val count = bizObjectPermService.update(id, dto)
        return successApiResp(count)
    }


    @Function(AccountType.ADMIN)
    @ApiOperation
(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): ApiResp<Int> {
        val count = bizObjectPermService.removeById(id)
        return successApiResp(count)
    }
}