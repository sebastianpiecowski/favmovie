package favmovie.gateway.exception

enum class ErrorCode(val msg: String) {
    GENERIC("Coś poszło nie tak"),
    INVALID_CREDENTIALS("Niepoprawne dane logowania"),
    NOT_FOUND("Nie znaleziono %s o id %s"),
    ALREADY_EXISTS("%s o id %s już istnieje"),
    FORBIDDEN("Token wygasł"),
}