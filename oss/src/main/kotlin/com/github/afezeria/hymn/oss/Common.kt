package com.github.afezeria.hymn.oss

import com.github.afezeria.hymn.common.exception.BusinessException
import com.github.afezeria.hymn.common.exception.InnerException
import java.util.*

/**
 * @author afezeria
 */
private val FILE_NAME_REGEX = "[^\\x00-\\x1f\\x7f\\\\?*<\":>/]{1,199}".toRegex()
fun String.isValidFileName(): Boolean = matches(FILE_NAME_REGEX)
fun String.throwIfFileNameInvalid() =
    takeIf { matches(FILE_NAME_REGEX) } ?: throw BusinessException("$this 不是有效的文件名")


private val BUCKET_REGEX = "[-a-z0-9.]{3,40}".toRegex()
fun String.isValidBucketName(): Boolean = matches(BUCKET_REGEX)
fun String.throwIfBucketNameInvalid() =
    takeIf { matches(BUCKET_REGEX) } ?: throw InnerException("$this 不是有效的bucket名称")

fun filename2ContentType(filename: String) =
    when (filename.substringAfterLast('.', "").lowercase(Locale.getDefault())) {
        "tif" -> "image/tiff"
        "tiff" -> "image/tiff"
        "fax" -> "image/fax"
        "gif" -> "image/gif"
        "ico" -> "image/x-icon"
        "jpe" -> "image/jpeg"
        "jpeg" -> "image/jpeg"
        "jpg" -> "image/jpeg"
        "png" -> "image/png"
        "bmp" -> "image/bmp"
        "svg" -> "image/svg+xml"
        "webp" -> "image/webp"

        "tar" -> "application/x-tar"
        "rar" -> "application/vnd.rar"
        "bz" -> "application/x-bzip"
        "bz2" -> "application/x-bzip2"
        "gz" -> "application/gzip"
        "zip" -> "application/zip"
        "gzip" -> "application/gzip"
        "7z" -> "application/x-7z-compressed"

        "mp3" -> "audio/mpeg"
        "weba" -> "audio/webm"
        "webm" -> "video/webm"
        "mpeg" -> "video/mpeg"
        "avi" -> "video/x-msvideo"

        "woff" -> "application/font-woff"
        "pdf" -> "application/pdf"
        "ppt" -> "application/vnd.ms-powerpoint"
        "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation"
        "xls" -> "application/x-xls"
        "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        "doc" -> "application/msword"
        "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        else -> "application/octet-stream"
    }

fun contentType2Bucket(contentType: String): String = when (contentType) {
    "image/tiff", "image/fax", "image/gif", "image/x-icon", "image/jpeg", "image/png", "image/bmp", "image/svg+xml", "image/webp" -> "image"
    "application/vnd.rar", "application/x-tar", "application/x-bzip", "application/x-bzip2", "application/gzip", "application/zip", "application/x-7z-compressed" -> "zip"
    "audio/mpeg", "audio/webm" -> "audio"
    "video/webm", "video/mpeg", "video/x-msvideo" -> "video"
    "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/pdf", "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", "application/x-xls", "application/msword" -> "office"
    else -> "other"
}

internal var remoteServerSupportHttpAccess: Boolean = false
