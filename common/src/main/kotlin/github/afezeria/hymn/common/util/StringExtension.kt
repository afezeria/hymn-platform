package github.afezeria.hymn.common.util

import github.afezeria.hymn.common.exception.BusinessException
import github.afezeria.hymn.common.exception.InnerException

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

fun String.msgById(value: Any): String {
    return "$this [id:$value]"
}

fun String.msgByPairs(vararg pairs: Pair<String, Any?>): String {
    return "$this [${pairs.joinToString(separator = ",") { "${it.first}:${it.second}" }}]"
}

fun String.msgByPair(field: String, value: Any?): String {
    return "$this [$field:$value]"
}