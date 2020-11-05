package github.afezeria.hymn.common.platform

/**
 * @author afezeria
 */
interface DataService {
    /**
     * 查询数据
     * @param objectApiName 对象api名称
     * @param expr where表达式
     * @return 符合条件的数据列表
     */
    fun query(objectApiName: String, expr: String): MutableList<MutableMap<String, Any>>

    /**
     * 查询数据
     * @param objectApiName 对象api名称
     * @param expr where表达式
     * @param params sql参数
     * @return 数据列表
     */
    fun query(objectApiName: String, expr: String, params: Array<Any>): MutableList<MutableMap<String, Any>>

    /**
     * 查询数据
     * @param objectApiName 对象api名称
     * @param condition 查询条件，value不为空是条件为 "=" ，为空是条件为 "is null"，多个条件取和
     * @return 符合条件的数据列表
     */
    fun query(objectApiName: String, condition: Map<String, Any?>): MutableList<MutableMap<String, Any>>

    /**
     * 查询数据
     * @param objectApiName 对象api名称
     * @param id 数据id
     * @return 指定id的数据
     */
    fun queryById(objectApiName: String, id: String): MutableMap<String, Any>?

    /**
     * 根据权限查询数据
     * @param objectApiName 对象api名称
     * @param expr where表达式
     * @return 符合条件的数据列表
     */
    fun queryWithRole(objectApiName: String, expr: String): MutableList<MutableMap<String, Any>>

    /**
     * 根据权限查询数据
     * @param objectApiName 对象api名称
     * @return 符合条件的数据列表
     */
    fun queryWithRole(objectApiName: String, expr: String, params: Array<Any>): MutableList<MutableMap<String, Any>>

    /**
     * 根据权限查询数据
     * @param objectApiName 对象api名称
     * @param condition 查询条件，value不为空是条件为 "=" ，为空是条件为 "is null"，多个条件取和
     * @return 符合条件的数据列表
     */
    fun queryWithRole(objectApiName: String, condition: Map<String, Any?>): MutableList<MutableMap<String, Any>>

    /**
     * 根据权限查询数据
     * @param objectApiName 对象api名称
     * @return 符合条件的数据列表
     * @return 数据列表
     */
    fun queryByIdWithRole(objectApiName: String, id: String): MutableList<MutableMap<String, Any>>

    /**
     * 插入数据
     * @param objectApiName 对象api名称
     * @param data 待插入的数据，插入成功后data中将新增key为id的键值对
     * @return 新增数据id
     */
    fun insert(objectApiName: String, data: MutableMap<String, Any?>): String

    /**
     * 插入数据
     * @param objectApiName 对象api名称
     * @param data 待插入的数据，插入成功后data中将新增key为id的键值对
     * @param trigger 是否触发触发器
     * @return 新增数据id
     */
    fun insert(objectApiName: String, data: MutableMap<String, Any?>, trigger: Boolean): String

    /**
     * 根据权限插入数据，当前帐号无插入权限时抛出异常
     * @param objectApiName 对象api名称
     * @param data 待插入的数据，插入成功后data中将新增key为id的键值对
     * @return 新增数据id
     */
    fun insertWithRole(objectApiName: String, data: MutableMap<String, Any?>): String

    /**
     * 批量插入
     * @param objectApiName 对象api名称
     * @param dataList 待插入的数据列表，插入成功后data中将新增key为id的键值对
     * @return 新增数据的id列表
     */
    fun batchInsert(objectApiName: String, dataList: MutableList<MutableMap<String, Any?>>): List<String>

    /**
     * 批量插入
     * @param objectApiName 对象api名称
     * @param dataList 待插入的数据列表，插入成功后列表的每一项中将新增key为id的键值对
     * @param trigger 是否触发触发器
     * @return 新增数据的id列表
     */
    fun batchInsert(objectApiName: String, dataList: MutableList<MutableMap<String, Any?>>, trigger: Boolean)

    /**
     * 根据权限批量插入
     * @param objectApiName 对象api名称
     * @param dataList 待插入的数据列表，插入成功后data中将新增key为id的键值对
     * @return 新增数据的id列表
     */
    fun batchInsertWithRole(objectApiName: String, dataList: MutableList<MutableMap<String, Any?>>)

    /**
     * 更新数据
     * 更新方式为全覆盖， [data] 中缺少的字段将被视为null
     * 系统预定义字段除owner外不会被覆盖
     * @param objectApiName 对象api名称
     * @param data 待更新数据，必须包含id
     * @return 更新后的数据
     */
    fun update(objectApiName: String, data: MutableMap<String, Any?>): MutableMap<String, Any?>

    /**
     * 更新数据
     * 更新方式为全覆盖， [data] 中缺少的字段将被视为null
     * 系统预定义字段除owner外不会被覆盖
     * @param objectApiName 对象api名称
     * @param data 待更新数据，必须包含id
     * @param trigger 是否触发触发器
     * @return 更新后的数据
     */
    fun update(objectApiName: String, data: MutableMap<String, Any?>, trigger: Boolean): MutableMap<String, Any?>

    /**
     * 更新数据
     * 更新方式为部分覆盖，只会更新 [data] 中包含的字段
     * 系统预定义字段除owner外不会被更新
     * @param objectApiName 对象api名称
     * @param id 待更新数据的id
     * @param data 待更新数据，必须包含id
     * @return 更新后的数据
     */
    fun update(objectApiName: String, id: String, data: MutableMap<String, Any?>): MutableMap<String, Any?>

    /**
     * 更新数据
     * 更新方式为部分覆盖，只会更新 [data] 中包含的字段
     * 系统预定义字段除owner外不会被更新
     * @param objectApiName 对象api名称
     * @param id 待更新数据的id
     * @param data 待更新数据，必须包含id
     * @param trigger 是否触发触发器
     * @return 更新后的数据
     */
    fun update(objectApiName: String, id: String, data: MutableMap<String, Any?>, trigger: Boolean): MutableMap<String, Any?>

    /**
     * 根据权限更新数据
     * 更新方式为全覆盖， [data] 中缺少的字段将被视为null
     * 系统预定义字段除owner外不会被覆盖
     * @param objectApiName 对象api名称
     * @param data 待更新数据，必须包含id
     * @return 更新后的数据
     */
    fun updateWithRole(objectApiName: String, data: MutableMap<String, Any?>): MutableMap<String, Any?>

    /**
     * 更新数据
     * 更新方式为部分覆盖，只会更新 [data] 中包含的字段
     * 系统预定义字段除owner外不会被更新
     * @param objectApiName 对象api名称
     * @param id 待更新数据的id
     * @param data 待更新数据，必须包含id
     * @return 更新后的数据
     */
    fun updateWithRole(objectApiName: String, id: String, data: MutableMap<String, Any?>): MutableMap<String, Any?>

    /**
     * 批量更新数据
     * 更新方式为全覆盖， [data] 中缺少的字段将被视为null
     * 系统预定义字段除owner外不会被覆盖
     * @param objectApiName 对象api名称
     * @param data 待更新数据，必须包含id
     * @return 更新后的数据
     */
    fun batchUpdate(objectApiName: String, data: MutableMap<String, Any?>): MutableList<MutableMap<String, Any?>>

    /**
     * 批量更新数据
     * 更新方式为全覆盖， [data] 中缺少的字段将被视为null
     * 系统预定义字段除owner外不会被覆盖
     * @param objectApiName 对象api名称
     * @param data 待更新数据，必须包含id
     * @param trigger 是否触发触发器
     * @return 更新后的数据
     */
    fun batchUpdate(objectApiName: String, data: MutableMap<String, Any?>, trigger: Boolean): MutableList<MutableMap<String, Any?>>

    /**
     * 根据权限批量更新数据
     * 更新方式为全覆盖， [data] 中缺少的字段将被视为null
     * 系统预定义字段除owner外不会被覆盖
     * @param objectApiName 对象api名称
     * @param data 待更新数据，必须包含id
     * @return 更新后的数据
     */
    fun batchUpdateWithRole(objectApiName: String, data: MutableMap<String, Any?>): MutableList<MutableMap<String, Any?>>

    /**
     * 根据id删除数据
     * @param objectApiName 对象api名称
     * @param id 待删除数据的id
     * @return 已删除的数据，id不存在时返回null
     */
    fun delete(objectApiName: String, id: String): MutableMap<String, Any?>?

    /**
     * 根据id删除数据
     * @param objectApiName 对象api名称
     * @param id 待删除数据的id
     * @param trigger 是否触发触发器
     * @return 已删除的数据，id不存在时返回null
     */
    fun delete(objectApiName: String, id: String, trigger: Boolean): MutableMap<String, Any?>?

    /**
     * 根据id和权限删除数据，无权限时抛出异常
     * @param objectApiName 对象api名称
     * @param id 待删除数据的id
     * @return 已删除的数据，id不存在时返回null
     */
    fun deleteWithRole(objectApiName: String, id: String): MutableMap<String, Any?>?

    /**
     * 根据id列表批量删除数据
     * @param objectApiName 对象api名称
     * @param ids 待删除数据id列表
     * @return pair.first 数据不存在的id列表，全部数据都被删除时为空列表， pair.second 已删除的数据
     */
    fun batchDelete(objectApiName: String, ids: MutableList<String>)
        : Pair<MutableList<String>, MutableList<MutableMap<String, Any?>>>

    /**
     * 根据id列表批量删除数据
     * @param objectApiName 对象api名称
     * @param ids 待删除数据id列表
     * @param trigger 是否触发触发器
     * @return 已删除的数据列表
     */
    fun batchDelete(objectApiName: String, ids: MutableList<String>, trigger: Boolean): MutableList<MutableMap<String, Any?>>

    /**
     * 根据id列表和权限批量删除数据
     * @param objectApiName 对象api名称
     * @param ids 待删除数据id列表
     * @return pair.first 数据不存在的id列表，全部数据都被删除时为空列表， pair.second 已删除的数据
     */
    fun batchDeleteWithRole(objectApiName: String, ids: MutableList<String>)
        : Pair<MutableList<String>, MutableList<MutableMap<String, Any?>>>


    /**
     * 执行sql语句，进行的修改不会触发触发器，也不会记录在数据变更历史中
     * @param sql 对象api名称
     */
    fun sql(sql: String): MutableList<MutableMap<String, Any>>

    /**
     * 执行sql语句，进行的修改不会触发触发器，也不会记录在数据变更历史中
     * @param sql 对象api名称
     * @param params 参数
     */
    fun sql(sql: String, params: Array<Any>): MutableList<MutableMap<String, Any>>

    /**
     * 执行sql语句，进行的修改不会触发触发器，也不会记录在数据变更历史中
     * @param sql 对象api名称
     * @param params 命名参数
     */
    fun sql(sql: String, params: Map<String, Any>): MutableList<MutableMap<String, Any>>
}