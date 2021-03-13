package com.github.afezeria.hymn.oss.module.dao

import com.github.afezeria.hymn.common.db.AbstractDao
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.oss.module.entity.FileRecord
import com.github.afezeria.hymn.oss.module.table.OssFileRecords
import org.ktorm.dsl.and
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import org.ktorm.dsl.like
import org.ktorm.expression.BinaryExpression
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

    fun pageSelectByBucket(bucket: String, pageSize: Int, pageNum: Int): List<FileRecord> {
        return pageSelect(
            { table.bucket eq bucket },
            pageSize,
            pageNum,
            listOf(table.createDate.desc())
        )
    }

    fun pageSelectByContainFileName(
        fileName: String?,
        pageSize: Int,
        pageNum: Int
    ): List<FileRecord> {
        val condition: ((OssFileRecords) -> BinaryExpression<Boolean>)? = if (fileName != null) {
            { table.fileName like "%$fileName%" }
        } else {
            null
        }
        return pageSelect(condition, pageSize, pageNum)
    }

}