package favmovie.gateway.exception

import model.ApiErrorModel

class AuthException(val errorModel: ApiErrorModel): Exception()