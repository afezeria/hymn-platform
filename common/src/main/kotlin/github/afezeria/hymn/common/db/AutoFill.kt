package github.afezeria.hymn.common.db

/**
 * @author afezeria
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class AutoFill(
    val fillOnInsert: Boolean = true,
    val fillOnUpdate: Boolean = false,
    val type: AutoFillType,
)
