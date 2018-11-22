package favmovie.auth.model

import model.command.user.CreateUserCommand
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import java.time.LocalDateTime

class User(
        @Id
        val id: ObjectId,
        val firstName: String,
        val lastName: String,
        val email: String,
        val password: String,
        var deletedDate: LocalDateTime?
        ) {
        constructor(command: CreateUserCommand): this(
                ObjectId(),
                command.firstName,
                command.lastName,
                command.email,
                command.password,
                null
        )
}