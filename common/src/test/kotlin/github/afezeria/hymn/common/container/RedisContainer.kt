package github.afezeria.hymn.common.container

import github.afezeria.hymn.common.KGenericContainer

/**
 * @author afezeria
 */
object RedisContainer : Container() {
    init {
        port = 6379
        container = KGenericContainer("redis:6.0.10-buster")
            .withExposedPorts(port)
        ip = container.containerInfo.networkSettings.ipAddress
    }

}
