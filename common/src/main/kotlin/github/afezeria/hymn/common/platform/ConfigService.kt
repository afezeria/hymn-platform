package github.afezeria.hymn.common.platform

/**
 * @author afezeria
 */
interface ConfigService {
    fun get(key: String): String?
    fun getAll(): List<String>
    fun getStartWith(prefix: String): List<String>
}