package favmovie.movie.model.genre

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Genres @JsonCreator constructor(
        @JsonProperty("genres")
        val genres: List<Genre>
)