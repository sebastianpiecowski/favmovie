package model.api.movie

import com.fasterxml.jackson.annotation.*

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiMovieModel @JsonCreator constructor(
        @JsonProperty("id")
        val id: String?,
        @JsonProperty("title")
        val title: String,
        @JsonProperty("overview")
        val overview: String?,
        @JsonProperty("releaseYear")
        val releaseYear: String?,
        @JsonProperty("genres")
        val genres: List<ApiGenresModel>,
        @JsonProperty("posterPath")
        val posterPath: String?,
        @JsonProperty("adult")
        val adult: Boolean?,
        @JsonProperty("originalTitle")
        val originalTitle: String?,
        @JsonProperty("cast")
        val cast: List<ApiCastModel>?,
        @JsonProperty("crew")
        val crew: List<ApiCrewModel>?,
        @JsonProperty("productionCompanies")
        val productionCompanies: List<ApiProductionCompanyModel>?,
        @JsonProperty("productionCountries")
        val productionCountries: List<ApiProductionCountryModel>?,
        @JsonProperty("rating")
        var rating: Double?

)