package favmovie.gateway.controller

import favmovie.gateway.AuthService
import favmovie.gateway.apiconfig.api.MovieService
import favmovie.gateway.apiconfig.api.RecommenderService
import model.api.movie.ApiMovieModel
import model.api.recommender.ApiRatingModel
import model.command.recommendation.SetRatingCommand
import model.command.recommendation.SetRatingInternalCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import util.validate

@RestController
@RequestMapping("/command/recommender")
class RecommenderController @Autowired constructor(
        val recommenderService: RecommenderService,
        val authService: AuthService,
        val movieService: MovieService
) {

    @GetMapping("recommendation")
    fun getRecommendation(
            @RequestHeader("Authorization") authToken: String,
            @RequestParam("count") count: Long
    ):ResponseEntity<List<ApiMovieModel>> {
        val loggedUser = authService.getLoggedUser(authToken).execute().validate()
        val recommended = recommenderService.getRecommendation(loggedUser.id, count).execute().validate()
        val movies = movieService.getMoviesById(recommended).execute().validate()
        return ResponseEntity.ok().body(movies)
    }

    @PostMapping("recommendation")
    fun setRating(
            @RequestHeader("Authorization") authToken: String,
            @RequestBody ratingInternalCommand: SetRatingCommand
    ): ResponseEntity<String> {
        val loggedUser = authService.getLoggedUser(authToken).execute().validate()
        val internalCommand = SetRatingInternalCommand.create(loggedUser.id, ratingInternalCommand)
        val response = recommenderService.postRating(internalCommand).execute().validate()
        return ResponseEntity.ok().body(response)
    }

    @DeleteMapping("recommendation")
    fun deleteRating(
            @RequestHeader("Authorization") authToken: String,
            @RequestParam("movieId") movieId: String
    ): ResponseEntity<String> {
        val loggedUser = authService.getLoggedUser(authToken).execute().validate()
        val response = recommenderService.deleteRating(movieId, loggedUser.id).execute().validate()
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("ratings/movie={movieId}")
    fun getMovieRatings(
            @RequestParam("movieId") movieId: String
    ): ResponseEntity<List<ApiRatingModel>> {
        val response = recommenderService.getItemRatings(movieId).execute().validate()
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("ratings/user")
    fun getUserRatings(
            @RequestHeader("Authorization") authToken: String
    ): ResponseEntity<List<ApiRatingModel>> {
        val loggedUser = authService.getLoggedUser(authToken).execute().validate()
        val response = recommenderService.getUserRatings(loggedUser.id).execute().validate()
        return ResponseEntity.ok().body(response)
    }
}