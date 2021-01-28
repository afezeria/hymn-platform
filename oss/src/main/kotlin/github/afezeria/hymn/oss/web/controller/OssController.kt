package github.afezeria.hymn.oss.web.controller

import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.common.platform.OssService
import github.afezeria.hymn.common.util.BusinessException
import github.afezeria.hymn.common.util.isValidFileName
import github.afezeria.hymn.oss.contentType2Bucket
import github.afezeria.hymn.oss.postfix2ContentType
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

/**
 * @author afezeria
 */
@Controller
@RequestMapping("oss/file")
class OssController {
    companion object:KLogging()
    @Autowired
    private lateinit var ossService: OssService

    @PostMapping("/tmp")
    @Function(AccountType.NORMAL, "tmp-file-upload")
    fun tmpFileUpload(
        @RequestParam("file") file: MultipartFile,
    ): String {
        logger.info { "upload tmp file" }
        if (file.isEmpty) throw BusinessException("上传文件内容为空")
        file.name.isValidFileName()
        val contentType = postfix2ContentType(file.name)
        val bucket = contentType2Bucket(contentType)
        val today = LocalDate.now()
        val objectName = "${today.year}/${today.monthValue}/${today.dayOfMonth}/${file.name}"
        return ossService.putObject(bucket, objectName, file.inputStream, contentType)
    }

    @PostMapping
    @Function(AccountType.ADMIN, "file-upload")
    fun fileUpload(
        @RequestParam("file") file: MultipartFile,
    ): String {
        if (file.isEmpty) throw BusinessException("上传文件内容为空")
        file.name.isValidFileName()
        val contentType = postfix2ContentType(file.name)
        val bucket = contentType2Bucket(contentType)
        val today = LocalDate.now()
        val objectName = "${today.year}/${today.monthValue}/${today.dayOfMonth}/${file.name}"
        return ossService.putObject(bucket, objectName, file.inputStream, contentType, false)
    }

}