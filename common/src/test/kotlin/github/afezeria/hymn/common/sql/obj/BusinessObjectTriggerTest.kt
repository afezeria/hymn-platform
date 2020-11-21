package github.afezeria.hymn.common.sql.obj

import github.afezeria.hymn.common.conn
import github.afezeria.hymn.common.sql.BaseDbTest
import github.afezeria.hymn.common.sql.DEFAULT_ACCOUNT_ID
import github.afezeria.hymn.common.sql.DEFAULT_ACCOUNT_NAME
import github.afezeria.hymn.common.util.execute
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

/**
 * @author afezeria
 */
class BusinessObjectTriggerTest : BaseDbTest() {

    @Test
    fun insertObject() {
        conn.use {
            it.execute("""
insert into hymn.sys_core_b_object  ( name, api,  active,   create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
values ('测试对象','test_obj',true,?,?,?,?,?,?) returning id;
                """, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME, LocalDateTime.now(), LocalDateTime.now())
        }
    }
}