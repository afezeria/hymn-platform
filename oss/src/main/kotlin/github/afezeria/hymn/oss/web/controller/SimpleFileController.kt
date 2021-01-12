package github.afezeria.hymn.oss.web.controller

import github.afezeria.hymn.common.platform.StorageService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 文件上传/下载
 * 当 [github.afezeria.hymn.common.platform.StorageService.isRemoteServerSupportHttpAccess] 返回值为 false 时才工作，
 * 返回值为 true 时，文件下载接口返回404
 * @author afezeria
 */
@Controller
@RequestMapping("oss")
class SimpleFileController {
    lateinit var storageService: StorageService

    @GetMapping("public/download")
    fun download(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse) {
        if (storageService.isRemoteServerSupportHttpAccess()) {

        }
    }

    fun generateFileUrl(bucket: String, fileName: String, expiry: Int): String {
        TODO()
    }
}