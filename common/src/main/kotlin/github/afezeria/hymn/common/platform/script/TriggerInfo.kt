package github.afezeria.hymn.common.platform.script

/**
 * @author afezeria
 */
class TriggerInfo(
    val name: String,
    val api: String,
    val ord: Int,
    val remark: String = "",
    val idx: Int,
    val triggerList: List<String>,
)