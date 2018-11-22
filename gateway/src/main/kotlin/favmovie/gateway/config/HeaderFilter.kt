package favmovie.gateway.config

import config.LOGGING_AUTH_HEADER_NAME
import config.LOGGING_ID_HEADER_NAME
import favmovie.gateway.RequestIdGenerator
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.http.HttpServletRequest

@Component
class HeaderFilter @Autowired constructor(private val idGenerator: RequestIdGenerator) : Filter {
    override fun destroy() {
    }

    override fun init(filterConfig: FilterConfig?) {
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpServletRequest = request as? HttpServletRequest
        val authToken = httpServletRequest?.getHeader(LOGGING_AUTH_HEADER_NAME).orEmpty().replace("Bearer", "").trim()
        MDC.put(LOGGING_ID_HEADER_NAME, idGenerator.requestId)
        MDC.put(LOGGING_AUTH_HEADER_NAME, authToken)
        chain?.doFilter(request, response)
    }

}
