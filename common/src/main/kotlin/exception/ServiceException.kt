package exception

import java.lang.Exception

class ServiceException(name: String): Exception("Service $name not working")