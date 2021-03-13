package com.github.afezeria.hymn.common.db

import org.ktorm.schema.*

/**
 * @author afezeria
 */
class TestTables : AbstractTable<TestTable>(
    tableName = "test_table",
    alias = null,
    catalog = null,
    schema = "test_dao",
) {
    val inta = int("inta")
    val longa = long("longa")
    val doublea = double("doublea")
    val decimala = decimal("decimala")
    val boola = boolean("boola")
    val lazya = text("lazya")

    val roleId = varchar("role_id")
    val roleName = varchar("role_name")
    val orgId = varchar("org_id")
    val orgName = varchar("org_name")
    val createById = varchar("create_by_id")
    val createBy = varchar("create_by")
    val modifyById = varchar("modify_by_id")
    val modifyBy = varchar("modify_by")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")

}