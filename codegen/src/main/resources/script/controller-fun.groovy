package script

mapping = [
    sys_core_b_object_field           : ['findByObjectId'],
    sys_core_b_object_field_perm      : ['findByObjectId'],
    sys_core_b_object_layout          : ['findByObjectId'],
    sys_core_b_object_perm            : ['findByObjectId'],
    sys_core_b_object_record_layout   : ['findByObjectId'],
    sys_core_b_object_record_type     : ['findByObjectId'],
    sys_core_b_object_record_type_perm: ['findByObjectId'],
    sys_core_b_object_trigger         : ['findByObjectId'],
    sys_core_b_object_validator       : ['findByObjectId'],
    sys_core_b_object_mapping_item    : ['findByMappingId'],
]

findByObjectId =
    """
    @ApiOperation(value = "根据对象id查询", notes = "无参数时查询全部数据")
    @ApiImplicitParams(
        ApiImplicitParam(name = "objectId", value = "对象id", required = false, dataType = "String")
    )
    @GetMapping
    fun findByObjectId(@RequestParam("objectId", required = false) objectId: UUID?): RestResp<${_controller.entityClassName}> {
        return if (objectId == null) {
            val all = ${_controller.daoName}.findAll()
            successRestResp(all)
        } else {
            val entityList = ${_controller.daoName}.findByObjectId(objectId)
            successRestResp(entityList)
        }
    }
"""

findByMappingId =
    """
    @ApiOperation(value = "根据映射表id查询", notes = "")
    @ApiImplicitParams(
        ApiImplicitParam(name = "mappingId", value = "映射表id", required = true, dataType = "String")
    )
    @GetMapping
    fun findByMappingId(@RequestParam("mappingId", required = true) mappingId: UUID): RestResp<${_controller.entityClassName}> {
        val entityList = ${_controller.daoName}.findByMappingId(mappingId)
        return successRestResp(entityList)
    }
"""
