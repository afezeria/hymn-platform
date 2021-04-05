package com.github.afezeria.hymn.common.module

import mu.KLogging

/**
 * @author afezeria
 */
abstract class AbstractModule(name: String, registerCallback: (() -> Unit)? = null) {
    companion object : KLogging()

    init {
        logger.info("register $name module")
        ClusterService.addModule(name, registerCallback)
    }
}