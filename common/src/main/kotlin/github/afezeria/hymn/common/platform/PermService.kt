package github.afezeria.hymn.common.platform

/**
 * @author afezeria
 */
interface PermService {
    fun hasFunctionPerm(roleId: String, name: String): Boolean

}