package favmovie.gateway.apiconfig.api

import model.api.recommender.ApiRatingModel
import model.api.recommender.ApiSimpleRatingModel
import model.command.recommendation.SetRatingInternalCommand
import retrofit2.Call
import retrofit2.http.*

interface RecommenderService {

    @GET("recommendation")
    fun getRecommendation(
            @Query("userId") userId: String,
            @Query("count") count: Long
    ) : Call<List<String>>

    @POST("recommendation")
    fun postRating(
            @Body setRatingInternal: SetRatingInternalCommand
    ): Call<String>

    @DELETE("recommendation/rating/user={userId}/movie={movieId}")
    fun deleteRating(
            @Path("movieId") movieId: String,
            @Path("userId") userId: String
    ): Call<String>

    @GET("ratings/user={userId}")
    fun getUserRatings(
            @Path("userId") userId: String
    ): Call<List<ApiRatingModel>>

    @GET("ratings/movie={movieId}")
    fun getItemRatings(
            @Path("movieId") movieId: String
    ): Call<List<ApiRatingModel>>

    @GET("ratings")
    fun getRatings(): Call<List<ApiSimpleRatingModel>>
}