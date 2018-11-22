package favmovie.auth.security

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import favmovie.auth.exception.UserNotFoundException
import io.jsonwebtoken.JwtException
import model.ApiErrorModel
import org.springframework.http.HttpStatus
import org.springframework.web.filter.OncePerRequestFilter
import util.LoggingUtil
import java.lang.Exception
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ExceptionHandlerFilter : OncePerRequestFilter() {

    var logging = LoggingUtil.getLogger(javaClass)

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: JwtException) {
            logging.error("JWT parsing error: ${e.localizedMessage}")
            val errorModel = ApiErrorModel(status = HttpStatus.FORBIDDEN.value(), message = e.message)
            response.status = HttpStatus.FORBIDDEN.value()
            response.writer.write(convertObjectToJson(errorModel))
        } catch (e: UserNotFoundException) {
            logging.error("User not found: ${e.localizedMessage}")
            val errorModel = ApiErrorModel(status = HttpStatus.NOT_FOUND.value(), message = "User not found!!")
            response.status = HttpStatus.NOT_FOUND.value()
            response.writer.write(convertObjectToJson(errorModel))
        } catch (ex: Exception) {
            logging.error("Occurred error processing object: ${ex.localizedMessage}")
            val errorModel = ApiErrorModel(status = HttpStatus.INTERNAL_SERVER_ERROR.value(), message = "Error processing entity")
            response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
            response.writer.write(convertObjectToJson(errorModel))
        }
    }

    @Throws(JsonProcessingException::class)
    private fun convertObjectToJson(model: Any?): String? {
        if (model == null) {
            return null
        }
        val mapper = ObjectMapper()
        return mapper.writeValueAsString(model)
    }
}