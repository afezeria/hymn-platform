package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.BizObjectField
import java.time.LocalDateTime
import io.swagger.annotations.*
import java.util.*

/**
 * @author afezeria
 */
class BizObjectFieldDto(
    @ApiModelProperty(value = "所属业务对象id ;;fk:[core_biz_object cascade];idx")
    var bizObjectId: String,
    @ApiModelProperty(value = "名称，用于页面显示")
    var name: String,
    @ApiModelProperty(value = "api名称，用于触发器和自定义接口")
    var api: String,
    @ApiModelProperty(value = "字段类型 ;;optional_value:[text(文本),check_box(复选框),check_box_group(复选框组),select(下拉菜单),integer(整型),float(浮点型),money(货币),date(日期),datetime(日期时间),master_slave(主详),reference(关联关系),mreference(多选关联关系),summary(汇总),auto(自动编号),picture(图片);")
    var type: String,
    @ApiModelProperty(value = "字段启用状态，false表示停用，字段停用时从视图中移除，删除时清空没一行中对应字段数据", required = true)
    var active: Boolean? = null,
    @ApiModelProperty(value = "是否启用历史记录", required = true)
    var history: Boolean? = null,
    @ApiModelProperty(value = "默认值，可选择其他表中的字段，由后端处理，新建时与页面布局一起返回给前端", required = true)
    var defaultValue: String? = null,
    @ApiModelProperty(value = "公式，js代码，由前端处理，新建和编辑时拼接成监听函数与页面布局一起返回给前端", required = true)
    var formula: String? = null,
    @ApiModelProperty(value = "文本类型最大长度/浮点型整数位长度/整型最大值/图片最大数量", required = true)
    var maxLength: Int? = null,
    @ApiModelProperty(value = "文本类型最小长度/浮点型小数位长度/整型最小值/图片最小数量", required = true)
    var minLength: Int? = null,
    @ApiModelProperty(value = "文本类型显示行数/下拉选项 （多选）前端显示行数", required = true)
    var visibleRow: Int? = null,
    @ApiModelProperty(value = "单选/多选/下拉对应的数据字典id;idx", required = true)
    var dictId: String? = null,
    @ApiModelProperty(value = "下拉字段依赖的主字段id", required = true)
    var masterFieldId: String? = null,
    @ApiModelProperty(value = "副选框和下拉多选的可选个数", required = true)
    var optionalNumber: Int? = null,
    @ApiModelProperty(value = "关联的自定义对象id", required = true)
    var refId: String? = null,
    @ApiModelProperty(value = "相关列表标签，当前对象在被关联对象的相关列表中显示的标签，为空时表示不能显示在被关联对象的相关列表中", required = true)
    var refListLabel: String? = null,
    @ApiModelProperty(value = "当字段为关联字段时，引用数据被删除时的动作。 cascade 级联删除当前对象数据, restrict 阻止删除被引用对象, null 无动作", required = true)
    var refDeletePolicy: String? = null,
    @ApiModelProperty(value = "字段为汇总字段时表示对子表的过滤条件，字段为引用/主从字段时表示在创建当前对象时查找引用对象的过滤条件，sql where表达式", required = true)
    var queryFilter: String? = null,
    @ApiModelProperty(value = "汇总对象id", required = true)
    var sId: String? = null,
    @ApiModelProperty(value = "汇总字段id", required = true)
    var sFieldId: String? = null,
    @ApiModelProperty(value = "汇总类型 ;;optional_value:[sum(求和),count(总数),min(最小值),max(最大值)]", required = true)
    var sType: String? = null,
    @ApiModelProperty(value = "编号规则，{000} 递增序列，必填，实际序号大小小于0的个数时将会在前面补0 ; {yyyy}/{yy} 年; {mm} 月; {dd} 日", required = true)
    var genRule: String? = null,
    @ApiModelProperty(value = "备注", required = true)
    var remark: String? = null,
    @ApiModelProperty(value = "说明，显示在页面上的帮助信息", required = true)
    var help: String? = null,
    @ApiModelProperty(value = "多选字段中间表视图名，中间表名为视图名加上前缀 core_ ，表结构为（s_id,t_id)，s_id 为当前数据id， t_id为关联数据id", required = true)
    var joinViewName: String? = null,
    @ApiModelProperty(value = "标准类型 自定义字段不能设置该值，用于处理模块对象和标准对象的特殊字段的类型 ;; optional_value:[create_by_id(创建人id), create_by(创建人), modify_by_id(修改人id), modify_by(修改人), create_date(创建时间), modify_date(修改时间), org_id(组织id), lock_state(锁定状态), name(名称), type_id(业务类型), owner_id(所有人)]", required = true)
    var standardType: String? = null,
    @ApiModelProperty(value = "是否是预定义字段，区分对象中的自定义字段与预定义字段，预定义字段该值为true且source_column与api相等，后台对象管理界面中不能删除和修改")
    var isPredefined: Boolean,
){
    fun toEntity(): BizObjectField {
        return BizObjectField(
            bizObjectId = bizObjectId,
            name = name,
            api = api,
            type = type,
            active = active,
            history = history,
            defaultValue = defaultValue,
            formula = formula,
            maxLength = maxLength,
            minLength = minLength,
            visibleRow = visibleRow,
            dictId = dictId,
            masterFieldId = masterFieldId,
            optionalNumber = optionalNumber,
            refId = refId,
            refListLabel = refListLabel,
            refDeletePolicy = refDeletePolicy,
            queryFilter = queryFilter,
            sId = sId,
            sFieldId = sFieldId,
            sType = sType,
            genRule = genRule,
            remark = remark,
            help = help,
            joinViewName = joinViewName,
            standardType = standardType,
            isPredefined = isPredefined,
        )
    }

    fun update(entity: BizObjectField) {
        entity.also {
            it.bizObjectId = bizObjectId
            it.name = name
            it.api = api
            it.type = type
            it.active = active
            it.history = history
            it.defaultValue = defaultValue
            it.formula = formula
            it.maxLength = maxLength
            it.minLength = minLength
            it.visibleRow = visibleRow
            it.dictId = dictId
            it.masterFieldId = masterFieldId
            it.optionalNumber = optionalNumber
            it.refId = refId
            it.refListLabel = refListLabel
            it.refDeletePolicy = refDeletePolicy
            it.queryFilter = queryFilter
            it.sId = sId
            it.sFieldId = sFieldId
            it.sType = sType
            it.genRule = genRule
            it.remark = remark
            it.help = help
            it.joinViewName = joinViewName
            it.standardType = standardType
            it.isPredefined = isPredefined
        }
    }
}
