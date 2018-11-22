package favmovie.auth.security

import favmovie.auth.exception.UserNotFoundException
import favmovie.auth.repository.UserRepository
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.context.support.WebApplicationContextUtils
import util.unwrap
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthorizationFilter(authManager: AuthenticationManager
): BasicAuthenticationFilter(authManager) {
    private lateinit var userRepository: UserRepository
    private lateinit var securityConstraints: SecurityConstraints

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(request: HttpServletRequest,
                                  response: HttpServletResponse,
                                  chain: FilterChain) {
        initializeDependencies(request)
        val header = request.getHeader(securityConstraints.headerString)
        if(header == null || !header.startsWith(securityConstraints.prefix!!)) {
            chain.doFilter(request, response)
            return
        }

        val authentication = getAuthentication(request, response)

        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }

    private fun getAuthentication(request: HttpServletRequest, response: HttpServletResponse): UsernamePasswordAuthenticationToken? {
        val token = request.getHeader(securityConstraints.headerString)
        if (token != null) {

            /* parse the token. */
            val email = Jwts.parser()
                    .setSigningKey(securityConstraints.secret)
                    .parseClaimsJws(token.replace(securityConstraints.prefix!!, ""))
                    .body
                    .issuer

            val user = userRepository.findByEmailAndDeletedDateIsNull(email).unwrap()
                    ?: throw UserNotFoundException(email)

            response.addHeader(securityConstraints.headerString, token)
            return UsernamePasswordAuthenticationToken(email, user.password, arrayListOf())
        } else {
            logger.error("Given token is empty!")
            return null
        }
    }

    /* Initialize spring components */
    private fun initializeDependencies(request: HttpServletRequest) {
        val webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(request.servletContext)
        userRepository = webApplicationContext!!.getBean(UserRepository::class.java)
        securityConstraints = webApplicationContext.getBean(SecurityConstraints::class.java)
    }
}