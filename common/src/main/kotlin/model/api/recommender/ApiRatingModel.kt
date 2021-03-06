package model.api.recommender

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ApiRatingModel @JsonCreator constructor(
        @JsonProperty("id")
        val id: String,
        @JsonProperty("userId")
        val userId: String,
        @JsonProperty("itemId")
        val itemId: String,
        @JsonProperty("timestamp")
        val timestamp: Long,
        @JsonProperty("rating")
        val rating: Double
)