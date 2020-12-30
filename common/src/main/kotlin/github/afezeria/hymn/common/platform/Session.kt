package github.afezeria.hymn.common.platform

import github.afezeria.hymn.common.constant.ClientType
import github.afezeria.hymn.common.constant.AccountType


class Session(
    var id: String,
    var accountType: AccountType,
    var accountId: String,
    var accountName: String,
    var clientType: ClientType,
    var roleId: String,
    var roleName: String,
    var orgId: String,
    var orgName: String,
) {

}

