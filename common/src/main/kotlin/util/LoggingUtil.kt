package util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import model.log.Logged
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

class LoggingUtil private constructor(name: String) {

    private val logger = LoggerFactory.getLogger(name)

    companion object {
        fun getLogger(logger: Class<*>): LoggingUtil {
            return LoggingUtil(logger.canonicalName)
        }
    }

    private val classKey = "class"
    private val methodKey = "method"

    fun info(msg: String?, item: Any? = null) {
        try {
            val log = getInfo()
            val parsedJson = toJson(Logged(msg, item, log[classKey], log[methodKey], Level.INFO))
            logger.info(parsedJson)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun error(msg: String?, item: Any? = null) {
        try {
            val log = getInfo()
            val parsedJson = toJson(Logged(msg, item, log[classKey], log[methodKey], Level.ERROR))
            logger.error(parsedJson)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun toJson(logged: Logged): String {
        return "${logged.logLvl}|||${logged.dateTime}|||${logged.className}|||${logged.methodName}|||" +
                "${logged.requestId}|||${logged.message}|||${jacksonObjectMapper().writeValueAsString(logged.item)}"
    }

    private fun getInfo(): Map<String, String> {
        val stackTrace = Exception().stackTrace.first { it.isNativeMethod &&
            it.className.endsWith(javaClass.simpleName, true)}
        val className = stackTrace.className
        val methodName = "${stackTrace.methodName} : ${stackTrace.lineNumber}"
        return mapOf(
                Pair(classKey, className),
                Pair(methodKey, methodName)
        )
    }
}