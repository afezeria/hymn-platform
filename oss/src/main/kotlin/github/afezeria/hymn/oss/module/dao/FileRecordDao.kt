package github.afezeria.hymn.oss.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.oss.module.entity.FileRecord
import github.afezeria.hymn.oss.module.table.OssFileRecords
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class FileRecordDao(
    databaseService: DatabaseService
) : AbstractDao<FileRecord, OssFileRecords>(OssFileRecords(), databaseService) {
    fun deleteByBucketAndPath(bucket: String, path: String): Int {
        return delete { (table.bucket eq bucket) and (table.path eq path) }
    }

    fun selectByBucketAndPath(bucket: String, objectName: String): FileRecord? {
        return singleRowSelect(listOf(table.bucket eq bucket, table.path eq objectName))
    }

}