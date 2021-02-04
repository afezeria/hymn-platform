package github.afezeria.hymn.oss.module.web

import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.common.exception.ResourceNotFoundException
import github.afezeria.hymn.common.platform.OssService
import github.afezeria.hymn.oss.module.dto.FileRecordDto
import github.afezeria.hymn.oss.module.entity.FileRecord
import github.afezeria.hymn.oss.module.service.FileRecordService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * @author afezeria
 */
@RestController
@RequestMapping("oss/api/file-record")
@Api(tags = ["文件存储列表接口"])
class FileRecordController {

    @Autowired
    private lateinit var fileRecordService: FileRecordService

    @Autowired
    private lateinit var ossService: OssService

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "查询全部数据", notes = "")
    @GetMapping
    fun find(
        @RequestParam("pageSize", defaultValue = "50") pageSize: Int,
        @RequestParam("pageNum", defaultValue = "1") pageNum: Int,
        @RequestParam("fileName", required = false) fileName: String?,
    ): List<FileRecord> {
        val list = fileRecordService.pageFindByContainFileName(fileName, pageSize, pageNum)
        return list
    }


    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id查询")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): FileRecord {
        val entity = fileRecordService.findById(id)
            ?: throw ResourceNotFoundException("文件记录")
        return entity
    }

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: String,
        @RequestBody dto: FileRecordDto
    ): Int {
        val count = fileRecordService.update(id, dto)
        return count
    }


    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): Int {
        return ossService.removeObjectWithPerm(id)
    }
}