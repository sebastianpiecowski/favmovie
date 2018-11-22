package favmovie.auth.controller

import favmovie.auth.exception.UserAlreadyExistException
import model.ApiErrorModel
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import util.LoggingUtil

@ControllerAdvice
class ExceptionHandler {

    private val logger = LoggingUtil.getLogger(javaClass)

    @ExceptionHandler(UserAlreadyExistException::class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ResponseBody
    fun userAlreadyExist(exception: UserAlreadyExistException): ApiErrorModel {
        logger.error("User already exists exception")
        return ApiErrorModel(status = HttpStatus.BAD_REQUEST.value(), message = exception.message)
    }
}