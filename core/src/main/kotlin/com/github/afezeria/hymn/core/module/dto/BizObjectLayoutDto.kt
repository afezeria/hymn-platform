package com.github.afezeria.hymn.core.module.dto

import com.github.afezeria.hymn.common.util.toJson
import com.github.afezeria.hymn.core.module.entity.BizObjectLayout
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
    var components: Components,
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
    class Components(
        @ApiModelProperty("页面布局中使用到的对象的列表")
        var objects: MutableSet<ObjectInfo>,
        @ApiModelProperty("页面中使用到的按钮的id列表")
        var buttons: MutableSet<String>,
    )

    class ObjectInfo(
        @ApiModelProperty("对象id")
        val id: String,
        @ApiModelProperty("字段列表", notes = "key为字段id")
        var fields: MutableSet<FieldInfo>,
        @ApiModelProperty("类型")
        var types: MutableSet<String>,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ObjectInfo

            if (id != other.id) return false

            return true
        }

        override fun hashCode(): Int {
            return id.hashCode()
        }
    }

    class FieldInfo(
        @ApiModelProperty("字段id")
        val id: String,
        @ApiModelProperty("字段带入带出规则或计算表达式")
        val expression: String = "",
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as FieldInfo

            if (id != other.id) return false

            return true
        }

        override fun hashCode(): Int {
            return id.hashCode()
        }
    }


    companion object {
        fun defaultLayout(bizObjectId: String): BizObjectLayoutDto {
            return BizObjectLayoutDto(
                bizObjectId = bizObjectId,
                name = "默认布局",
                components = Components(mutableSetOf(), mutableSetOf()),
                pcReadLayoutJson = "",
                pcEditLayoutJson = "",
                mobileReadLayoutJson = "",
                mobileEditLayoutJson = "",
                previewLayoutJson = ""
            )
        }
    }

    fun toEntity(): BizObjectLayout {
        return BizObjectLayout(
            bizObjectId = bizObjectId,
            name = name,
            remark = remark,
            componentJson = components.toJson(),
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
            it.componentJson = components.toJson()
            it.pcReadLayoutJson = pcReadLayoutJson
            it.pcEditLayoutJson = pcEditLayoutJson
            it.mobileReadLayoutJson = mobileReadLayoutJson
            it.mobileEditLayoutJson = mobileEditLayoutJson
            it.previewLayoutJson = previewLayoutJson
        }
    }
}