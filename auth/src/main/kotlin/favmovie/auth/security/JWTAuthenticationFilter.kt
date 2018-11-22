package favmovie.auth.security

import com.fasterxml.jackson.databind.ObjectMapper
import config.LOGGING_ID_HEADER_NAME
import exception.NotFoundException
import favmovie.auth.repository.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import model.api.user.UserCredentials
import org.slf4j.MDC
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.context.support.WebApplicationContextUtils
import util.unwrap
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthenticationFilter(authenticationManager: AuthenticationManager
): UsernamePasswordAuthenticationFilter() {
    private lateinit var securityConstants: SecurityConstraints
    private lateinit var userRepository: UserRepository

    init {
        this.authenticationManager = authenticationManager
        setRequiresAuthenticationRequestMatcher(AntPathRequestMatcher("/login", "POST"))
    }

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(request: HttpServletRequest,
                                       response: HttpServletResponse
    ): Authentication? {
        try {
            initializeDependencies(request)
            MDC.put(LOGGING_ID_HEADER_NAME, request.getHeader(LOGGING_ID_HEADER_NAME))
            val user = ObjectMapper().readValue(request.inputStream, UserCredentials::class.java)
            val authenticate = this.authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(user.email, user.password)
            )
            if(authenticate.isAuthenticated) {
                val userInfo = userRepository.findByEmailAndDeletedDateIsNull(user.email).unwrap()
                if(userInfo != null) {
                    userRepository.save(userInfo)
                }
            }
            return authenticate
        } catch (ex: IOException) {
            logger.error("Authentication failed! $ex")
            response.status = 400
        }
        return null
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(request: HttpServletRequest,
                                          response: HttpServletResponse,
                                          chain: FilterChain,
                                          authResult: Authentication) {

        val email = authResult.principal.toString()
        val user = userRepository.findByEmailAndDeletedDateIsNull(email).unwrap() ?: NotFoundException(email)

        val token = Jwts.builder()
                .setIssuer(email)
                .setSubject("$email token")
                .claim("scope ", email)
                .signWith(SignatureAlgorithm.HS512, securityConstants.secret)
                .compact()

        response.addHeader(securityConstants.headerString, securityConstants.prefix + " " + token)
        response.contentType = "application/json"
        response.addHeader("Access-Control-Expose-Headers", "Authorization")
        response.outputStream.write("{}".toByteArray())
    }

    private fun initializeDependencies(request: HttpServletRequest) {
        val webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(request.servletContext)
        userRepository = webApplicationContext!!.getBean(UserRepository::class.java)
        securityConstants = webApplicationContext.getBean(SecurityConstraints::class.java)
    }
}