package favmovie.recommender.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Recommendation @JsonCreator constructor(
        @JsonProperty("userId")
        val userId: String,
        @JsonProperty("movies")
        val movies: List<String>
)