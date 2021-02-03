package github.afezeria.hymn.oss.module.web

import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.oss.module.entity.PreSignedHistory
import github.afezeria.hymn.oss.module.service.PreSignedHistoryService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime


/**
 * @author afezeria
 */
@RestController
@RequestMapping("oss/api/pre-signed-history")
@Api(tags = ["创建文件预签url记录接口"])
class PreSignedHistoryController {

    @Autowired
    private lateinit var preSignedHistoryService: PreSignedHistoryService

    @Function(AccountType.ADMIN)
    @ApiOperation(value = "查询全部数据", notes = "fileId存在时忽略startDate和endDate")
    @GetMapping
    fun find(
        @RequestParam("fileId", required = false) fileId: String?,
        @RequestParam("startDate", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        startDate: LocalDateTime?,
        @RequestParam("endDate", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        endDate: LocalDateTime?,
        @RequestParam("pageSize", defaultValue = "50") pageSize: Int,
        @RequestParam("pageNum", defaultValue = "1") pageNum: Int,
    ): List<PreSignedHistory> {
        return if (fileId != null) {
            preSignedHistoryService.findByFileId(fileId)
        } else {
            preSignedHistoryService.pageFindBetweenCreateDate(
                startDate,
                endDate,
                pageSize,
                pageNum
            )
        }
    }


    @Function(AccountType.ADMIN)
    @ApiOperation(value = "根据id列表删除", notes = "")
    @DeleteMapping
    fun deleteByIds(
        @RequestParam("ids") ids: List<String>,
    ): Int {
        return preSignedHistoryService.removeByIds(ids)
    }
}