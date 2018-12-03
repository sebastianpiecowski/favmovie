package model.command.recommendation

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class SetRatingInternalCommand @JsonCreator constructor(
        @JsonProperty("userId")
        val userId: String,
        @JsonProperty("movieId")
        val movieId: String,
        @JsonProperty("rating")
        val rating: Double
) {
        companion object {
            fun create(userId: String,
                    ratingCommand: SetRatingCommand
            ): SetRatingInternalCommand {
                    return SetRatingInternalCommand(
                            userId,
                            ratingCommand.itemId,
                            (ratingCommand.rating-3)/2
                    )
            }
        }
}