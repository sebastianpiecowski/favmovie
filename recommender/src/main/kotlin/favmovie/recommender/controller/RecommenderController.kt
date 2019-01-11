package favmovie.recommender.controller

import favmovie.recommender.model.Rating
import favmovie.recommender.service.RecombeeService
import model.api.recommender.ApiSimpleRatingModel
import model.command.recommendation.SetRatingInternalCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationOptions
import org.springframework.data.mongodb.core.query.Collation
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class RecommenderController @Autowired constructor(
        val recommbeeService: RecombeeService,
        val mongoTemplate: MongoTemplate
) {

    @GetMapping("recommendation")
    fun getRecommendation(
            @RequestParam("userId") userId: String,
            @RequestParam("count", defaultValue = "20") count: Long
    ): List<String> {
        return recommbeeService.getRecommendations(userId, count)
    }

    @PostMapping("recommendation")
    fun setRating(
            @RequestBody setRatingInternalCommand: SetRatingInternalCommand
    ): String {
        return recommbeeService.setRating(setRatingInternalCommand)
    }

    @DeleteMapping("recommendation/rating/user={userId}/movie={movieId}")
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

    @GetMapping("ratings")
    fun getRatings(): List<ApiSimpleRatingModel> {
        val options = AggregationOptions.Builder().collation(Collation.of(Collation.CollationLocale.of("pl")).caseLevel(false)).build()
        var result: MutableMap<String, Double> = mutableMapOf()
        mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.project("itemId", "rating")
        ).withOptions(options),
                Rating::class.java,
                ApiSimpleRatingModel::class.java
        ).forEach {
            if (result.containsKey(it.itemId)) {
                result[it.itemId] = (result[it.itemId]!!.plus((it.rating)).div(2))
            } else {
                result[it.itemId] = it.rating
            }
        }
        return result.map { ApiSimpleRatingModel(it.key, it.value) }.sortedByDescending { it.rating }
    }
}