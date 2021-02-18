package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.dto.BizObjectFieldDto
import github.afezeria.hymn.core.module.entity.BizObjectField

/**
 * @author afezeria
 */
internal interface BizObjectFieldService {

    fun removeById(id: String): Int

    fun update(id: String, dto: BizObjectFieldDto): Int

    fun create(dto: BizObjectFieldDto): String

    fun findAll(): MutableList<BizObjectField>

    fun findById(id: String): BizObjectField?

    fun findByIds(ids: List<String>): MutableList<BizObjectField>

    fun findByBizObjectIdAndApi(
        bizObjectId: String,
        api: String,
    ): BizObjectField?

    fun findByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectField>

    /**
     * 创建默认字段，[objId] 表示的对象已存在字段时抛出 [github.afezeria.hymn.common.util.InnerException]
     * @param objId 新建的对象的id
     * @param fieldName name字段的名称
     * @param autoRule 自动编号规则，name字段类型为text时为空
     */
    fun createDefaultField(objId: String, fieldName: String, autoRule: String?)

    fun pageFind(pageSize: Int, pageNum: Int): List<BizObjectField>


}