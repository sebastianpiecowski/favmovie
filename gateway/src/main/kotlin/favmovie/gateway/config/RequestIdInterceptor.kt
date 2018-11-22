package favmovie.gateway

import config.LOGGING_ID_HEADER_NAME
import okhttp3.Interceptor
import okhttp3.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import org.springframework.web.context.WebApplicationContext

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
class RequestIdInterceptor @Autowired constructor(val requestIdGenerator: RequestIdGenerator): Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder().addHeader(LOGGING_ID_HEADER_NAME, requestIdGenerator.requestId).build()
        return chain.proceed(newRequest)
    }
}