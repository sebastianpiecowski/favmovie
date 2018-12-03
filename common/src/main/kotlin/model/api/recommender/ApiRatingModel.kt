package model.api.recommender

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class ApiRatingModel @JsonCreator constructor(
        @JsonProperty("userId")
        val userId: String,
        @JsonProperty("itemId")
        val itemId: String,
        @JsonProperty("timestamp")
        val timestamp: Date,
        @JsonProperty("rating")
        val rating: Double,
        @JsonProperty("recommId")
        val recommId: String?
        )