package github.afezeria.hymn.common.platform

import github.afezeria.hymn.common.util.mapper

/**
 * @author afezeria
 */
abstract class ConfigService {
    abstract fun getAsString(key: String): String?

    inline fun <reified T> get(key: String): T? {
        return getAsString(key)?.let { mapper.readValue(it, T::class.java) }
    }

    abstract fun getKeyList(): List<String>

    abstract fun getStartWith(prefix: String): List<String>
}
