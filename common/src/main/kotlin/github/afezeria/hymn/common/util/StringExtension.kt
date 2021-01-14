package github.afezeria.hymn.common.util

/**
 * @author afezeria
 */
private val UNAVAILABLE_CHARACTER_SET = setOf('\\', '?', '*', '<', '"', ':', '>', '/')
fun String.isValidFileName(): Boolean {
    if (length > 189) return false
    forEach { if (UNAVAILABLE_CHARACTER_SET.contains(it)) return false }
    return true
}
