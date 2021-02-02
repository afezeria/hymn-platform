package github.afezeria.hymn.oss.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.oss.module.entity.PreSignedHistory
import github.afezeria.hymn.oss.module.table.OssPreSignedHistorys
import org.ktorm.dsl.*
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * @author afezeria
 */
@Component
class PreSignedHistoryDao(
    databaseService: DatabaseService
) : AbstractDao<PreSignedHistory, OssPreSignedHistorys>(
    table = OssPreSignedHistorys(),
    databaseService = databaseService,
) {

    fun selectByFileId(
        fileId: String,
    ): MutableList<PreSignedHistory> {
        return select({ table.fileId eq fileId })
    }

    fun pageSelectBetweenCreateDateOrderByCreateDateDesc(
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        pageSize: Int,
        pageNum: Int
    ): List<PreSignedHistory> {
        val condition = if (startDate == null) {
            if (endDate == null) {
                null
            } else {
                { table.createDate less endDate }
            }
        } else {
            if (endDate == null) {
                { table.createDate greater startDate }
            } else {
                { table.createDate between startDate..endDate }
            }
        }
        return pageSelect(
            condition,
            pageSize,
            pageNum,
            listOf(table.createDate.desc())
        )

    }

    fun pageSelectByFileId(fileId: String, pageSize: Int, pageNum: Int): List<PreSignedHistory> {
        return pageSelect(
            { table.fileId eq fileId },
            pageSize,
            pageNum,
            listOf(table.createDate.desc())
        )
    }

}