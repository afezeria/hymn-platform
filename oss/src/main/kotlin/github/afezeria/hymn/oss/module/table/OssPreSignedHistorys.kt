package github.afezeria.hymn.oss.module.table

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import github.afezeria.hymn.oss.module.entity.PreSignedHistory

/**
 * @author afezeria
 */
class OssPreSignedHistorys(alias: String? = null) :
    BaseTable<PreSignedHistory>("oss_pre_signed_history", schema = "hymn", alias = alias) {

    val fileId = varchar("file_id")
    val expiry = int("expiry")
    val id = varchar("id")
    val createDate = datetime("create_date")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = PreSignedHistory(
        fileId = requireNotNull(row[this.fileId]) { "field PreSignedHistory.fileId should not be null" },
        expiry = requireNotNull(row[this.expiry]) { "field PreSignedHistory.expiry should not be null" },
    ).also {
        it.id = requireNotNull(row[this.id]) { "field PreSignedHistory.id should not be null" }
        it.createDate = requireNotNull(row[this.createDate]) { "field PreSignedHistory.createDate should not be null" }
        it.createById = requireNotNull(row[this.createById]) { "field PreSignedHistory.createById should not be null" }
        it.createBy = requireNotNull(row[this.createBy]) { "field PreSignedHistory.createBy should not be null" }
    }
}
