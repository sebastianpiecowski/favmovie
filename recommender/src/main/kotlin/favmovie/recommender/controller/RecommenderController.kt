package favmovie.recommender.controller

import com.recombee.api_client.bindings.Rating
import favmovie.recommender.service.RecombeeService
import model.api.recommender.ApiRatingModel
import model.command.recommendation.DeleteRatingInternalCommand
import model.command.recommendation.SetRatingInternalCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class RecommenderController @Autowired constructor(
        val recommbeeService: RecombeeService
){

    @GetMapping("recommendation")
    fun getRecommendation(
            @RequestParam("userId") userId: String,
            @RequestParam("count") count: Long
    ): List<String> {
        return recommbeeService.getRecommendations(userId, count)
    }

    @PostMapping("recommendation")
    fun setRating(
            @RequestBody setRatingInternalCommand: SetRatingInternalCommand
    ): String {
        return recommbeeService.setRating(setRatingInternalCommand)
    }

    @DeleteMapping("recommendation/rating/movie={movieId}&user={userId}")
    fun deleteRating(
            @PathVariable("movieId") movieId: String,
            @PathVariable("userId") userId: String
    ): String {
        return recommbeeService.deleteRating(movieId, userId)
    }

    @GetMapping("ratings/user={userId}")
    fun getUserRatings(
            @PathVariable("userId") userId: String
    ): List<Rating> {
        return recommbeeService.getUserRatings(userId)
    }

    @GetMapping("ratings/movie={movieId}")
    fun getItemRatings(
            @PathVariable("movieId") movieId: String
    ): List<Rating> {
        return recommbeeService.getItemRatings(movieId)
    }
}