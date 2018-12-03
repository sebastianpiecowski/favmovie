package exception

import java.lang.Exception

class NotFoundException(id: String): Exception("Resource with $id not found")