package github.afezeria.hymn.common.platform

import org.ktorm.database.Database
import org.ktorm.database.Transaction

/**
 * 数据源接口
 * @author afezeria
 */
interface DatabaseService {

    fun db(): Database

    fun primary(): Database

    fun readOnly(): Database

    fun user(): Database

    fun <T> useTransaction(fn: (Transaction) -> T): T
}