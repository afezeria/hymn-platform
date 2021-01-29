package github.afezeria.hymn.common.db

/**
 * @author afezeria
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class AutoFillField(
    val fillOnInsert: Boolean,
    val fillOnUpdate: Boolean,
    val type: AutoFillType,
)
