package github.afezeria.hymn.oss.web.controller

import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.common.exception.BusinessException
import github.afezeria.hymn.common.exception.ResourceNotFoundException
import github.afezeria.hymn.common.platform.OssService
import github.afezeria.hymn.oss.contentType2Bucket
import github.afezeria.hymn.oss.filename2ContentType
import github.afezeria.hymn.oss.isValidFileName
import github.afezeria.hymn.oss.module.service.FileRecordService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import mu.KLogging
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.time.LocalDate
import javax.servlet.http.HttpServletResponse

/**
 * @author afezeria
 */
@Controller
@RequestMapping("oss/api/{version}")
@Api(tags = ["文件上传/下载接口"])
class FileController {
    companion object : KLogging()

    @Autowired
    private lateinit var ossService: OssService

    @Autowired
    private lateinit var fileRecordService: FileRecordService

    @PostMapping("tmp-file")
    @Function(AccountType.NORMAL, "tmp-file-upload")
    @ApiOperation(value = "上传临时文件", notes = "")
    @ResponseBody
    fun tmpFileUpload(
        @RequestParam("file") file: MultipartFile,
    ): String {
        return handler(file, true)
    }

    @PostMapping("file")
    @Function(AccountType.ADMIN, "oss.file-upload")
    @ApiOperation(value = "上传文件", notes = "需要管理员帐号和file-upload功能权限")
    @ResponseBody
    fun fileUpload(
        @RequestParam("file") file: MultipartFile,
    ): String {
        return handler(file, false)
    }


    fun handler(
        @RequestParam("file") file: MultipartFile,
        tmp: Boolean,
    ): String {
        if (file.isEmpty) throw BusinessException("上传文件内容为空")
        file.name.isValidFileName()
        val filename = file.originalFilename ?: throw BusinessException("缺少文件名")
        val contentType = filename2ContentType(filename)
        val bucket = contentType2Bucket(contentType)
        val today = LocalDate.now()
        val objectName =
            "${today.year}/${today.monthValue}/${today.dayOfMonth}/${filename}"
        return ossService.putObject(bucket, objectName, file.inputStream, contentType, tmp)
    }

    @GetMapping("file/{id}")
    @Function(accountType = AccountType.NORMAL, name = "")
    fun download(
        @PathVariable("id") id: String,
        response: HttpServletResponse
    ): StreamingResponseBody {
        val record = fileRecordService.findById(id)
            ?: throw ResourceNotFoundException("文件不存在")
        response.contentType = "application/octet-stream"
        response.setHeader(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment;filename=\"${record.fileName}\""
        )
        response.setContentLength(record.size)
        return StreamingResponseBody { o ->
            ossService.getObjectWithPerm(id) { i ->
                IOUtils.copy(i, o)
            }
        }
    }
}