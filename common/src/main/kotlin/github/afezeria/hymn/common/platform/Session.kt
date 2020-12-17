package github.afezeria.hymn.common.platform

import github.afezeria.hymn.common.constant.ClientType
import java.util.*


class Session(
    var id: UUID,
    var accountId: UUID,
    var accountName: String,
    var clientType: ClientType,
    var roleId: UUID,
    var roleName: String,
    var orgId: UUID,
    var orgName: String,
) {

}

