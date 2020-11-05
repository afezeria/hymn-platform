package script

findAll =
    """
    @ApiOperation(value = "查询全部数据", notes = "")
    @GetMapping
    fun findAll(): RestResp<${_controller.entityClassName}> {
        val all = ${_controller.daoName}.findAll()
        return successRestResp(all)
    }
"""

findById =

    """
    @ApiOperation(value = "根据id查询", notes = "无参数时查询全部数据")
    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: UUID): RestResp<${_controller.entityClassName}> {
        val entity = ${_controller.daoName}.findById(id)
            ?: throw DataNotFoundException("${_controller.entityClassName}".msgById(id))
        return successRestResp(entity)
    }
"""

create =
    """
    @ApiOperation(value = "新建", notes = "")
    @PostMapping
    fun create(@RequestBody dto: ${_controller.dtoClassName}): RestResp<UUID> {
        val entity = dto.toEntity()
        ${_controller.daoName}.insert(entity)
        return successRestResp(entity.id)
    }
"""

update =

    """
    @ApiOperation(value = "更新", notes = "")
    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: UUID,
               @RequestBody dto: ${_controller.dtoClassName}): RestResp<Int> {
        val entity = (${_controller.daoName}.findById(id)
            ?: throw DataNotFoundException("${_controller.entityClassName}".msgById(id)))
        dto.update(entity)
        val count = ${_controller.daoName}.update(entity)
        return successRestResp(count)
    }
"""

delete =

    """
    @ApiOperation(value = "根据id删除", notes = "")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: UUID): RestResp<Int> {
        val count = ${_controller.daoName}.deleteById(id)
        return successRestResp(count)
    }
"""