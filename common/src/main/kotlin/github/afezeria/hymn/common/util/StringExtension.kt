package github.afezeria.hymn.common.util

import java.util.*

/**
 * @author afezeria
 */
private val FILE_NAME_REGEX = "[^\\x00-\\x1f\\x7f\\\\?*<\":>/]{1,222}".toRegex()
fun String.isValidFileName(): Boolean = matches(FILE_NAME_REGEX)
fun String.throwIfFileNameInvalid() =
    takeIf { matches(FILE_NAME_REGEX) } ?: throw BusinessException("$this 不是有效的文件名")


private val BUCKET_REGEX = "[-a-z0-9.]{3,40}".toRegex()
fun String.isValidBucketName(): Boolean = matches(BUCKET_REGEX)
fun String.throwIfBucketNameInvalid() =
    takeIf { matches(BUCKET_REGEX) } ?: throw InnerException("$this 不是有效的bucket名称")

