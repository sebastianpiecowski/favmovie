package favmovie.recommender.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

data class Recommendation @JsonCreator constructor(
        @JsonProperty("id")
        @Id val id: ObjectId,
        @JsonProperty("userId")
        val userId: String,
        @JsonProperty("movies")
        var movies: List<String>
)