package com.github.afezeria.hymn.core.module.service

import com.github.afezeria.hymn.core.module.dto.ButtonPermDto
import com.github.afezeria.hymn.core.module.view.ButtonPermListView

/**
 * @author afezeria
 */
interface ButtonPermService {
    /**
     * 根据角色id和对象id查找对象专用按钮的权限
     */
    fun findViewByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String
    ): List<ButtonPermListView>

    fun findViewByButtonId(buttonId: String): List<ButtonPermListView>
    fun findViewByRoleId(roleId: String): List<ButtonPermListView>
    fun save(dtoList: List<ButtonPermDto>): Int
}