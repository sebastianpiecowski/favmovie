package favmovie.movie.controller

import exception.NotFoundException
import model.ApiErrorModel
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    fun notFoundExceptionHandling(ex: NotFoundException): ApiErrorModel {
        return ApiErrorModel(status = HttpStatus.NOT_FOUND.value(), message = ex.message)
    }
}