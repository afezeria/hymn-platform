package github.afezeria.hymn.oss.module.dao

import github.afezeria.hymn.oss.module.entity.PreSignedHistory
import github.afezeria.hymn.oss.module.table.OssPreSignedHistorys
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import org.ktorm.dsl.*
import java.time.LocalDateTime
import java.util.*

/**
* @author afezeria
*/
@Component
class PreSignedHistoryDao {

    @Autowired
    private lateinit var dbService: DataBaseService
    @Autowired
    private lateinit var sessionService:SessionService

    val table = OssPreSignedHistorys()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: PreSignedHistory): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.fileId, e.fileId)
            set(it.expiry, e.expiry)
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: PreSignedHistory): String {
        val now = LocalDateTime.now()
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        e.createDate = now
        e.createById = accountId
        e.createBy = accountName
        return dbService.db().insertAndGenerateKey(table) {
            set(it.fileId, e.fileId)
            set(it.expiry, e.expiry)
        } as String
    }

    fun selectAll(): MutableList<PreSignedHistory> {
        return dbService.db().from(table)
            .select(table.columns)
            .mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectById(id: String): PreSignedHistory? {
        return dbService.db().from(table)
            .select(table.columns)
            .where { table.id eq id }
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByIds(ids: List<String>): MutableList<PreSignedHistory>{
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.id inList ids
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectByFileId(
        fileId: String,
    ): MutableList<PreSignedHistory> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.fileId eq fileId
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }


}