package model.command.user

import com.fasterxml.jackson.annotation.JsonProperty

data class UserCredentials constructor(
        @JsonProperty("email") val email: String,
        @JsonProperty("password") val password: String
)