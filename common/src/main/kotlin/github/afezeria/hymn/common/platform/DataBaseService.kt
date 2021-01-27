package github.afezeria.hymn.common.platform

import org.ktorm.database.Database

/**
 * 数据源接口
 * @author afezeria
 */
interface DataBaseService {

    fun db(): Database

    fun readOnly(): Database

}