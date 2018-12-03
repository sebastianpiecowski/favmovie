package favmovie.gateway.controller

import favmovie.gateway.apiconfig.api.MovieService
import model.api.movie.ApiMovieModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import util.validate

@RestController
@RequestMapping("/command/movie")
class MovieController @Autowired constructor(
    val movieService: MovieService
){

    @GetMapping("filtered")
    fun getMoviesFiltered(
            @RequestParam("years", required = false) years: List<String>?,
            @RequestParam("genres", required = false) genres: List<Int>?,
            @RequestParam("popular", required = false) popular: Boolean ?= false
    ): ResponseEntity<List<ApiMovieModel>> {
        val movies = movieService.getMoviesFiltered(years, genres, popular).execute().validate()
        return ResponseEntity.ok().body(movies)
    }

    @GetMapping("")
    fun getMovieById(
            @RequestParam("ids") ids: List<String>
    ): ResponseEntity<List<ApiMovieModel>> {
        val movies = movieService.getMoviesById(ids).execute().validate()
        return ResponseEntity.ok().body(movies)
    }

    @GetMapping("search")
    fun getMovieByTitle(
            @RequestParam("title") title: String
    ): ResponseEntity<List<ApiMovieModel>> {
        val movies = movieService.getMoviesByTitle(title).execute().validate()
        return ResponseEntity.ok().body(movies)
    }
}