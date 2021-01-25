package github.afezeria.hymn.oss.web.controller

import github.afezeria.hymn.oss.StorageService
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * 文件上传/下载
 * 当 [github.afezeria.hymn.common.platform.OssService.remoteServerSupportHttpAccess] 返回值为 false 时才工作，
 * 返回值为 true 时，文件下载接口返回404
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
    private lateinit var redissonClient: RedissonClient


    @GetMapping("public/download")
    fun download(
        @PathVariable fileId: String,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): StreamingResponseBody {
        if (storageService.remoteServerSupportHttpAccess()) {
            throw ResponseStatusException(HttpStatus.NOT_IMPLEMENTED)
        } else {
//            val str = redisTemplate.boundValueOps("oss:download:$fileId").get()
//            if (str == null) {
//                throw ResponseStatusException(HttpStatus.NOT_FOUND)
//            }
            val bucket = redissonClient.getBucket<String>("oss:presigned:$fileId")
            val str = bucket.get()
            bucket.expire()
//
//            val fileInfo: FileInfo = fileService.findFileInfo(fileId)
//            response.setContentType(fileInfo.getContentType())
//            response.setHeader(
//                HttpHeaders.CONTENT_DISPOSITION,
//                "attachment;filename=\"" + fileInfo.getFilename().toString() + "\""
//            )
//
//            return Unit { outputStream ->
//                var bytesRead: Int
//                val buffer = ByteArray(BUFFER_SIZE)
//                val inputStream: InputStream = fileInfo.getInputStream()
//                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
//                    outputStream.write(buffer, 0, bytesRead)
//                }
//            }
        }
        TODO()
    }


}