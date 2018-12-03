package exception

import model.ApiErrorModel

class ServiceCallException(val error: ApiErrorModel) : Exception()
