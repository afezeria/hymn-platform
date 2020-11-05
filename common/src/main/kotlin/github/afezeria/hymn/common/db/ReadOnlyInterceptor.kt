package github.afezeria.hymn.common.db

import github.afezeria.hymn.common.util.createLogger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component


/**
 * @author afezeria
 */
@Aspect
@Component
class ReadOnlyInterceptor {
    val log = createLogger()

    companion object {
        private val readOnly: ThreadLocal<Boolean> = ThreadLocal()

        fun isReadOnly(): Boolean {
            return readOnly.get() ?: false
        }
    }

    @Around("@annotation(ann)")
    fun setRead(joinPoint: ProceedingJoinPoint, ann:ReadOnly?): Any? {
        val a=Triple(1,2,3)
        a.first
        a.second
        a.third
        val b=1 to 2
        b.second
        log.info("readonly aspect")
        return ann?.run {
                try {
                    readOnly.set(true)
                    joinPoint.proceed()
                }finally {
                    readOnly.set(null)
                }
            }?:joinPoint.proceed()
    }
}