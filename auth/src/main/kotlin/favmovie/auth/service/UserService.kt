package favmovie.auth.service

import exception.NotFoundException
import favmovie.auth.model.User
import favmovie.auth.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import util.unwrap

@Service
class UserService @Autowired constructor(
        val userRepository: UserRepository
){
    fun getLoggedUser(): User {
        val email = SecurityContextHolder.getContext().authentication.principal.toString()
        val user = userRepository.findByEmailAndDeletedDateIsNull(email).unwrap() ?: throw NotFoundException(email)
        return user
    }
}