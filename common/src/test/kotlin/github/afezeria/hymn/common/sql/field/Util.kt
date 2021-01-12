package github.afezeria.hymn.common.sql.field

/**
 * @author afezeria
 */

private val map = mutableMapOf(
    "text" to 0,
    "check_box" to 0,
    "check_box_group" to 0,
    "select" to 0,
    "integer" to 0,
    "float" to 0,
    "money" to 0,
    "date" to 0,
    "datetime" to 0,
    "master_slave" to 0,
    "reference" to 0,
    "areference" to 0,
    "mreference" to 0,
    "summary" to 0,
    "auto" to 0,
    "picture" to 0,
    "files" to 0,
)

fun fieldTypeDescription(fieldType: String): String {
    return when (fieldType) {
        "text" -> "文本"
        "check_box" -> "复选框"
        "check_box_group" -> "复选框组"
        "select" -> "下拉菜单"
        "integer" -> "整型"
        "float" -> "浮点型"
        "money" -> "货币"
        "date" -> "日期"
        "datetime" -> "日期时间"
        "master_slave" -> "主详"
        "reference" -> "关联关系"
        "mreference" -> "多选关联关系"
        "areference" -> "任意关联关系"
        "summary" -> "汇总"
        "auto" -> "自动编号"
        "picture" -> "图片"
        "files" -> "文件"
        else -> throw RuntimeException("错误的自定义字段类型:${fieldType}")
    }
}

fun randomFieldNameAndApi(type: String): String {
    val desc = fieldTypeDescription(type)
    val i = map[type]!!
    map[type] = i + 1
    return "'$desc$i', '$type$i', '$type'"
}

fun randomFieldName(type: String): String {
    val desc = fieldTypeDescription(type)
    val i = map[type]!!
    map[type] = i + 1
    return "'$desc$i', '$type'"
}

fun randomFieldAndApi(type: String): String {
    val i = map[type]!!
    map[type] = i + 1
    return "'$type$i', '$type'"
}
