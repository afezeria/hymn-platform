package github.afezeria.hymn.oss

import github.afezeria.hymn.oss.StorageType

/**
 * @author afezeria
 */
data class Config(val type: StorageType, val data: Map<String, Any?>)