package favmovie.gateway

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.*
import okio.Buffer
import okio.GzipSource
import util.LoggingUtil
import java.io.EOFException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * Based on [okhttp.logging.HttpLoggingInterceptor]
 */
class RequestLoggingInterceptor : Interceptor {

    private val logger = LoggingUtil.getLogger(javaClass)

    private val charsetUTF8 = Charset.forName("UTF-8")

    private data class HttpHeader(val name: String?,
                                  val value: String?)

    private data class HttpLogData(val url: String,
                                   val method: String,
                                   var status: Int? = null,
                                   var duration: Long = 0,
                                   var requestBody: Any? = null,
                                   var responseBody: Any? = null,
                                   val requestHeaders: MutableList<HttpHeader> = mutableListOf(),
                                   val responseHeaders: MutableList<HttpHeader> = mutableListOf())

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val logData = HttpLogData(request.url().toString(), request.method())
        var message: String? = null
        try {
            logRequest(request, logData)

            val startNanos = System.nanoTime()
            val response: Response
            try {
                response = chain.proceed(request)!!
            } catch (ex: Exception) {
                logData.status = 500
                message = "HTTP FAILED: ${ex.localizedMessage}"
                throw ex
            }
            logData.duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos)
            logResponse(response, logData)

            return response
        } finally {
            logger.info(message, logData)
        }
    }

    private fun logResponse(response: Response, logData: HttpLogData) {

        logData.status = response.code()

        val responseHeaders = response.headers()
        logData.responseHeaders.addAll(responseHeaders.names().map { HttpHeader(it, responseHeaders.get(it)) })
        response.body()?.let {
            logResponseBody(it, response, logData)
        }
    }

    private fun logRequest(request: Request, logData: HttpLogData) {
        val requestHeaders = request.headers()
        val requestBody = request.body()
        requestBody?.let {
            // Request body headers are only present when installed as a network interceptor. Force
            // them to be included (when available) so there values are known.
            if (requestBody.contentType() != null) {
                logData.requestHeaders.add(HttpHeader("Content-Type", requestBody.contentType().toString()))
            }
            if (requestBody.contentLength() != -1L) {
                logData.requestHeaders.add(HttpHeader("Content-Length", requestBody.contentLength().toString()))
            }
            logBody(requestBody, request, logData)
        }
        logData.requestHeaders.addAll(requestHeaders.names()
                .filterNotNull()
                .filterNot { "Content-Type".equals(it, true) || "Content-Length".equals(it, true) }
                .map { HttpHeader(it, requestHeaders.get(it)) })
    }

    private fun logResponseBody(responseBody: ResponseBody, response: Response, logData: HttpLogData) {
        val source = responseBody.source()
        val contentLength = responseBody.contentLength()
        val headers = response.headers()
        source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
        var buffer = source.buffer()

        if ("gzip".equals(headers.get("Content-Encoding"), ignoreCase = true)) {
            var gzippedResponseBody: GzipSource? = null
            try {
                gzippedResponseBody = GzipSource(buffer.clone())
                buffer = Buffer()
                buffer.writeAll(gzippedResponseBody)
            } finally {
                gzippedResponseBody?.close()
            }
        }

        var charset: Charset = charsetUTF8
        val contentType = responseBody.contentType()
        if (contentType != null) {
            charset = contentType.charset(charsetUTF8)!!
        }

        if (!isPlaintext(buffer)) {
            logData.responseBody = "{\"type\":\"binary\",\"content-length\":${buffer.size()}}"
            return
        }
        if (contentLength != 0L) {
            logData.responseBody = buffer.clone().readString(charset)
            if (contentType?.subtype().toString().equals("json")) {
//                logData.responseBody = jacksonObjectMapper().readValue<Map<String, Any>>(logData.responseBody as String, object : TypeReference<Map<String, Any>>() {})
                logData.responseBody = jacksonObjectMapper().readTree(logData.responseBody as String)
            }
        }
    }

    private fun logBody(requestBody: RequestBody, request: Request, logData: HttpLogData) {
        if (bodyHasUnknownEncoding(request.headers())) {
            logData.requestBody = null
        } else {
            val buffer = Buffer()
            requestBody.writeTo(buffer)

            var charset: Charset = charsetUTF8
            val contentType = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(charsetUTF8)!!
            }

            if (isPlaintext(buffer)) {
                logData.requestBody = buffer.readString(charset)
                if (contentType?.subtype().toString().equals("json")) {
//                    logData.requestBody = jacksonObjectMapper().readValue<Map<String, Any>>(logData.requestBody as String, object : TypeReference<Map<String, Any>>() {})
                    logData.requestBody = jacksonObjectMapper().readTree(logData.requestBody as String)
                } else {
                    logData.requestBody = "{\"type\":\"binary\",\"content-length\":${requestBody.contentLength()}}"
                }
            }

        }
    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers.get("Content-Encoding")
        return (contentEncoding != null
                && !contentEncoding.equals("identity", ignoreCase = true)
                && !contentEncoding.equals("gzip", ignoreCase = true))
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private fun isPlaintext(buffer: Buffer): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = if (buffer.size() < 64) buffer.size() else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (e: EOFException) {
            return false // Truncated UTF-8 sequence.
        }

    }


}