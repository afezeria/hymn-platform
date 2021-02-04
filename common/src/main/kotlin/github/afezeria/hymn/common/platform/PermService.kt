package github.afezeria.hymn.common.platform

/**
 * @author afezeria
 */
interface PermService {
    /**
     * 是否具有指定功能的访问权限
     * 与[github.afezeria.hymn.common.ann.Function]相关
     * @param roleId 角色id
     * @param name 功能名称，[github.afezeria.hymn.common.ann.Function.name]的值，注解name值为空字符串时返回true
     */
    fun hasFunctionPerm(roleId: String, name: String): Boolean

    /**
     * 检查当前用户是否具有指定功能的访问权限
     * @see [hasFunctionPerm]
     */
    fun hasFunctionPerm(name: String): Boolean {
        return hasFunctionPerm(Session.getInstance().roleId, name)
    }

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
        read: Boolean? = null,
        update: Boolean? = null,
        share: Boolean? = null,
        owner: Boolean? = null,
    ): Boolean


    /**
     * 当前用户是否具有[dataId]所代表的数据的权限
     * @see [hasDataPerm]
     */
    fun hasDataPerm(
        objectId: String,
        dataId: String,
        read: Boolean? = null,
        update: Boolean? = null,
        share: Boolean? = null,
        owner: Boolean? = null,
    ): Boolean {
        return hasDataPerm(Session.getInstance().accountId, dataId, read, update, share, owner)
    }


    /**
     * 是否具有相关对象权限
     *
     * 对象权限共9种： 查看(que)，创建(ins)，更新(upd)，删除(del)，查看本人及直接下属(query_with_account_tree)，
     * 查看本组织(query_with_org)，查看本部门及下级组织(query_with_org_tree)，
     * 查看全部(query_all)，编辑全部(edit_all)
     *
     * 查看本人及直接下属，查看本组织，查看本组织及下级组织，查看全部 也是一种查看权限
     *
     * 权限分为3级，从低到高分别为：
     *
     * 1 查看 最基础的权限，没有查看权限时就没有其他权限
     *
     * 2 创建，更新，删除，查看本人及直接下属，查看本组织，查看本组织及下级组织，查看全部
     * 更新和删除默认只对所有者为自己的数据其作用
     *
     * 3 编辑全部 具有全部权限，并可以更新和删除任意数据
     *
     * [query],[create],[update],[delete]同时为null时返回false
     *
     * @param roleId 角色id
     * @param objectId 对象id
     * @param create 是否具有创建权限，为null时不检查
     * @param update 是否具有更新权限，为null时不检查
     * @param query 是否具有查看权限，为null时不检查
     * @param delete 是否具有删除权限，为null时不检查
     * @exception [github.afezeria.hymn.common.exception.DataNotFoundException] 当对象不存在时抛出
     */
    fun hasObjectPerm(
        roleId: String,
        objectId: String,
        query: Boolean? = null,
        create: Boolean? = null,
        update: Boolean? = null,
        delete: Boolean? = null
    ): Boolean

    /**
     * 当前用户是否具有相关对象权限
     * @see [hasObjectPerm]
     */
    fun hasObjectPerm(
        objectId: String,
        query: Boolean? = null,
        create: Boolean? = null,
        update: Boolean? = null,
        delete: Boolean? = null
    ): Boolean {
        return hasObjectPerm(Session.getInstance().roleId, objectId, query, create, update, delete)
    }

    /**
     * 是否具有字段权限
     *
     * 字段权限分两种：查看，更新
     *
     * 具有查看权限时能查看相关数据的指定字段的值
     *
     * 具有更新权限时能更新相关数据的指定字段的值
     *
     * 当前方法只判断字段权限，不检查对象权限，在实际操作中可能返回值为true，
     * 但因为不具有对象或字段权限等原因而无法修改相关字段的数据
     *
     * [read],[edit]同时为null时返回false
     *
     * @param roleId 角色id
     * @param objectId 字段所属对象的id
     * @param fieldId 字段id
     * @param read 是否具有查看权限，为null时不检查
     * @param edit 是否具有更新权限，为null时不检查
     * @exception [github.afezeria.hymn.common.exception.DataNotFoundException] 当对象或字段不存在时抛出
     */
    fun hasFieldPerm(
        roleId: String,
        objectId: String,
        fieldId: String,
        read: Boolean? = null,
        edit: Boolean? = null
    ): Boolean

    /**
     * 当前用户是否具有字段权限
     * @see [hasFieldPerm]
     */
    fun hasFieldPerm(
        objectId: String,
        fieldId: String,
        read: Boolean? = null,
        edit: Boolean? = null
    ): Boolean {
        return hasFieldPerm(Session.getInstance().roleId, objectId, fieldId, read, edit)
    }
}