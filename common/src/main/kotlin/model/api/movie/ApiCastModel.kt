package model.api.movie

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ApiCastModel @JsonCreator constructor(
        @JsonProperty("name")
        val name: String,
        @JsonProperty("character")
        val character: String?,
        @JsonProperty("profilePath")
        val profilePath: String?
)