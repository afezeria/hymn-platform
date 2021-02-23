package github.afezeria.hymn.core.service.dataservice

/**
 * @author afezeria
 */
class ShareTable(map: Map<String, Any?>) {
    val dataId: String = requireNotNull(map["data_id"]) as String
    val roleId: String? = map["role_id"] as String?
    val orgId: String? = map["org_id"] as String?
    val accountId: String? = map["account_id"] as String?
    val readOnly: Boolean = requireNotNull(map["read_only"]) as Boolean
}