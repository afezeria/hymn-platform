package com.github.afezeria.hymn.script.platform

import com.github.afezeria.hymn.common.constant.TriggerEvent
import org.graalvm.polyglot.Source

/**
 * @author afezeria
 */
open class SourceWithTime(
    val api: String,
    val source: Source,
    val timestamp: Long,
    val functionIds: List<String>,
)

class TriggerSourceWithTime(
    val event: TriggerEvent,
    val ord: Int,
    api: String,
    source: Source,
    timestamp: Long,
    functionIds: List<String>
) : SourceWithTime(api, source, timestamp, functionIds)