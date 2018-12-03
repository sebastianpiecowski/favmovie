package model.api.movie

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ApiProductionCompanyModel @JsonCreator constructor(
        @JsonProperty("name")
        val name:String
)