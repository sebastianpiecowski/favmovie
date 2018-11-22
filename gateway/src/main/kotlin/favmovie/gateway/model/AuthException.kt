package favmovie.gateway.model

import model.ApiErrorModel

class AuthException(val errorModel: ApiErrorModel): Exception()