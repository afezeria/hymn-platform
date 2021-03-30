package com.github.afezeria.hymn.common.module

import mu.KLogging

/**
 * @author afezeria
 */
abstract class AbstractModule(name: String, clusterService: ClusterService) {
    companion object : KLogging()

    init {
        logger.info("register $name module")
        clusterService.addModule(name)
    }
}