package favmovie.gateway.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class HealthCheckResponse @JsonCreator constructor(
        @JsonProperty("status") val status: String
)
