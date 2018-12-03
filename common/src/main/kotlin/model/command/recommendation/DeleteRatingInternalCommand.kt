package model.command.recommendation

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class DeleteRatingInternalCommand @JsonCreator constructor(
        @JsonProperty("userId")
        val userId: String,
        @JsonProperty("movieId")
        val movieId: String
)