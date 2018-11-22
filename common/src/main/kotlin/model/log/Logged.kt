package model.log

import config.LOGGING_AUTH_HEADER_NAME
import config.LOGGING_ID_HEADER_NAME
import org.slf4j.MDC
import org.slf4j.event.Level
import java.time.LocalDateTime

internal data class Logged(val message: String?,
                           val item: Any?,
                           val className: String?,
                           val methodName: String?,
                           val logLvl: Level,
                           val dateTime: String = LocalDateTime.now().toString(),
                           val requestId: String? = MDC.get(LOGGING_ID_HEADER_NAME),
                           val userToken: String? = MDC.get(LOGGING_AUTH_HEADER_NAME))