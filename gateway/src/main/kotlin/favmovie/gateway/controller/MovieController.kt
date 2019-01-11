package favmovie.gateway.controller

import favmovie.gateway.AuthService
import favmovie.gateway.apiconfig.api.MovieService
import favmovie.gateway.apiconfig.api.RecommenderService
import io.swagger.annotations.Api
import model.api.movie.ApiGenresModel
import model.api.movie.ApiMovieFiltered
import model.api.movie.ApiMovieModel
import model.api.movie.ApiRatingMovieModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import util.validate

@RestController
@RequestMapping("movie")
class MovieController @Autowired constructor(
    val movieService: MovieService,
    val recommenderService: RecommenderService,
    val authService: AuthService
){

    @GetMapping("filtered")
    fun getMoviesFiltered(
            @RequestParam("years", required = false) years: List<String>?,
            @RequestParam("genres", required = false) genres: List<Int>?,
            @RequestParam("popular", required = false) popular: Boolean ?= false,
            @RequestParam("pageNumber", defaultValue = "0") pageNumber: Int,
            @RequestParam("pageSize", defaultValue = "20") pageSize: Int
    ): ResponseEntity<ApiMovieFiltered> {
        val movies = movieService.getMoviesFiltered(years, genres, popular, pageNumber, pageSize).execute().validate()
        return ResponseEntity.ok().body(movies)
    }

    @GetMapping("")
    fun getMovieById(
            @RequestParam("ids") ids: List<String>,
            @RequestHeader("Authorization") authToken: String
    ): ResponseEntity<List<ApiMovieModel>> {
        val user = authService.getLoggedUser(authToken).execute().validate()
        val rating = recommenderService.getUserRatings(user.id).execute().validate()
        val movie = movieService.getMoviesById(ids).execute().validate()
        movie.first().rating = rating.find { it.itemId == movie.first().id }?.rating
        return ResponseEntity.ok().body(movie)
    }

    @GetMapping("search")
    fun getMovieByTitle(
            @RequestParam("title") title: String,
            @RequestParam("pageNumber", defaultValue = "0") pageNumber: Int,
            @RequestParam("pageSize", defaultValue = "20") pageSize: Int
    ): ResponseEntity<ApiMovieFiltered> {
        val movies = movieService.getMoviesByTitle(title, pageNumber, pageSize).execute().validate()
        return ResponseEntity.ok().body(movies)
    }

    @GetMapping("genres")
    fun getGenres(): ResponseEntity<List<ApiGenresModel>> {
        val genres = movieService.getGenres().execute().validate()
        return ResponseEntity.ok().body(genres)
    }

    @GetMapping("topRating")
    fun getMovieByRating(
            @RequestParam("pageNumber", defaultValue = "0") pageNumber: Int,
            @RequestParam("pageSize", defaultValue = "20") pageSize: Int
    ): ResponseEntity<List<ApiRatingMovieModel>> {
        val ratings = recommenderService.getRatings().execute().validate()
        val movies = movieService.getMoviesByRating(ratings, pageNumber, pageSize).execute().validate()
        val response = movies.movies.map { ApiRatingMovieModel(it, ratings.find { rating -> rating.itemId == it.id }!!.rating)}.sortedByDescending{ it.rating }
        return ResponseEntity.ok().body(response)
    }
}