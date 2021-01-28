package github.afezeria.hymn.common.web

import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.common.exception.*
import github.afezeria.hymn.common.platform.SessionService
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.io.PrintWriter
import java.io.StringWriter

/**
 * @author afezeria
 */
@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {
    companion object : KLogging()

    @Autowired
    lateinit var sessionService: SessionService

    @ExceptionHandler(PlatformException::class)
    @ResponseBody
    fun platformExceptionHandler(
        ex: PlatformException,
    ): ResponseEntity<ErrorResponse> {
        logger.warn(ex.message, ex)
        val session = sessionService.getSession()
        val resp = if (session.accountType == AccountType.ADMIN) {
            val stringWriter = StringWriter()
            ex.printStackTrace(PrintWriter(stringWriter))
            ErrorResponse(ex.code, ex.message, stringWriter.toString())
        } else {
            if (ex is BusinessException ||
                ex is DataNotFoundException ||
                ex is PermissionDeniedException ||
                ex is UnauthorizedException
            ) {
                ErrorResponse(ex.code, ex.message)
            } else {
                ErrorResponse(message = "服务器内部错误")
            }
        }
        return ResponseEntity(resp, HttpStatus.valueOf(ex.httpCode))
    }


    @ExceptionHandler(Throwable::class)
    fun handler(ex: Throwable): ResponseEntity<ErrorResponse> {
        logger.warn(ex.message, ex)
        return ResponseEntity(
            ErrorResponse(message = "服务器内部错误"),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}