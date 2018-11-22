package favmovie.gateway.apiconfig.api

import retrofit2.Call
import retrofit2.http.GET

interface RecommenderService {

    @GET("recommendation")
    fun getRecommendation() : Call<String>
}