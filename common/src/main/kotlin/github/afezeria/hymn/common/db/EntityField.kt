package github.afezeria.hymn.common.db

import java.lang.reflect.Field

/**
 * @author afezeria
 */
class EntityField(
    val name: String,
    val nullable: Boolean,
    val lazy: Boolean,
    val field: Field,
    val autoFill: AutoFillField?
)
