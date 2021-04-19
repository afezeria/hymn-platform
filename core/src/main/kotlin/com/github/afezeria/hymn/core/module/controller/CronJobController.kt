package com.github.afezeria.hymn.core.module.controller


import com.github.afezeria.hymn.common.ann.Function
import com.github.afezeria.hymn.common.constant.AccountType
import com.github.afezeria.hymn.common.exception.ResourceNotFoundException
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.core.module.dto.CronJobDto
import com.github.afezeria.hymn.core.module.entity.CronJob
import com.github.afezeria.hymn.core.module.service.CronJobService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * @author afezeria
 */
@RestController
@RequestMapping("/core/api/cron-job")
@Api(tags = ["CronJobController"], description = "定时任务接口")
class CronJobController {

    @Autowired
    private lateinit var cronJobService: CronJobService

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "分页查询数据", notes = "")
    @GetMapping
    fun findAll(
        @RequestParam("pageSize", defaultValue = "50") pageSize: Int,
        @RequestParam("pageNum", defaultValue = "1") pageNum: Int,
    ): List<CronJob> {
        val list = cronJobService.pageFind(pageSize, pageNum)
        return list
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id查询", notes = "")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): CronJob {
        val entity = cronJobService.findById(id)
            ?: throw ResourceNotFoundException("定时任务".msgById(id))
        return entity
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: CronJobDto): String {
        val id = cronJobService.create(dto)
        return id
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: String,
        @RequestBody dto: CronJobDto
    ): Int {
        val count = cronJobService.update(id, dto)
        return count
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): Int {
        val count = cronJobService.removeById(id)
        return count
    }
}