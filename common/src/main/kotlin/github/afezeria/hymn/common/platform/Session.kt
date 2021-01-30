package github.afezeria.hymn.common.platform

import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.common.constant.ClientType


class Session(
    val id: String,
    val accountType: AccountType,
    val accountId: String,
    val accountName: String,
    val clientType: ClientType,
    val roleId: String,
    val roleName: String,
    val orgId: String,
    val orgName: String,
) {
    val other: MutableMap<String, String> = mutableMapOf()

    companion object {
        internal val current = ThreadLocal<Session>()
        fun getInstance(): Session {
            return current.get()
        }
    }

}

