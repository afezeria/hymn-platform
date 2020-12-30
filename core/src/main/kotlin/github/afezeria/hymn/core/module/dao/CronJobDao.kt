package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.PlatformService
import github.afezeria.hymn.core.module.entity.CronJob
import github.afezeria.hymn.core.module.table.CoreCronJobs
import org.ktorm.dsl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
@Component
class CronJobDao {

    @Autowired
    private lateinit var dbService: DataBaseService

    @Autowired
    private lateinit var platformService: PlatformService

    val table = CoreCronJobs()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: CronJob): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = platformService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.active, e.active)
            set(it.sharedCodeId, e.sharedCodeId)
            set(it.cron, e.cron)
            set(it.startDateTime, e.startDateTime)
            set(it.endDateTime, e.endDateTime)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: CronJob): String {
        val now = LocalDateTime.now()
        val session = platformService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        e.createDate = now
        e.modifyDate = now
        e.createById = accountId
        e.modifyById = accountId
        e.createBy = accountName
        e.modifyBy = accountName
        return dbService.db().insertAndGenerateKey(table) {
            set(it.active, e.active)
            set(it.sharedCodeId, e.sharedCodeId)
            set(it.cron, e.cron)
            set(it.startDateTime, e.startDateTime)
            set(it.endDateTime, e.endDateTime)
            set(it.createDate, e.createBy)
            set(it.modifyDate, e.modifyDate)
            set(it.createById, e.createById)
            set(it.modifyById, e.modifyById)
            set(it.createBy, e.createBy)
            set(it.modifyBy, e.modifyBy)
        } as String
    }

    fun selectAll(): MutableList<CronJob> {
        return dbService.db().from(table)
            .select(table.columns)
            .mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectById(id: String): CronJob? {
        return dbService.db().from(table)
            .select(table.columns)
            .where { table.id eq id }
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByIds(ids: List<String>): MutableList<CronJob> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.id inList ids
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectBySharedCodeId(
        sharedCodeId: String,
    ): MutableList<CronJob> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.sharedCodeId eq sharedCodeId
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }


}