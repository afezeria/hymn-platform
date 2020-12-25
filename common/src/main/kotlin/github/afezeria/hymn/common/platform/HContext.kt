package github.afezeria.hymn.common.platform

import github.afezeria.hymn.common.constant.ClientType
import github.afezeria.hymn.common.perm.Role

/**
 * @author afezeria
 */
object HContext {
    fun currentAccountType(): Role {
        return Role.USER
    }

    val clientType: ClientType
        get() {
            TODO()
        }

    val map = ThreadLocal<MutableMap<String, Any?>>()

    fun init() {
        map.set(mutableMapOf())
    }

    val admin: Boolean
        get() {
            TODO()
        }
    val accountId: String
        get() {
            TODO()
        }
    val accountName: String
        get() {
            TODO()
        }
    val roleId: String
        get() {
            TODO()
        }


    fun clear() {
        map.remove()
    }

    var sessionId: String?
        get() {
            return map.get().get("sessionId") as String?
        }
        set(value) {
            map.get().set("sessionId", value)
        }

}