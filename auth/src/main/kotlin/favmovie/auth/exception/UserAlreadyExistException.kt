package favmovie.auth.exception

class UserAlreadyExistException(email: String): Exception("User with $email already exists")