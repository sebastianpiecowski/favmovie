package favmovie.gateway.model

import com.fasterxml.jackson.annotation.JsonValue
import config.LOGGING_ID_HEADER_NAME
import favmovie.gateway.exception.ErrorCode
import model.ApiErrorModel
import org.slf4j.MDC
import org.springframework.http.HttpStatus

data class ErrorModel(val errorCode: ErrorCode,
                      val traceId: String = MDC.get(LOGGING_ID_HEADER_NAME),
                      val params: List<String> = listOf(),
                      val message: String? = null) {

    @JsonValue
    fun toJson(): Map<String, String> = mapOf(
            Pair("status", errorCode.name),
            Pair("message", message?: if (params.isEmpty()) errorCode.msg else errorCode.msg.format(*params.toTypedArray())),
            Pair("traceId", traceId))

    fun toApiError(status: HttpStatus): ApiErrorModel {
        val map = toJson()
        return ApiErrorModel(status = status.value(), message = map["message"], traceId = map["traceId"])
    }

}