package model.api.movie

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ApiMovieFiltered @JsonCreator constructor(
        @JsonProperty("movies")
        val movies: List<ApiMovieModel>,
        @JsonProperty("count")
        val count: Long
)