package github.afezeria.hymn.oss.web.controller

import org.springframework.stereotype.Controller

/**
 * 文件上传/下载
 * 当 [github.afezeria.hymn.common.platform.StorageService.isRemoteServerSupportHttpAccess] 返回值为 false 时才工作，
 * 返回值为 true 时，文件下载接口返回404
 * @author afezeria
 */
@Controller
class SimpleFileController {
}