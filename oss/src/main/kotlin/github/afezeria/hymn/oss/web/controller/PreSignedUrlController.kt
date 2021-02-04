package github.afezeria.hymn.oss.web.controller

import github.afezeria.hymn.common.ann.ApiVersion
import github.afezeria.hymn.common.exception.BusinessException
import github.afezeria.hymn.common.platform.CacheService
import github.afezeria.hymn.common.platform.OssService
import github.afezeria.hymn.common.util.Jwt
import github.afezeria.hymn.common.util.randomUUIDStr
import github.afezeria.hymn.oss.OssCacheKey
import github.afezeria.hymn.oss.StorageService
import github.afezeria.hymn.oss.module.service.FileRecordService
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * 文件上传/下载
 * 当 [StorageService.remoteServerSupportHttpAccess] 返回值为 true 时不处理请求，
 * @author afezeria
 */
@ApiVersion
@Controller
@RequestMapping("oss/api/{version}")
class PreSignedUrlController {
    @Autowired
    private lateinit var storageService: StorageService

    @Autowired
    private lateinit var cacheService: CacheService

    @Autowired
    private lateinit var ossService: OssService

    @Autowired
    private lateinit var fileRecordService: FileRecordService

    private val version = 2104

    internal fun generatePreSignedObjectUrl(fileId: String, expiry: Long): String {
        val id = randomUUIDStr()
        val token = Jwt.createJwtToken(mapOf("id" to id), expiry)
        val res = cacheService.setIfAbsent(OssCacheKey.preSigned(id), fileId, expiry)
        if (!res) throw BusinessException("生成对象预签名链接失败")
        return "module/oss/api/v$version/public/pre-signed?token=$token"
    }

    @GetMapping("public/pre-signed")
    fun download(
        @RequestParam("token") token: String,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): StreamingResponseBody {
        if (storageService.remoteServerSupportHttpAccess()) {
            throw ResponseStatusException(HttpStatus.NOT_IMPLEMENTED)
        }
        if (token.isBlank()) throw BusinessException("无效的token")
        val id = requireNotNull(Jwt.parseToken(token)["id"]) as String

        val fileId = cacheService.get(OssCacheKey.preSigned(id))
            ?: throw BusinessException("token已过期")
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