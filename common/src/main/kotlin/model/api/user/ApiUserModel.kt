package model.api.user

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ApiUserModel @JsonCreator constructor(
        @JsonProperty("id")
        val id: String,
        @JsonProperty("firstName")
        val firstName: String,
        @JsonProperty("lastName")
        val lastName: String,
        @JsonProperty("email")
        val email: String
)