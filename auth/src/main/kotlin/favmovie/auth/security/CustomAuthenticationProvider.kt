package favmovie.auth.security

import favmovie.auth.exception.UserNotFoundException
import favmovie.auth.model.User
import favmovie.auth.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import util.LoggingUtil
import util.unwrap

@Component
class CustomAuthenticationProvider @Autowired constructor(
        private val userRepository: UserRepository,
        private val bCryptPasswordEncoder: BCryptPasswordEncoder
): AuthenticationProvider {
    private val logger = LoggingUtil.getLogger(javaClass)

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication? {
        if(authentication.credentials == null) {
            logger.error("Authentication failed - no credentials")
            throw BadCredentialsException("Bad credentials")
        }
        val email = authentication.name
        val password = authentication.credentials.toString()
        val user = userRepository.findByEmailAndDeletedDateIsNull(email).unwrap() ?: throw UserNotFoundException(email)

        return login(user, email, password)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }

    private fun login(user: User,
                      email: String,
                      password: String
    ): UsernamePasswordAuthenticationToken {
        logger.info("Login attempt $email")
        if(!bCryptPasswordEncoder.matches(password, user.password)) {
            logger.error("Authentication failed! Password not matched")
            throw BadCredentialsException("Bad credentials")
        }
        return UsernamePasswordAuthenticationToken(email, password, arrayListOf())
    }
}
