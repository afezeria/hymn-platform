package github.afezeria.hymn.common.util

/**
 * @author afezeria
 */
private val UNAVAILABLE_CHARACTER_SET = setOf('\\', '?', '*', '<', '"', ':', '>', '/')
fun String.isValidFileName(str: String): Boolean {
    if (str.length > 189) return false
    str.forEach { if (UNAVAILABLE_CHARACTER_SET.contains(it)) return false }
    return true
}
