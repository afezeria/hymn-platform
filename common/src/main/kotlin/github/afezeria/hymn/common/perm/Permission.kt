package github.afezeria.hymn.common.perm

/**
 * @author afezeria
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
annotation class Permission(val type: Role = Role.ANONYMOUS)
