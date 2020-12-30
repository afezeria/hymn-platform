package github.afezeria.hymn.common.ann

import github.afezeria.hymn.common.constant.AccountType

/**
 * @author afezeria
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
annotation class Function(val accountType: AccountType = AccountType.NORMAL, val name: String = "")

