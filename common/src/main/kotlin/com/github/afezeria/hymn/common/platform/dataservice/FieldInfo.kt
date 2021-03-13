package com.github.afezeria.hymn.common.platform.dataservice

import com.github.afezeria.hymn.common.exception.InnerException
import com.github.afezeria.hymn.common.util.toJson

class FieldInfo(
    val id: String,
    /**
     * 所属业务对象id
     */
    val objectId: String,
    /**
     * 名称
     */
    val name: String,
    /**
     * api名称
     */
    val api: String,
    /**
     * 字段类型
     * text(文本),check_box(复选框),check_box_group(复选框组),select(下拉菜单),integer(整型),
     * float(浮点型),money(货币),date(日期),datetime(日期时间),master_slave(主详),reference(关联关系),
     * mreference(多选关联关系),areference(任意关联),summary(汇总),auto(自动编号),picture(图片),files(文件)
     */
    val type: String,
    /**
     * 文本类型最大长度/浮点型整数位长度/整型最大值/图片最大数量
     */
    val maxLength: Int? = null,
    /**
     * 文本类型最小长度/浮点型小数位长度/整型最小值/图片最小数量
     */
    val minLength: Int? = null,
    /**
     * 单选/多选/下拉对应的数据字典id
     */
    val dictId: String? = null,
    /**
     * 副选框和下拉多选的可选个数
     */
    val optionalNumber: Int? = null,
    /**
     * 关联的自定义对象id
     */
    val refId: String? = null,
    /**
     * 当字段为关联字段时，引用数据被删除时的动作 ;;optional_value:[cascade(级联删除引用当前数据的数据),restrict(阻止删除被引用数据),set_null(删除引用字段的值),no_action(无动作)]
     */
    var refDeletePolicy: String? = null,
    /**
     * 汇总对象id
     */
    val sId: String? = null,
    /**
     * 汇总字段id
     */
    val sFieldId: String? = null,
    /**
     * 汇总类型 sum(求和)/count(总数)/min(最小值)/max(最大值)
     */
    val sType: String? = null,
    /**
     * 标准字段类型，
     * create_by_id(创建人id)/create_by(创建人)/modify_by_id(修改人id)/modify_by(修改人)/
     * create_date(创建时间)/modify_date(修改时间)/org_id(组织id)/lock_state(锁定状态)/name(名称)/
     * type_id(业务类型)/owner_id(所有人)
     */
    val standardType: String? = null,
    /**
     * 是否是预定义字段
     */
    val predefined: Boolean,
) {
    fun getTypeDescription(): String = when (type) {
        "text" -> "String"
        "check_box" -> "Boolean"
        "check_box_group" -> "String"
        "select" -> "String"
        "integer" -> "Int"
        "float" -> "Float"
        "money" -> "BigDecimal"
        "date" -> "LocalDateTime"
        "datetime" -> "LocalDateTime"
        "master_slave" -> "String"
        "reference" -> "String"
        "mreference" -> "String"
        "areference" -> "String"
        "picture" -> "String"
        "files" -> "String"
        "summary" -> ""
        "auto" -> ""
        else -> throw InnerException("字段定义错误，未定义的类型:${this.toJson()}")
    }
}
