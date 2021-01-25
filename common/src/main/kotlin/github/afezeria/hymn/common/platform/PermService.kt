package github.afezeria.hymn.common.platform

/**
 * @author afezeria
 */
interface PermService {
    fun hasFunctionPerm(roleId: String, name: String): Boolean
    fun hasDataPerm(objectId: String, dataId: String): Boolean
    fun hasObjectPerm(objectId: String): Boolean
    fun hasFieldPerm(objectId: String, fieldId: String): Boolean

}