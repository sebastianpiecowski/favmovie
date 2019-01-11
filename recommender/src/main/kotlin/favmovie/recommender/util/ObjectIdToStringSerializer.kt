package favmovie.recommender.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.bson.types.ObjectId
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class ObjectIdToStringSerializer: JsonSerializer<ObjectId>() {

    override fun serialize(value: ObjectId?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen!!.writeObject(value!!.toHexString())
    }
}