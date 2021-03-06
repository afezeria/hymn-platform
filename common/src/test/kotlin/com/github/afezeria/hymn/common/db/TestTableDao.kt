package com.github.afezeria.hymn.common.db

import com.github.afezeria.hymn.common.platform.DatabaseService

/**
 * @author afezeria
 */
class TestTableDao(databaseService: DatabaseService) :
    AbstractDao<TestTable, TestTables>(table = TestTables(), databaseService = databaseService),
    HistoryDao