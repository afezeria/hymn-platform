package github.afezeria.hymn.common.db

import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * @author afezeria
 */
data class TestTable(

    var inta: Int,
    var longa: Long? = null,
    var doublea: Double,
    var decimala: BigDecimal,
    var boola: Boolean,
) : AbstractEntity() {
    lateinit var lazya: String

    @field:AutoFill(fillOnUpdate = true, type = AutoFillType.ROLE_ID)
    lateinit var roleId: String

    @field:AutoFill(fillOnUpdate = true, type = AutoFillType.ROLE_NAME)
    lateinit var roleName: String

    @field:AutoFill(fillOnUpdate = true, type = AutoFillType.ORG_ID)
    lateinit var orgId: String

    @field:AutoFill(fillOnUpdate = true, type = AutoFillType.ORG_NAME)
    lateinit var orgName: String

    @field:AutoFill(type = AutoFillType.ACCOUNT_ID)
    lateinit var createById: String

    @field:AutoFill(type = AutoFillType.ACCOUNT_NAME)
    lateinit var createBy: String

    @field:AutoFill(fillOnUpdate = true, type = AutoFillType.ACCOUNT_ID)
    lateinit var modifyById: String

    @field:AutoFill(fillOnUpdate = true, type = AutoFillType.ACCOUNT_NAME)
    lateinit var modifyBy: String

    @field:AutoFill(type = AutoFillType.DATETIME)
    lateinit var createDate: LocalDateTime

    @field:AutoFill(fillOnUpdate = true, type = AutoFillType.DATETIME)
    lateinit var modifyDate: LocalDateTime

}