package github.afezeria.hymn.common.platform

import org.ktorm.database.Database
import org.ktorm.database.Transaction
import org.ktorm.database.TransactionIsolation

/**
 * 数据源接口
 * @author afezeria
 */
interface DatabaseService {

    fun db(): Database

    fun primary(): Database

    fun readOnly(): Database

    fun <T> useTransaction(fn: (Transaction) -> T): T

}