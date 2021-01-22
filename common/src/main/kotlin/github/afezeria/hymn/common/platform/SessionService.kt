package github.afezeria.hymn.common.platform


/**
 * @author afezeria
 */
interface SessionService {
    fun createSession(session: Session): String
    fun deleteSession(id: String)
    fun getSession(): Session
}