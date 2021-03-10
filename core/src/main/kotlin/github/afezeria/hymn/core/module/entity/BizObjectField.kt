package github.afezeria.hymn.core.module.entity

import github.afezeria.hymn.common.db.AbstractEntity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 *
 * 业务对象字段
 *
 * @author afezeria
 */
@ApiModel(
    value = "业务对象字段", description = """业务对象字段

字段类型：文本(text),复选框(check_box),复选框组(check_box_group),下拉菜单(select),整型(integer),
浮点型(float),货币(money),日期(date),日期时间(datetime),主详(master_slave),关联关系(reference),
多选关联(mreference),任意关联(areference),汇总(summary),自动编号(auto),图片(picture);
公共可选字段：remark （备注，只显示在管理员界面），help （帮助文本，显示在对象详情界面）
通用字段：default_value （默认值，后端处理，字段间不能联动），formula （前端处理）

type: 文本 text
required: min_length （最小长度）, max_length （最大长度）, visible_row （显示行数）
optional: default_value, formula
rule: min_length >= 0, max_length <= 50000 , visible_row > 0, min_length <= max_length, (if api = 'name' than max_length <=255)

type: 复选框 check_box
remark: 值为 true/false
required:
optional: default_value

type: 复选框组 check_box_group
remark: 值为字典项代码，多个值时以英文逗号分割，顺序无关
required: optional_number （可选个数）, dict_id （引用字典id）
optional: default_value, formula
rule: optional_number > 0, (dict_id is not null) or (tmp is not null)

type: 下拉菜单 select
remark: 值为字典项代码，多个值时以英文逗号分割，顺序无关
required: optional_number （可选个数）, dict_id （引用字典id）,visible_row (显示行数）
optional: default_value, formula, master_field_id （依赖字段id，必须是当前对象的字段，且类型为check_box/select/multiple_select）
rule: optional_number > 0, (dict_id is not null) or (tmp is not null)

type: 整型 integer
remark: 后台类型为 Long
required: min_length （最小值）, max_length （最大值）
optional: default_value, formula
rule: min_length <= max_length

type: 浮点 float
remark: 后台类型为 Double
required: min_length （小数位长度）, max_length （整数位长度）
optional: default_value, formula
rule: min_length >= 0, max_length >= 1, (min_length + max_length) <= 18

type: 货币 money
remark: 后台类型为 BigDecimal
required: min_length （小数位长度）, max_length （整数位长度）
optional: default_value, formula
rule: min_length >= 0, max_length >= 1

type: 日期 date
remark: 格式 yyyy-MM-dd
required:
optional: default_value, formula

type: 日期时间 datetime
remark: 格式 yyyy-MM-dd HH:mm:ss.SSSZ
required:
optional: default_value, formula

type: 主详 master_slave
remark: 值为主表数据id
required: ref_id （引用对象id）, ref_list_label （引用对象相关列表显示的标签）
optional: default_value, formula, query_filter
rule:

type: 关联 reference
remark: 值为引用数据id
required: ref_id （引用对象id）, ref_delete_policy （引用对象数据被删除时是否阻止）
optional: default_value, formula, query_filter, ref_list_label （引用对象相关列表显示的标签）

type: 多选关联 mreference
remark: 值为引用数据id，多个id间以英文逗号分隔
required: ref_id （引用对象id）, ref_delete_policy （引用对象数据被删除时是否阻止）
optional: default_value, formula, query_filter, ref_list_label （引用对象相关列表显示的标签）

type: 任意关联 areference
remark: 格式为：业务对象id,数据id;对象名称,数据name字段。默认不提供任意关联字段，
    需要增加该类型字段时自行在字段资源表中添加一行column_name 为 aref\d{3} 格式的数据
required:
optional:  query_filter, filter_list

type: 汇总 summary
remark: 数据库中没有实际的列，由后端实时查询后显示在页面上
required: s_id （子对象id）, s_field_id （子对象汇总字段id）, s_type （汇总类型）, min_length （小数位长度）
optional: query_filter
rule: min_length >=0, min_length <= 16, s_type in ('count','max','min','sum')

type: 自动编号 auto
remark: 插入数据后自动生成，前端不可修改，插入数据失败后会跳过特定编号
required: gen_rule （编号规则）
optional:
rule: auto_gen_rule SIMILAR TO '%\{0+\}%'

type: 图片 picture
remark: 上传格式：[{"file":"filename","size":24}]，filename为上传文件后服务器返回的文件名，size为文件大小，单位为kb，
  其中filename不能包含以下字符 / \ : * ? " < > |,
  文件上传后返回的filename格式为： 随机字符串-原始文件名
  表单提交成功后filename格式变为： 当前对象id-当前数据id-原始文件名
required: min_length （图片最大数量）, max_length （图片最大大小，单位：kb）
optional:
rule: min_length >= 1, max_length > 0

type: 文件 files
remark: 格式同picture字段相同
required: min_length （文件最大数量）, max_length （文件最大大小，单位：kb）
optional:
rule: min_length >= 1, max_length > 0

"""
)
data class BizObjectField(


    @ApiModelProperty(value = "所属业务对象id ")
    var bizObjectId: String,
    @ApiModelProperty(value = "名称，用于页面显示")
    var name: String,
    @ApiModelProperty(value = "api名称，用于触发器和自定义接口")
    var api: String,
    @ApiModelProperty(
        value = "字段类型 ;;optional_value:[text(文本),check_box(复选框),check_box_group(复选框组),select(下拉菜单),integer(整型),float(浮点型),money(货币),date(日期),datetime(日期时间),master_slave(主详),reference(关联关系),mreference(多选关联关系),areference(任意关联),summary(汇总),auto(自动编号),picture(图片),files(文件)];"
    )
    var type: String,
    @ApiModelProperty(value = "字段启用状态，false表示停用，字段停用时从视图中移除，删除时清空没一行中对应字段数据")
    var active: Boolean? = null,
    @ApiModelProperty(value = "是否启用历史记录")
    var history: Boolean? = null,
    @ApiModelProperty(value = "默认值，可选择其他表中的字段，由后端处理，新建时与页面布局一起返回给前端")
    var defaultValue: String? = null,
    @ApiModelProperty(value = "公式，js代码，由前端处理，新建和编辑时拼接成监听函数与页面布局一起返回给前端")
    var formula: String? = null,
    @ApiModelProperty(value = "文本类型最大长度/浮点型整数位长度/整型最大值/图片最大数量")
    var maxLength: Int? = null,
    @ApiModelProperty(value = "文本类型最小长度/浮点型小数位长度/整型最小值/图片最小数量")
    var minLength: Int? = null,
    @ApiModelProperty(value = "文本类型显示行数/下拉选项 （多选）前端显示行数")
    var visibleRow: Int? = null,
    @ApiModelProperty(value = "单选/多选/下拉对应的数据字典id;idx")
    var dictId: String? = null,
    @ApiModelProperty(value = "下拉字段依赖的主字段id")
    var masterFieldId: String? = null,
    @ApiModelProperty(value = "副选框和下拉多选的可选个数")
    var optionalNumber: Int? = null,
    @ApiModelProperty(value = "关联的自定义对象id")
    var refId: String? = null,
    @ApiModelProperty(value = "相关列表标签，当前对象在被关联对象的相关列表中显示的标签，为空时表示不能显示在被关联对象的相关列表中")
    var refListLabel: String? = null,
    @ApiModelProperty(
        value = "当字段为关联字段时，引用数据被删除时的动作。 cascade 级联删除当前对象数据, restrict 阻止删除被引用对象, null 无动作"
    )
    var refDeletePolicy: String? = null,
    @ApiModelProperty(
        value = "字段为汇总字段时表示对子表的过滤条件，字段为引用/主从字段时表示在创建当前对象时查找引用对象的过滤条件，sql where表达式"
    )
    var queryFilter: String? = null,
    @ApiModelProperty(value = "页面中填入值时可以选择的对象的过滤列表，多个id间以英文逗号分隔，为空时可以选择所有有查看权限的对象，不为空时可以选择列表中所有有查看权限的对象")
    var filterList: String? = null,
    @ApiModelProperty(value = "汇总对象id")
    var sId: String? = null,
    @ApiModelProperty(value = "汇总字段id")
    var sFieldId: String? = null,
    @ApiModelProperty(value = "汇总类型 ;;optional_value:[sum(求和),count(总数),min(最小值),max(最大值)]")
    var sType: String? = null,
    @ApiModelProperty(
        value = "编号规则，{000} 递增序列，必填，实际序号大小小于0的个数时将会在前面补0 ; {yyyy}/{yy} 年; {mm} 月; {dd} 日"
    )
    var genRule: String? = null,
    @ApiModelProperty(value = "备注")
    var remark: String? = null,
    @ApiModelProperty(value = "说明，显示在页面上的帮助信息")
    var help: String? = null,
    @ApiModelProperty(
        value = "多选字段中间表视图名，中间表名为视图名加上前缀 core_ ，表结构为（s_id,t_id)，s_id 为当前数据id， t_id为关联数据id"
    )
    var joinViewName: String? = null,
    @ApiModelProperty(
        value = "标准类型 自定义字段不能设置该值，用于处理模块对象和标准对象的特殊字段的类型 ;; optional_value:[create_by_id(创建人id), create_by(创建人), modify_by_id(修改人id), modify_by(修改人), create_date(创建时间), modify_date(修改时间), org_id(组织id), lock_state(锁定状态), name(名称), type_id(业务类型), owner_id(所有人)]"
    )
    var standardType: String? = null,
    @ApiModelProperty(
        value = "是否是预定义字段，区分对象中的自定义字段与预定义字段，预定义字段该值为true且source_column与api相等，后台对象管理界面中不能删除和修改"
    )
    var predefined: Boolean,
    @ApiModelProperty(value = "字段对应的实际表中的列名,对象为远程对象时该字段填充空字符串")
    var sourceColumn: String? = null
) : AbstractEntity() {

    lateinit var createById: String
    lateinit var createBy: String
    lateinit var modifyById: String
    lateinit var modifyBy: String
    lateinit var createDate: LocalDateTime
    lateinit var modifyDate: LocalDateTime

}
