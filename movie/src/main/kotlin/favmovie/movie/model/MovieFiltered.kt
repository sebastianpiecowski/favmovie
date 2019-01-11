package favmovie.movie.model

import com.fasterxml.jackson.annotation.JsonCreator

data class MovieFiltered @JsonCreator constructor(
        val movies: List<Movie>,
        val count: Long
)