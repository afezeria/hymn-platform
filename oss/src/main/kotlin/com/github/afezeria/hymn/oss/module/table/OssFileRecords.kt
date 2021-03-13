package com.github.afezeria.hymn.oss.module.table

import com.github.afezeria.hymn.common.db.AbstractTable
import com.github.afezeria.hymn.oss.module.entity.FileRecord
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class OssFileRecords(alias: String? = null) :
    AbstractTable<FileRecord>("oss_file_record", schema = "hymn", alias = alias) {

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
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}
