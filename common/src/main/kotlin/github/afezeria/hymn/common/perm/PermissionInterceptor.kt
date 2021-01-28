package github.afezeria.hymn.common.perm

import github.afezeria.hymn.common.ann.Function
import github.afezeria.hymn.common.constant.AccountType.ANONYMOUS
import github.afezeria.hymn.common.platform.PermService
import github.afezeria.hymn.common.platform.SessionService
import github.afezeria.hymn.common.exception.PermissionDeniedException
import github.afezeria.hymn.common.exception.UnauthorizedException
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
    private lateinit var sessionService: SessionService

    @Autowired
    private lateinit var permService: PermService

    companion object : KLogging()

    @Around("@annotation(ann)")
    fun setRead(joinPoint: ProceedingJoinPoint, ann: Function): Any? {
        logger.info("permission aspect")
        val session = sessionService.getSession()
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