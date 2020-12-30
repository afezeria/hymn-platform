package github.afezeria.hymn.common.platform


/**
 * @author afezeria
 */
interface PlatformService {
    fun createSession(session: Session): String
    fun deleteSession(id: String)
    fun getSession(): Session
    fun hasPerm(roleId: String, name: String): Boolean
}