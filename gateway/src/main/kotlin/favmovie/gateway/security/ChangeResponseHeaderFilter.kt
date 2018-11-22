package favmovie.gateway

import java.io.IOException
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebFilter(urlPatterns = ["/command/auth/users/*"])
class ChangeResponseHeaderFilter : Filter {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse,
                          chain: FilterChain) {
        val httpServletResponse = response as HttpServletResponse
        val httpServletRequest = request as HttpServletRequest
        try {
            val authorization = httpServletRequest.getHeader("Authorization")
            httpServletResponse.setHeader(
                    "Authorization", authorization)
            httpServletResponse.setHeader(
                    "Access-Control-Expose-Headers", "Authorization")
            chain.doFilter(httpServletRequest, httpServletResponse)
        } catch (e: Exception) {
            setUnauthorizedResponse(httpServletResponse)
        }
    }

    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {
        // ...
    }

    override fun destroy() {
        // ...
    }


    fun setUnauthorizedResponse(response: HttpServletResponse) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
    }
}