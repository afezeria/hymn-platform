package github.afezeria.hymn.oss.module.table

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import github.afezeria.hymn.oss.module.entity.FileRecord

/**
 * @author afezeria
 */
class OssFileRecords(alias: String? = null) :
    BaseTable<FileRecord>("oss_file_record", schema = "hymn", alias = alias) {

    val bucket = varchar("bucket")
    val fileName = varchar("file_name")
    val contentType = varchar("content_type")
    val path = varchar("path")
    val objectId = varchar("object_id")
    val fieldId = varchar("field_id")
    val dataId = varchar("data_id")
    val size = int("size")
    val tmp = boolean("tmp")
    val visibility = varchar("visibility")
    val remark = varchar("remark")
    val id = varchar("id")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = FileRecord(
        bucket = requireNotNull(row[this.bucket]) { "field FileRecord.bucket should not be null" },
        fileName = requireNotNull(row[this.fileName]) { "field FileRecord.fileName should not be null" },
        contentType = row[this.contentType],
        path = requireNotNull(row[this.path]) { "field FileRecord.path should not be null" },
        objectId = row[this.objectId],
        fieldId = row[this.fieldId],
        dataId = row[this.dataId],
        size = requireNotNull(row[this.size]) { "field FileRecord.size should not be null" },
        tmp = row[this.tmp],
        visibility = row[this.visibility],
        remark = row[this.remark],
    ).also {
        it.id = requireNotNull(row[this.id]) { "field FileRecord.id should not be null" }
        it.createById = requireNotNull(row[this.createById]) { "field FileRecord.createById should not be null" }
        it.createBy = requireNotNull(row[this.createBy]) { "field FileRecord.createBy should not be null" }
        it.modifyById = requireNotNull(row[this.modifyById]) { "field FileRecord.modifyById should not be null" }
        it.modifyBy = requireNotNull(row[this.modifyBy]) { "field FileRecord.modifyBy should not be null" }
        it.createDate = requireNotNull(row[this.createDate]) { "field FileRecord.createDate should not be null" }
        it.modifyDate = requireNotNull(row[this.modifyDate]) { "field FileRecord.modifyDate should not be null" }
    }
}
