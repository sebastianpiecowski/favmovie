package favmovie.auth.utils

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.bson.types.ObjectId

class ObjectIdToStringSerializer: JsonSerializer<ObjectId>() {
    override fun serialize(value: ObjectId?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen!!.writeObject(value!!.toHexString())
    }
}