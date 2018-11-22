package favmovie.auth.controller

import favmovie.auth.exception.UserAlreadyExistException
import favmovie.auth.model.User
import model.command.user.CreateUserCommand
import favmovie.auth.repository.UserRepository
import model.CreatedIdModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import util.LoggingUtil
import javax.validation.Valid

@RestController
@RequestMapping("/")
class AuthController @Autowired constructor(
        val userRepository: UserRepository,
        val bCryptPasswordEncoder: BCryptPasswordEncoder
) {
    private val logger = LoggingUtil.getLogger(javaClass)

    @PostMapping("users")
    fun createUser(@Valid @RequestBody command: CreateUserCommand
    ): CreatedIdModel {
        if(!userRepository.existsByEmail(command.email)) {
            command.password = bCryptPasswordEncoder.encode(command.password)
            val user = userRepository.save(User(command))
            logger.info("Created ${user.email} user")
            return CreatedIdModel(user.id.toHexString())
        }
        throw UserAlreadyExistException(command.email)
    }
}