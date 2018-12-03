package model.api.movie

import com.fasterxml.jackson.annotation.JsonCreator

data class MovieFilteredCommand @JsonCreator constructor(
        val genres: List<String>,
        val years: List<String>
)