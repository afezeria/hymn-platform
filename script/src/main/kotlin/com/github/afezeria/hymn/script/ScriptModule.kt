package com.github.afezeria.hymn.script

import com.github.afezeria.hymn.common.module.AbstractModule
import com.github.afezeria.hymn.common.module.ClusterService
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class ScriptModule(clusterService: ClusterService) : AbstractModule("script", clusterService)