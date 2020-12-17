package github.afezeria.hymn.common.db

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
class ReadOnlyInterceptor {

    companion object : KLogging() {
        private val readOnly: ThreadLocal<Boolean> = ThreadLocal()

        fun isReadOnly(): Boolean {
            return readOnly.get() ?: false
        }
    }

    @Around("@annotation(ann)")
    fun setRead(joinPoint: ProceedingJoinPoint, ann: ReadOnly?): Any? {
        logger.info("readonly aspect")
        return ann?.run {
            try {
                readOnly.set(true)
                joinPoint.proceed()
            } finally {
                readOnly.set(null)
            }
        } ?: joinPoint.proceed()
    }
}