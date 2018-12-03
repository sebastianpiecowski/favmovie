package favmovie.recommender.service

import com.recombee.api_client.RecombeeClient
import com.recombee.api_client.api_requests.*
import com.recombee.api_client.bindings.Rating
import com.recombee.api_client.exceptions.ResponseException
import favmovie.recommender.exception.RecombeeException
import favmovie.recommender.model.Recommendation
import favmovie.recommender.repository.RecommendationRepository
import favmovie.recommender.util.Constraints
import model.command.recommendation.SetRatingInternalCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant

@Service
class RecombeeService @Autowired constructor(
        constraints: Constraints,
        val recommendationRepository: RecommendationRepository
) {

    private val recombeeClient = RecombeeClient("favmovie", constraints.apiKey)

    private fun cacheRecommendations(userId: String, count: Long) {
        val recommendedMoviesId = recombeeClient.send(RecommendItemsToUser(userId, count)).ids.toList()
        recommendationRepository.save(Recommendation(userId, recommendedMoviesId))
    }

    fun getRecommendations(userId: String, count: Long
    ): List<String> {
        return try {
            recommendationRepository.findByUserId(userId)?.movies?.take(count.toInt()) ?: recombeeClient.send(RecommendItemsToUser(userId, count)).ids.toList()
        } catch (ex: ResponseException) {
            throw RecombeeException(ex)
        }
    }

    fun setRating(setRatingInternalCommand: SetRatingInternalCommand): String {
        return try {
            recombeeClient.send(AddRating(setRatingInternalCommand.userId,
                    setRatingInternalCommand.movieId, setRatingInternalCommand.rating)
                    .setTimestamp(Timestamp.from(Instant.now()))
                    .setCascadeCreate(true))
        } catch (ex: ResponseException) {
            throw RecombeeException(ex)
        } finally {
            cacheRecommendations(setRatingInternalCommand.userId, 100)
        }
    }

    fun getUserRatings(userId: String): List<Rating>{
        return try {
            recombeeClient.send(ListUserRatings(userId)).toList()
        } catch (ex: ResponseException) {
            throw RecombeeException(ex)
        }
    }

    fun deleteRating(movieId: String, userId: String): String {
        return try {
            recombeeClient.send(DeleteRating(userId, movieId))
        } catch (ex: ResponseException) {
            throw RecombeeException(ex)
        }
    }

    fun getItemRatings(movieId: String): List<Rating> {
        return try {
            recombeeClient.send(ListItemRatings(movieId)).toList()
        } catch (ex: ResponseException) {
            throw RecombeeException(ex)
        }
    }
}