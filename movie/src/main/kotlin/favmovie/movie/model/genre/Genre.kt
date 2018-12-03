package favmovie.movie.model.genre

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import info.movito.themoviedbapi.model.Genre
import org.springframework.data.annotation.Id

@JsonIgnoreProperties(ignoreUnknown = true)
data class Genre @JsonCreator constructor(
        @JsonProperty("id")
        @Id val id: Int,
        @JsonProperty("name")
        val name: String?
        ) {
        constructor(genre: Genre): this(
                genre.id,
                genre.name
        )
}

