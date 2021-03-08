package github.afezeria.hymn.core.module.dto

import github.afezeria.hymn.core.module.entity.BizObjectLayout
import io.swagger.annotations.ApiModelProperty

/**
 * @author afezeria
 */
class BizObjectLayoutDto(
    @ApiModelProperty(value = "引用对象", required = true)
    var bizObjectId: String,
    @ApiModelProperty(value = "布局名称", required = true)
    var name: String,
    @ApiModelProperty(
        value = "引用字段的数据的列表，用于根据权限对字段进行过滤，布局json中不能直接使用字段数据，在需要字段数据的部分通过rel_field_json_arr中的json对象的_id引用，找不到的场合下忽略该字段",
        required = true
    )
    var relFieldJsonArr: String,
    @ApiModelProperty(value = "pc端查看页面页面布局", required = true)
    var pcReadLayoutJson: String,
    @ApiModelProperty(value = "pc端新建、编辑页面页面布局", required = true)
    var pcEditLayoutJson: String,
    @ApiModelProperty(value = "移动端查看页面页面布局", required = true)
    var mobileReadLayoutJson: String,
    @ApiModelProperty(value = "移动端新建、编辑页面页面布局", required = true)
    var mobileEditLayoutJson: String,
    @ApiModelProperty(value = "小窗预览界面布局", required = true)
    var previewLayoutJson: String,
    @ApiModelProperty(value = "")
    var remark: String? = null,
) {
    fun toEntity(): BizObjectLayout {
        return BizObjectLayout(
            bizObjectId = bizObjectId,
            name = name,
            remark = remark,
            relFieldJsonArr = relFieldJsonArr,
            pcReadLayoutJson = pcReadLayoutJson,
            pcEditLayoutJson = pcEditLayoutJson,
            mobileReadLayoutJson = mobileReadLayoutJson,
            mobileEditLayoutJson = mobileEditLayoutJson,
            previewLayoutJson = previewLayoutJson,
        )
    }

    fun update(entity: BizObjectLayout) {
        entity.also {
            it.bizObjectId = bizObjectId
            it.name = name
            it.remark = remark
            it.relFieldJsonArr = relFieldJsonArr
            it.pcReadLayoutJson = pcReadLayoutJson
            it.pcEditLayoutJson = pcEditLayoutJson
            it.mobileReadLayoutJson = mobileReadLayoutJson
            it.mobileEditLayoutJson = mobileEditLayoutJson
            it.previewLayoutJson = previewLayoutJson
        }
    }
}
