package github.afezeria.hymn.oss.module.dao

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.platform.SessionService
import github.afezeria.hymn.oss.module.entity.FileRecord
import github.afezeria.hymn.oss.module.table.OssFileRecords
import org.ktorm.dsl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
@Component
class FileRecordDao {

    @Autowired
    private lateinit var dbService: DatabaseService

    @Autowired
    private lateinit var sessionService: SessionService

    val table = OssFileRecords()


    fun deleteById(id: String): Int {
        return dbService.db().delete(table) { it.id eq id }
    }

    fun update(e: FileRecord): Int {
        requireNotNull(e.id) { "missing id, unable to update data" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            set(it.bucket, e.bucket)
            set(it.fileName, e.fileName)
            set(it.contentType, e.contentType)
            set(it.path, e.path)
            set(it.objectId, e.objectId)
            set(it.fieldId, e.fieldId)
            set(it.dataId, e.dataId)
            set(it.size, e.size)
            set(it.tmp, e.tmp)
            set(it.visibility, e.visibility)
            set(it.remark, e.remark)
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }

    fun insert(e: FileRecord): String {
        val now = LocalDateTime.now()
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        e.createDate = now
        e.modifyDate = now
        e.createById = accountId
        e.modifyById = accountId
        e.createBy = accountName
        e.modifyBy = accountName
        return dbService.db().insertAndGenerateKey(table) {
            set(it.bucket, e.bucket)
            set(it.fileName, e.fileName)
            set(it.contentType, e.contentType)
            set(it.path, e.path)
            set(it.objectId, e.objectId)
            set(it.fieldId, e.fieldId)
            set(it.dataId, e.dataId)
            set(it.size, e.size)
            set(it.tmp, e.tmp)
            set(it.visibility, e.visibility)
            set(it.remark, e.remark)
        } as String
    }

    fun selectAll(): MutableList<FileRecord> {
        return dbService.db().from(table)
            .select(table.columns)
            .mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectById(id: String): FileRecord? {
        return dbService.db().from(table)
            .select(table.columns)
            .where { table.id eq id }
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }

    fun selectByIds(ids: List<String>): MutableList<FileRecord> {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.id inList ids
            }.mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun deleteByBucketAndPath(bucket: String, path: String): Int {
        return dbService.db().delete(table) {
            it.bucket eq bucket
            it.path eq path
        }
    }

    fun selectByBucketAndPath(bucket: String, objectName: String): FileRecord? {
        return dbService.db().from(table)
            .select(table.columns)
            .where {
                table.bucket eq bucket
                table.path eq objectName
            }.map { table.createEntity(it) }
            .firstOrNull()
    }

    fun update(id: String, data: Map<String, Any?>): Int {
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
            for (column in table.columns) {
                if (data.containsKey(column.name)) {
                    set(column, data[column.name])
                }
            }
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq id
            }
        }
    }


}