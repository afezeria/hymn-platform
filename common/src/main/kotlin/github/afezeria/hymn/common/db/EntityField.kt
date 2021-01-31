package github.afezeria.hymn.common.db

import org.ktorm.schema.Column
import java.lang.reflect.Field

/**
 * @author afezeria
 */
class EntityField(
    val field: Field,
    val column: Column<*>,
    val mutable: Boolean,
    val nullable: Boolean,
    val lazy: Boolean,
    val autoFill: AutoFill?,
)
