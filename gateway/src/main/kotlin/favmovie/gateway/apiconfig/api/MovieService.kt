package favmovie.gateway.apiconfig.api

import model.api.movie.ApiGenresModel
import model.api.movie.ApiMovieFiltered
import model.api.movie.ApiMovieModel
import model.api.recommender.ApiSimpleRatingModel
import retrofit2.Call
import retrofit2.http.*

interface MovieService {

    @GET("movies/filter")
    fun getMoviesFiltered(
            @Query("years") years: List<String>?,
            @Query("genres") genres: List<Int>?,
            @Query("popular") popular: Boolean?,
            @Query("pageNumber") pageNumber: Int,
            @Query("pageSize") pageSize: Int
    ): Call<ApiMovieFiltered>

    @GET("movies/search")
    fun getMoviesByTitle(
            @Query("title") title: String,
            @Query("pageNumber") pageNumber: Int,
            @Query("pageSize") pageSize: Int
    ): Call<ApiMovieFiltered>

    @GET("movies")
    fun getMoviesById(
            @Query("ids") ids: List<String>
    ): Call<List<ApiMovieModel>>

    @GET("movies/genres")
    fun getGenres(): Call<List<ApiGenresModel>>

    @POST("movies/bestRating")
    fun getMoviesByRating(
            @Body request: List<ApiSimpleRatingModel>,
            @Query("pageNumber") pageNumber: Int,
            @Query("pageSize") pageSize: Int
    ): Call<ApiMovieFiltered>
}