package exception

import model.ApiErrorModel

class ServiceCallException(val e: ApiErrorModel) : Exception()
