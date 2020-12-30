package github.afezeria.hymn.common.perm

import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType.ANONYMOUS
import github.afezeria.hymn.common.platform.PlatformService
import github.afezeria.hymn.common.util.PermissionDeniedException
import github.afezeria.hymn.common.util.UnauthorizedException
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
    private lateinit var platformService: PlatformService

    companion object : KLogging()

    @Around("@annotation(ann)")
    fun setRead(joinPoint: ProceedingJoinPoint, ann: Function): Any? {
        logger.info("permission aspect")
        val session = platformService.getSession()
        session.accountType.let {
            if (it < ann.accountType) {
                if (it == ANONYMOUS) throw UnauthorizedException()
                throw PermissionDeniedException()
            }
        }
        if (ann.name.isNotEmpty() && !platformService.hasPerm(session.roleId, ann.name)) {
            throw PermissionDeniedException()
        }
        return joinPoint.proceed()
    }
}