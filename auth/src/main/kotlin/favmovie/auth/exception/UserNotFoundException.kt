package favmovie.auth.exception

class UserNotFoundException(msg: String): Exception("User with $msg not found")