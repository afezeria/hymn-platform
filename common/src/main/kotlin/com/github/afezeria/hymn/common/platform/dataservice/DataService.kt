package com.github.afezeria.hymn.common.platform.dataservice

import org.intellij.lang.annotations.Language

/**
 * 用于自定义对象和部分标准对象的增删改查接口
 * @author afezeria
 */
interface DataService {
    /**
     * 根据api查询对象
     */
    fun getObjectByApi(api: String): ObjectInfo?

    /**
     *  根据id查询对象
     */
    fun getObjectById(id: String): ObjectInfo?

    /**
     * 获取对象权限
     */
    fun getObjectPerm(roleId: String, objectApiName: String): ObjectPerm?

    /**
     * 获取对象字段
     * @return key:field api,value:field info
     */
    fun getFieldApiMap(objectApiName: String): Map<String, FieldInfo>

    /**
     * 根据被关联对象id获取关联该对象的所有字段
     */
    fun getFieldByRefObjectId(objectId: String): List<FieldInfo>

    /**
     * 返回指定角色拥有指定权限的对象的字段
     * edit为true时返回有编辑权限的字段，read为true时返回有读权限的字段
     * 同时为false时返回没有权限的字段
     */
    fun getFieldApiSetWithPerm(
        roleId: String,
        objectApiName: String,
        read: Boolean = false,
        edit: Boolean = false,
    ): Set<String>

    /**
     * 获取对象的所有业务类型
     */
    fun getTypeList(objectApiName: String): Set<TypeInfo>

    /**
     * 返回指定对象下指定角色可用的权限的id列表
     */
    fun getVisibleTypeIdSet(roleId: String, objectApiName: String): Set<String>

    /**
     * 查询共享数据
     * [roleId],[orgId],[accountId]三个参数间为“或”关系
     */
    fun getShare(
        objectApiName: String,
        ids: Collection<String>,
        roleId: String?,
        orgId: String?,
        accountId: String?,
        readOnly: Boolean?,
    ): MutableList<ShareTable>

    /**
     * 共享数据给其他用户
     * [accountId],[roleId],[orgId] 同时只生效一个，优先级按照参数顺序
     *
     * 同时给出多个值时如果第一个值所代表的数据不存在，直接返回false
     *
     * 三个值都为null时返回false
     *
     * @param objectApiName 对象api名称
     * @param accountId 根据帐号id共享
     * @param roleId 根据权限共享
     * @param orgId 根据组织共享
     * @return true:共享成功
     *  false:共享失败
     */
    fun share(
        objectApiName: String,
        dataId: String,
        accountId: String?,
        roleId: String?,
        orgId: String?,
        readOnly: Boolean,
    ): Boolean

    /**
     * @see [share]
     */
    fun shareWithPerm(
        objectApiName: String,
        dataId: String,
        accountId: String?,
        roleId: String?,
        orgId: String?,
        readOnly: Boolean,
    ): Boolean

    /**
     * 获取符合条件的数据的数量
     */
    fun count(
        objectApiName: String,
        expr: String,
        params: Collection<Any>
    ): Long

    fun query(
        objectApiName: String, expr: String, params: Collection<Any>,
    ): MutableList<MutableMap<String, Any?>>

    fun query(
        objectApiName: String, expr: String, params: Collection<Any>, offset: Long, limit: Long,
    ): MutableList<MutableMap<String, Any?>>

    /**
     * 查询数据
     * @param objectApiName 对象api名称
     * @param expr where表达式
     * @param params 参数列表
     * @param offset sql offset参数
     * @param limit sql limit参数
     * @param fieldSet 返回数据中包含的字段，set为空时返回所有字段
     * @return 数据列表
     */
    fun query(
        objectApiName: String,
        expr: String,
        params: Collection<Any>,
        offset: Long,
        limit: Long,
        fieldSet: Set<String>,
    ): MutableList<MutableMap<String, Any?>>

    /**
     * 查询数据
     * @param objectApiName 对象api名称
     * @param id 数据id
     */
    fun queryById(objectApiName: String, id: String): MutableMap<String, Any?>?

    /**
     * 查询数据
     * @param objectApiName 对象api名称
     * @param ids 数据id列表
     */
    fun queryByIds(
        objectApiName: String,
        ids: Collection<String>
    ): MutableList<MutableMap<String, Any?>>

    fun queryWithPerm(
        objectApiName: String, expr: String, params: Collection<Any>,
    ): MutableList<MutableMap<String, Any?>>

    fun queryWithPerm(
        objectApiName: String, expr: String, params: Collection<Any>, offset: Long, limit: Long,
    ): MutableList<MutableMap<String, Any?>>

    /**
     * 根据权限查询数据
     */
    fun queryWithPerm(
        objectApiName: String,
        expr: String,
        params: Collection<Any>,
        offset: Long,
        limit: Long,
        fieldSet: Set<String>,
    ): MutableList<MutableMap<String, Any?>>

    fun queryByIdWithPerm(objectApiName: String, id: String): MutableMap<String, Any?>?

    fun queryByIdsWithPerm(
        objectApiName: String,
        ids: Collection<String>
    ): MutableList<MutableMap<String, Any?>>

    /**
     * 插入数据
     * @param objectApiName 对象api名称
     * @param data 待插入的数据，插入成功后data中将新增key为id的键值对
     * @return 新增数据id
     */
    fun insert(
        objectApiName: String,
        data: Map<String, Any?>,
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
     * 一次最多插入500条
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
     * [read],[update],[share],[owner]同时为false时返回false
     * 帐号/业务对象/数据不存在时返回false
     *
     * @param accountId 用户id
     * @param objectId 对象id
     * @param dataId 数据id
     * @param read 是否具有读权限
     * @param update 是否具有更新权限
     * @param delete 是否具有删除权限
     * @param share 是否具有共享权限
     * @param owner 是否检查当前用户为数据所有者
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