package com.github.afezeria.hymn.script.platform

import org.graalvm.polyglot.Source

/**
 * @author afezeria
 */
class SourceWithTime(
    val api: String,
    val source: Source,
    val timestamp: Long,
)