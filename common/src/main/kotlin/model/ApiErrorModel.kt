package model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import config.LOGGING_ID_HEADER_NAME
import org.slf4j.MDC
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class ApiErrorModel(
        val timestamp: String = LocalDateTime.now().toString(),
        val status: Int?,
        val message: String?,
        val traceId: String? = MDC.getCopyOfContextMap()?.get(LOGGING_ID_HEADER_NAME)
)