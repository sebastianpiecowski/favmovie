package model.api.movie

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ApiCrewModel @JsonCreator constructor(
        @JsonProperty("department")
        val department: String?,
        @JsonProperty("job")
        val job: String?,
        @JsonProperty("name")
        val name: String,
        @JsonProperty("profilePath")
        val profilePath: String?
)