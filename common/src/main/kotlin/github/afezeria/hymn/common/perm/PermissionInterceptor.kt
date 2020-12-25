package github.afezeria.hymn.common.perm

import github.afezeria.hymn.common.platform.HContext
import github.afezeria.hymn.common.util.NoAccessException
import mu.KLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Aspect
@Component
class PermissionInterceptor {
    companion object : KLogging()

    @Around("@annotation(ann)")
    fun setRead(joinPoint: ProceedingJoinPoint, ann: Permission): Any? {
        logger.info("permission aspect")
        if (HContext.currentAccountType() < ann.type) throw NoAccessException()
        return joinPoint.proceed()
    }
}