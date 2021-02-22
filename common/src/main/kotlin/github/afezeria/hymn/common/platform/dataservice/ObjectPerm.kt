package github.afezeria.hymn.common.platform.dataservice

data class ObjectPerm(
    /**
     *角色id
     */
    val roleId: String,
    /**
     *对象id
     */
    val bizObjectId: String,
    /**
     *创建
     */
    val ins: Boolean,
    /**
     *更新
     */
    val upd: Boolean,
    /**
     *删除
     */
    val del: Boolean,
    /**
     *查看
     */
    val que: Boolean,
    /**
     *查看本人及直接下属
     */
    val queryWithAccountTree: Boolean,
    /**
     *查看本部门
     */
    val queryWithOrg: Boolean,
    /**
     *查看本部门及下级部门
     */
    val queryWithOrgTree: Boolean,
    /**
     *查看全部
     */
    val queryAll: Boolean,
    /**
     *编辑全部
     */
    val editAll: Boolean,
)
