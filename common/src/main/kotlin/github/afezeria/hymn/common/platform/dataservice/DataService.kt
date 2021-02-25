package github.afezeria.hymn.common.platform.dataservice

import org.intellij.lang.annotations.Language

/**
 * 用于自定义对象和部分标准对象的增删改查接口
 * @author afezeria
 */
interface DataService {
    fun getObjectByApi(api: String): ObjectInfo?
    fun getObjectById(id: String): ObjectInfo?
    fun getObjectPerm(roleId: String, objectApiName: String): ObjectPerm?

    /**
     * key 为 api
     */
    fun getFieldApiMap(objectApiName: String): Map<String, FieldInfo>

    /**
     * 返回[objectApiName] 表示的对象的字段
     * edit为true时返回有编辑权限的字段，read为true时返回有读权限的字段
     * 同时为false时返回没有权限的字段
     */
    fun getFieldApiSetWithPerm(
        roleId: String,
        objectApiName: String,
        read: Boolean = false,
        edit: Boolean = false,
    ): Set<String>

    fun getTypeList(objectApiName: String): Set<TypeInfo>
    fun getVisibleTypeIdSet(roleId: String, objectApiName: String): Set<String>

    /**
     * 查询数据
     * @param objectApiName 对象api名称
     * @param expr where表达式
     * @return 符合条件的数据列表
     */
    fun query(
        objectApiName: String, expr: String,
        offset: Long? = null,
        limit: Long? = null,
    ): MutableList<MutableMap<String, Any?>>

    /**
     * 查询数据
     * @param objectApiName 对象api名称
     * @param expr where表达式
     * @param params sql参数
     * @return 数据列表
     */
    fun query(
        objectApiName: String,
        expr: String,
        params: Collection<Any>,
        offset: Long? = null,
        limit: Long? = null,
        fieldSet: Set<String> = emptySet(),
    ): MutableList<MutableMap<String, Any?>>

    /**
     * 查询数据
     * @param objectApiName 对象api名称
     * @param condition 查询条件，value不为空时条件为 "=" ，为空时条件为 "is null"，多个条件取和
     * @return 符合条件的数据列表
     */
    fun query(
        objectApiName: String,
        condition: Map<String, Any?>,
        offset: Long? = null,
        limit: Long? = null,
    ): MutableList<MutableMap<String, Any?>>

    /**
     * 查询数据
     * @param objectApiName 对象api名称
     * @param id 数据id
     * @return 指定id的数据
     */
    fun queryById(objectApiName: String, id: String): MutableMap<String, Any?>?

    /**
     * 查询数据
     * @param objectApiName 对象api名称
     * @param ids 数据id列表
     * @return 指定id的数据
     */
    fun queryByIds(
        objectApiName: String,
        ids: Collection<String>
    ): MutableList<MutableMap<String, Any?>>

    /**
     * 根据权限查询数据
     * @param objectApiName 对象api名称
     * @param expr where表达式
     * @return 符合条件的数据列表
     */
    fun queryWithPerm(
        objectApiName: String,
        expr: String,
        offset: Long? = null,
        limit: Long? = null,
    ): MutableList<MutableMap<String, Any?>>

    /**
     * 根据权限查询数据
     * @param objectApiName 对象api名称
     * @return 符合条件的数据列表
     */
    fun queryWithPerm(
        objectApiName: String,
        expr: String,
        params: Collection<Any> = emptyList(),
        offset: Long? = null,
        limit: Long? = null,
        fieldSet: Set<String> = emptySet(),
    ): MutableList<MutableMap<String, Any?>>

    /**
     * 根据权限查询数据
     * @param objectApiName 对象api名称
     * @param condition 查询条件，value不为空是条件为 "=" ，为空是条件为 "is null"，多个条件取和
     * @return 符合条件的数据列表
     */
    fun queryWithPerm(
        objectApiName: String,
        condition: Map<String, Any?>,
        offset: Long? = null,
        limit: Long? = null,
    ): MutableList<MutableMap<String, Any?>>

    /**
     * 根据权限查询数据
     * @param objectApiName 对象api名称
     * @return 符合条件的数据列表
     * @return 数据列表
     */
    fun queryByIdWithPerm(objectApiName: String, id: String): MutableMap<String, Any?>?

    fun queryByIdsWithPerm(
        objectApiName: String,
        ids: Collection<String>
    ): MutableList<MutableMap<String, Any?>>

    /**
     * 插入数据
     * @param objectApiName 对象api名称
     * @param data 待插入的数据，插入成功后data中将新增key为id的键值对
     * @param trigger 是否触发触发器
     * @return 新增数据id
     */
    fun insert(
        objectApiName: String,
        data: Map<String, Any?>,
        trigger: Boolean = true
    ): MutableMap<String, Any?>

    /**
     * 批量插入
     * @param objectApiName 对象api名称
     * @param dataList 待插入的数据列表，插入成功后data中将新增key为id的键值对
     * @return 新增数据的id列表
     */
    fun batchInsert(
        objectApiName: String,
        dataList: List<Map<String, Any?>>
    ): MutableList<MutableMap<String, Any?>>

    /**
     * 根据权限插入数据，当前帐号无插入权限时抛出异常
     * @param objectApiName 对象api名称
     * @param data 待插入的数据，插入成功后data中将新增key为id的键值对
     * @return 新增数据id
     */
    fun insertWithPerm(objectApiName: String, data: Map<String, Any?>): MutableMap<String, Any?>

    /**
     * 根据权限批量插入
     * @param objectApiName 对象api名称
     * @param dataList 待插入的数据列表，插入成功后data中将新增key为id的键值对
     * @return 新增数据的id列表
     */
    fun batchInsertWithPerm(
        objectApiName: String,
        dataList: List<Map<String, Any?>>
    ): MutableList<MutableMap<String, Any?>>


    fun insertWithoutTrigger(
        objectApiName: String,
        data: MutableMap<String, Any?>
    ): MutableMap<String, Any?>

    /**
     * 批量插入
     * 不触发触发器
     * @param objectApiName 对象api名称
     * @param dataList 待插入的数据列表，插入成功后列表的每一项中将新增key为id的键值对
     * @return 新增数据的id列表
     */
    fun bulkInsertWithoutTrigger(
        objectApiName: String,
        dataList: List<Map<String, Any?>>,
    ): MutableList<MutableMap<String, Any?>>

    /**
     * 更新数据
     * 系统预定义字段除owner外不会被覆盖
     * 给定的id表示的数据不存在时返回空map或抛出异常
     * @param objectApiName 对象api名称
     * @param data 待更新数据，必须包含id
     * @return 更新后的数据
     */
    fun update(
        objectApiName: String,
        data: MutableMap<String, Any?>,
    ): MutableMap<String, Any?>

    /**
     * 更新数据
     * 系统预定义字段除owner外不会被覆盖
     * @param objectApiName 对象api名称
     * @param data 待更新数据，必须包含id
     * @return 更新后的数据
     */
    fun updateWithoutTrigger(
        objectApiName: String,
        data: MutableMap<String, Any?>,
    ): MutableMap<String, Any?>


    /**
     * 根据权限更新数据
     * 部分更新，只更新设置了值的字段
     * 系统预定义字段除owner外不会被覆盖
     * @param objectApiName 对象api名称
     * @param data 待更新数据，必须包含id
     * @return 更新后的数据
     */
    fun updateWithPerm(
        objectApiName: String,
        data: MutableMap<String, Any?>,
    ): MutableMap<String, Any?>

    /**
     * 批量更新数据
     * 系统预定义字段除owner外不会被覆盖
     * @param objectApiName 对象api名称
     * @param dataList 待更新数据，必须包含id
     * @return 更新后的数据
     */
    fun batchUpdate(
        objectApiName: String,
        dataList: MutableList<MutableMap<String, Any?>>,
    ): MutableList<MutableMap<String, Any?>>

    /**
     * 根据权限批量更新数据
     * 部分更新，只更新设置了值的字段
     * 系统预定义字段除owner外不会被覆盖
     * @param objectApiName 对象api名称
     * @param dataList 待更新数据，必须包含id
     * @return 更新后的数据
     */
    fun batchUpdateWithPerm(
        objectApiName: String,
        dataList: MutableList<MutableMap<String, Any?>>,
    ): MutableList<MutableMap<String, Any?>>

    /**
     * 根据权限批量更新数据
     * 系统预定义字段除owner外不会被覆盖
     * @param objectApiName 对象api名称
     * @param dataList 待更新数据，必须包含id
     * @return 更新后的数据
     */
    fun batchUpdateWithoutTrigger(
        objectApiName: String,
        dataList: MutableList<MutableMap<String, Any?>>
    ): MutableList<MutableMap<String, Any?>>

    /**
     * 根据id删除数据
     * @param objectApiName 对象api名称
     * @param id 待删除数据的id
     * @return 已删除的数据，id不存在时返回null
     */
    fun delete(objectApiName: String, id: String): MutableMap<String, Any?>?

    /**
     * 根据id列表批量删除数据
     * @param objectApiName 对象api名称
     * @param ids 待删除数据id列表
     * @return pair.first 数据不存在的id列表，全部数据都被删除时为空列表， pair.second 已删除的数据
     */
    fun batchDelete(objectApiName: String, ids: MutableList<String>)
        : MutableList<MutableMap<String, Any?>>

    /**
     * 根据id和权限删除数据，无权限时抛出异常
     * @param objectApiName 对象api名称
     * @param id 待删除数据的id
     * @return 已删除的数据，id不存在时返回null
     */
    fun deleteWithPerm(objectApiName: String, id: String): MutableMap<String, Any?>?

    /**
     * 根据id列表和权限批量删除数据
     * @param objectApiName 对象api名称
     * @param ids 待删除数据id列表
     * @return pair.first 数据不存在的id列表，全部数据都被删除时为空列表， pair.second 已删除的数据
     */
    fun batchDeleteWithPerm(objectApiName: String, ids: MutableList<String>)
        : MutableList<MutableMap<String, Any?>>

    fun deleteWithoutTrigger(objectApiName: String, id: String): MutableMap<String, Any?>?

    /**
     * 根据id列表批量删除数据
     * @param objectApiName 对象api名称
     * @param ids 待删除数据id列表
     * @return 已删除的数据列表
     */
    fun batchDeleteWithoutTrigger(
        objectApiName: String,
        ids: List<String>,
    ): MutableList<MutableMap<String, Any?>>


    /**
     * 执行sql语句，进行的修改不会触发触发器，也不会记录在数据变更历史中
     * @param sql 对象api名称
     */
    fun sql(
        @Language("sql") sql: String,
        vararg params: Any?
    ): MutableList<MutableMap<String, Any?>>

    /**
     * 执行sql语句，进行的修改不会触发触发器，也不会记录在数据变更历史中
     * @param sql 对象api名称
     * @param params 参数
     */
    fun sql(@Language("sql") sql: String, params: List<Any?>): MutableList<MutableMap<String, Any?>>

    /**
     * 执行sql语句，进行的修改不会触发触发器，也不会记录在数据变更历史中
     * @param sql 对象api名称
     * @param params 命名参数
     */
    fun sql(
        @Language("sql") sql: String,
        params: Map<String, Any?>
    ): MutableList<MutableMap<String, Any?>>

    /**
     * 是否具有[dataId]所代表的数据的权限
     *
     * 数据权限类型共4种：查看，更新，删除，共享
     *
     * 用户对指定数据可能拥有的权限有5种情况：
     *
     * 1 当前用户为所有者 拥有全部权限
     *
     * 2 当前用户为管理员 至少拥有 共享 权限，
     * 其他权限与当前管理员帐号拥有的id为[objectId]的对象的权限和是否被共享当前数据有关
     *
     * 3 当前用户拥有id为[objectId]的对象的查看其他人数据的权限 拥有权限：查看
     *
     * 4 当前用户被共享了指定数据 拥有权限：查看，更新
     *
     * 5 当前用户被只读共享了指定数据 拥有权限：查看
     *
     * [read],[update],[share],[owner]同时为null时返回false
     * 帐号/业务对象/数据不存在时返回false
     *
     * @param accountId 用户id
     * @param objectId 对象id
     * @param dataId 数据id
     * @param read 是否具有读权限，为null时不检查
     * @param update 是否具有更新权限，为null时不检查
     * @param share 是否具有共享权限，为null时不检查
     * @param owner 是否检查当前用户为数据所有者，为null时不检查
     * @exception [github.afezeria.hymn.common.exception.DataNotFoundException] 当对象或数据不存在时抛出
     */
    fun hasDataPerm(
        accountId: String,
        objectId: String,
        dataId: String,
        read: Boolean = false,
        update: Boolean = false,
        delete: Boolean = false,
        share: Boolean = false,
        owner: Boolean = false,
    ): Boolean

}