package github.afezeria.hymn.common.container

import github.afezeria.hymn.common.KGenericContainer

/**
 * @author afezeria
 */
abstract class Container {
    lateinit var container: KGenericContainer
    lateinit var ip: String
    var port: Int = 0
    var config: Map<String, Any> = emptyMap()
}