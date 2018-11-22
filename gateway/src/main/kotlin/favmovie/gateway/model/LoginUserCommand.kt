package favmovie.gateway.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

data class LoginUserCommand @JsonCreator constructor(
        @field:JsonProperty(value = "email") val email: String,
        @field:JsonProperty(value = "password") val password: String
)