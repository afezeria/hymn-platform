package github.afezeria.hymn.common.sql.field

import github.afezeria.hymn.common.*
import github.afezeria.hymn.common.util.execute
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import org.junit.jupiter.api.Test

/**
 * @author afezeria
 */
class AreferenceFieldTest : BaseDbTest() {

    @Test
    fun success() {
        customBizObject {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,filter_list,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("areference")},?,?,?,?,?,now(),now()) returning *;
                    """,
//                用户表、角色表和随机id
                objId,
                "bcf5f00c2e6c494ea2318912a639031a,53e9c36723dc4e3db1faf396fdb3f1d2,${randomUUIDStr()}",
                *COMMON_INFO
            )[0]
            field["filter_list"] as String shouldBe "bcf5f00c2e6c494ea2318912a639031a,53e9c36723dc4e3db1faf396fdb3f1d2"
            field["source_column"] as String shouldMatch "aref\\d{3}".toRegex()
            userConn.use {
                it.execute(
                    """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                    *standardField, "bcf5f00c2e6c494ea2318912a639031a,${randomUUIDStr()};合同,采购合同"
                ).size shouldBe 1
            }

        }
    }
}