package script

fun "fun deleteById(id: UUID): Int",
    """
    @TableCache(isDelete = true)
    override fun deleteById(id: UUID): Int {
        return dbService.db().delete(table) { it.id eq id }
    }
"""

updateFieldSet = ["id", "modifyById", "modifyDate", "modifyBy"]

fun "fun update(e: ${_service.entityClassName}): Int",
    """
    @TableCache(isDelete = true)
    override fun update(e: ${_service.entityClassName}): Int {
        requireNotNull(e.id) { "can not update ${_service.entityClassName} when id is null" }
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        return dbService.db().update(table) {
${
      _service._table.fields.findAll { !updateFieldSet.contains(it.fieldName) }
          .collect {
            "            set(it.${it.fieldName}, e.${it.fieldName})\n"
          }.join()
    }
            set(it.modifyById, accountId)
            set(it.modifyBy, accountName)
            set(it.modifyDate, LocalDateTime.now())
            where {
                it.id eq e.id
            }
        }
    }
"""


fun "fun insert(e: ${_service.entityClassName}): Int",
    """
    @TableCache(isDelete = true)
    override fun insert(e: ${_service.entityClassName}): Int {
        val now = LocalDateTime.now()
        val session = sessionService.getSession()
        val accountId = session.accountId
        val accountName = session.accountName
        e.createDate = now
        e.modifyDate = now
        e.createById = accountId
        e.modifyById = accountId
        e.createBy = accountName
        e.modifyBy = accountName
        return dbService.db().insert(table) {
${
      _service._table.fields.findAll { it.fieldName != 'id' }
          .collect {
            "            set(it.${it.fieldName}, e.${it.fieldName})\n"
          }.join()
    }
        }
    }
"""


fun "fun findAll(): List<${_service.entityClassName}>",
    """
    @TableCache
    override fun findAll(): List<${_service.entityClassName}> {
        return dbService.db().from(table)
            .select(table.columns)
            .map { table.createEntity(it) }
    }
"""


fun "fun findById(id: UUID): ${_service.entityClassName}?",
    """
    @TableCache
    override fun findById(id: UUID): ${_service.entityClassName}? {
        return dbService.db().from(table)
            .select(table.columns)
            .limit(0, 1)
            .map { table.createEntity(it) }
            .firstOrNull()
    }
"""
