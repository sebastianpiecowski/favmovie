package favmovie.gateway.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import exception.ServiceCallException
import favmovie.gateway.exception.AuthException
import favmovie.gateway.exception.ErrorCode
import favmovie.gateway.model.ErrorModel
import javassist.NotFoundException
import model.ApiErrorModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import util.LoggingUtil
import javax.servlet.http.HttpServletResponse

fun okhttp3.ResponseBody.deserialize(): ApiErrorModel {
    return jacksonObjectMapper().readValue(this.string(), ApiErrorModel::class.java)
}

@ControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggingUtil.getLogger(javaClass)

    @ExceptionHandler(AuthException::class)
    @ResponseBody
    fun handleAuthException(ex: AuthException, response: HttpServletResponse): ApiErrorModel {
        logger.error(ex.message)
        response.status = ex.errorModel.status ?: HttpStatus.FORBIDDEN.value()
        return ex.errorModel
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleBasicException(ex: Exception): ApiErrorModel {
        logger.error(ex.message ?: "Coś poszło nie tak!")
        return ErrorModel(ErrorCode.GENERIC, message = ex.message).toApiError(HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalStateException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun editPageExceptionHandler(exception: IllegalStateException): ResponseEntity<String> {
        logger.error("IllegalStateException:: " + exception.message)
        return ResponseEntity(exception.message, HttpStatus.BAD_REQUEST)
    }


    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleNotFoundException(ex: NotFoundException): ApiErrorModel {
        logger.error(ex.message)
        return ApiErrorModel(status = HttpStatus.NOT_FOUND.value(), message = ex.message)
    }

    @ExceptionHandler(ServiceCallException::class)
    @ResponseBody
    fun onServiceException(exception: ServiceCallException, response: HttpServletResponse): ApiErrorModel {
        logger.error(exception.error.message)
        response.status = exception.error.status ?: HttpStatus.INTERNAL_SERVER_ERROR.value()
        return exception.error
    }

}