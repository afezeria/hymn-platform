package github.afezeria.hymn.common.platform

import github.afezeria.hymn.common.constant.ClientType
import java.util.*


class Session(
    var id: String,
    var accountId: String,
    var accountName: String,
    var clientType: ClientType,
    var roleId: String,
    var roleName: String,
    var orgId: String,
    var orgName: String,
) {

}

