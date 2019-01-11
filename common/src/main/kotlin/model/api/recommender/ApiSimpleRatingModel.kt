package model.api.recommender

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ApiSimpleRatingModel @JsonCreator constructor(
        @JsonProperty("itemId")
        val itemId: String,
        @JsonProperty("rating")
        val rating: Double
)