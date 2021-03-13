package com.github.afezeria.hymn.common

import org.testcontainers.containers.GenericContainer

/**
 * @author afezeria
 */
class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)
