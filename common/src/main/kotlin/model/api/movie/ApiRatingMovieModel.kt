package model.api.movie

import com.fasterxml.jackson.annotation.JsonCreator

data class ApiRatingMovieModel @JsonCreator constructor(
        val movie: ApiMovieModel,
        val rating: Double
)