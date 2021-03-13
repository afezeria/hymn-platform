package com.github.afezeria.hymn.common.perm

import com.github.afezeria.hymn.common.ann.Function
import com.github.afezeria.hymn.common.constant.AccountType.ANONYMOUS
import com.github.afezeria.hymn.common.exception.PermissionDeniedException
import com.github.afezeria.hymn.common.exception.UnauthorizedException
import com.github.afezeria.hymn.common.platform.PermService
import com.github.afezeria.hymn.common.platform.Session
import mu.KLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Aspect
@Component
class PermissionInterceptor {

    @Autowired
    private lateinit var permService: PermService

    companion object : KLogging()

    @Around("@annotation(ann)")
    fun setRead(joinPoint: ProceedingJoinPoint, ann: Function): Any? {
        logger.info("permission aspect")
        val session = Session.getInstance()
        session.accountType.let {
            if (it < ann.accountType) {
                if (it == ANONYMOUS) throw UnauthorizedException()
                throw PermissionDeniedException()
            }
        }
        if (ann.name.isNotEmpty() && !permService.hasFunctionPerm(session.roleId, ann.name)) {
            throw PermissionDeniedException()
        }
        return joinPoint.proceed()
    }
}