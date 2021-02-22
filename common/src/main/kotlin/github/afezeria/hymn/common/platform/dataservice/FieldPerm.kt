package github.afezeria.hymn.common.platform.dataservice

data class FieldPerm(
    val roleId: String,
    val fieldId: String,
    /**
     * 可读
     */
    val pRead: Boolean,
    /**
     * 可写
     */
    val pEdit: Boolean,
)
