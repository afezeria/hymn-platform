package com.github.afezeria.hymn.oss.module.table

import com.github.afezeria.hymn.common.db.AbstractTable
import com.github.afezeria.hymn.oss.module.entity.PreSignedHistory
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * @author afezeria
 */
class OssPreSignedHistorys(alias: String? = null) :
    AbstractTable<PreSignedHistory>("oss_pre_signed_history", schema = "hymn", alias = alias) {

    val fileId = varchar("file_id")
    val expiry = int("expiry")
    val createDate = datetime("create_date")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
}

