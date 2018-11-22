package favmovie.auth.config

import config.LOGGING_AUTH_HEADER_NAME
import config.LOGGING_ID_HEADER_NAME
import org.jboss.logging.MDC
import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.http.HttpServletRequest

@Component
class HeaderFilter : Filter {
    override fun destroy() {
    }

    override fun init(filterConfig: FilterConfig?) {
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpServletRequest = request as? HttpServletRequest
        val header = httpServletRequest?.getHeader(LOGGING_ID_HEADER_NAME)
        val authToken = httpServletRequest?.getHeader(LOGGING_AUTH_HEADER_NAME).orEmpty().replace("Bearer", "").trim()
        MDC.put(LOGGING_ID_HEADER_NAME, header)
        MDC.put(LOGGING_AUTH_HEADER_NAME, authToken)
        chain?.doFilter(request, response)
    }
}