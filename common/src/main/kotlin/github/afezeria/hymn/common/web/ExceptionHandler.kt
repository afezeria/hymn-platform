package github.afezeria.hymn.common.web

import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.common.exception.BusinessException
import github.afezeria.hymn.common.exception.PermissionDeniedException
import github.afezeria.hymn.common.exception.PlatformException
import github.afezeria.hymn.common.exception.UnauthorizedException
import github.afezeria.hymn.common.platform.Session
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.io.PrintWriter
import java.io.StringWriter
import javax.servlet.ServletException
import javax.servlet.http.HttpServletResponse

/**
 * @author afezeria
 */
@ControllerAdvice
class ExceptionHandler {
    companion object : KLogging()

    @ExceptionHandler(PlatformException::class)
    fun platformExceptionHandler(
        ex: PlatformException,
        response: HttpServletResponse,
    ): ResponseEntity<ErrorResponse> {
        logger.warn(ex.message, ex)
        val session = Session.getInstance()
        val resp = if (session.accountType == AccountType.ADMIN) {
            val stringWriter = StringWriter()
            ex.printStackTrace(PrintWriter(stringWriter))
            ErrorResponse(ex.code, ex.message, stringWriter.toString())
        } else {
            if (ex is BusinessException ||
                ex is PermissionDeniedException ||
                ex is UnauthorizedException
            ) {
                ErrorResponse(ex.code, ex.message)
            } else {
                ErrorResponse(message = "内部错误")
            }
        }
        response.setHeader("ex-flag", "t")
        return ResponseEntity(resp, HttpStatus.valueOf(ex.httpCode))
    }


    @ExceptionHandler(Throwable::class)
    fun handler(ex: Throwable): ResponseEntity<ErrorResponse> {
        if (ex is ServletException) throw ex
        logger.warn(ex.message, ex)
        return ResponseEntity(
            ErrorResponse(message = "内部错误"),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}