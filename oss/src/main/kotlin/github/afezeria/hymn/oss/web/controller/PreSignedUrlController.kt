package github.afezeria.hymn.oss.web.controller

import github.afezeria.hymn.common.exception.BusinessException
import github.afezeria.hymn.common.platform.CacheService
import github.afezeria.hymn.common.platform.OssService
import github.afezeria.hymn.oss.OssCacheKey
import github.afezeria.hymn.oss.StorageService
import github.afezeria.hymn.oss.module.service.FileRecordService
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * 文件上传/下载
 * 当 [StorageService.remoteServerSupportHttpAccess] 返回值为 true 时不处理请求，
 * @author afezeria
 */
@Controller
@RequestMapping("oss")
class PreSignedUrlController {
    @Autowired
    private lateinit var storageService: StorageService

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Autowired
    private lateinit var cacheService: CacheService

    @Autowired
    private lateinit var ossService: OssService

    @Autowired
    private lateinit var fileRecordService: FileRecordService

    companion object {

        fun generatePreSignedObjectUrl(id: String): String {
            return "module/oss/public/pre-signed/$id"
        }
    }

    @GetMapping("public/pre-signed/{id}")
    fun download(
        @PathVariable id: String,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): StreamingResponseBody {
        if (storageService.remoteServerSupportHttpAccess()) {
            throw ResponseStatusException(HttpStatus.NOT_IMPLEMENTED)
        }
        val fileId = cacheService.get(OssCacheKey.preSigned(id))
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val record = fileRecordService.findById(fileId)
            ?: throw BusinessException("文件不存在")
        response.contentType = "application/octet-stream"
        response.setHeader(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment;filename=\"${record.fileName}\""
        )
        response.setContentLength(record.size)
        return StreamingResponseBody { o ->
            ossService.getObject(record.bucket, record.path) { i ->
                IOUtils.copy(i, o)
            }
        }
    }


}