package favmovie.recommender.controller

import com.recombee.api_client.exceptions.ResponseException
import favmovie.recommender.exception.RecombeeException
import model.ApiErrorModel
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(RecombeeException::class)
    @ResponseBody
    fun handleRecombeeException(ex: ResponseException, response: HttpServletResponse): ApiErrorModel {
        response.status = ex.statusCode
        return ApiErrorModel(status = ex.statusCode, message = ex.localizedMessage)
    }
}