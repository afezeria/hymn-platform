package com.github.afezeria.hymn.common.ann

import com.github.afezeria.hymn.common.constant.AccountType

/**
 * @author afezeria
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
annotation class Function(val accountType: AccountType = AccountType.NORMAL, val name: String = "")

