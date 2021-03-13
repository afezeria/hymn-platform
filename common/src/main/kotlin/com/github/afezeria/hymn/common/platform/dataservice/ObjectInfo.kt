package com.github.afezeria.hymn.common.platform.dataservice

data class ObjectInfo(
    val id: String,
    /**
     * 业务对象名称，用于页面显示
     */
    val name: String,
    /**
     * 业务对象api，唯一
     */
    val api: String,
    /**
     * 类型 custom/module/remote
     */
    val type: String,
    /**
     * 模块api，所有自定义对象该字段都为null，不为null表示该对象属于指定模块
     */
    val moduleApi: String? = null,
    /**
     * 模块对象及远程对象是否可以新增数据
     */
    val canInsert: Boolean? = null,
    /**
     * 模块对象是及远程对象否可以更新数据
     */
    val canUpdate: Boolean? = null,
    /**
     * 模块对象是及远程对象否可以删除数据
     */
    val canDelete: Boolean? = null,
    /**
     * 是否支持软删除，用于标记模块对象的删除行为，模块对象表中有bool类型的deleted字段作为删除标记时 can_soft_delete 可以为true，当 can_soft_delete 为 true 时，dataService的数据删除动作为设置 deleted 的值为 true，当 can_soft_delete 为 false 时，数据删除时直接从表中删除数据
     */
    val canSoftDelete: Boolean? = null,
)