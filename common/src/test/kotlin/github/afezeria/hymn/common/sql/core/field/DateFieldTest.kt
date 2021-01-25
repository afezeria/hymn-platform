package github.afezeria.hymn.common.sql.core.field

import github.afezeria.hymn.common.BaseDbTest
import github.afezeria.hymn.common.COMMON_INFO
import github.afezeria.hymn.common.customBizObject
import github.afezeria.hymn.common.util.execute
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import org.junit.jupiter.api.Test
import java.time.LocalDate

/**
 * @author afezeria
 */
class DateFieldTest : BaseDbTest() {
    @Test
    fun date() {
        customBizObject {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type,  create_by_id, 
                        create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("date")},?,?,?,?,now(),now()) returning *;
                    """,
                objId,
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "datetime"
            it.fieldShouldExists(field["api"])
            it.execute(
                """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *standardField, LocalDate.now()
            ).size shouldBe 1

        }
    }
}