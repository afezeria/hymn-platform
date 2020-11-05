package github.afezeria.hymn.common.platform

import org.ktorm.database.Database

/**
 * @author afezeria
 */
interface DataBaseService {

    fun db(): Database

}