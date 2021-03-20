package com.github.afezeria.hymn.script

import org.graalvm.polyglot.Source

/**
 * @author afezeria
 */
class SourceWithTime(
    val api: String,
    val source: Source,
    val timestamp: Long,
)