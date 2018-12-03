package favmovie.gateway.apiconfig.api

import model.api.movie.ApiMovieModel
import model.api.movie.MovieFilteredCommand
import retrofit2.Call
import retrofit2.http.*

interface MovieService {

    @GET("movies/filter")
    fun getMoviesFiltered(
            @Query("years") years: List<String>?,
            @Query("genres") genres: List<Int>?,
            @Query("popular") popular: Boolean?
    ): Call<List<ApiMovieModel>>

    @GET("movies/search")
    fun getMoviesByTitle(
            @Query("title") title: String
    ): Call<List<ApiMovieModel>>

    @GET("movies")
    fun getMoviesById(
            @Query("ids") ids: List<String>
    ): Call<List<ApiMovieModel>>
}