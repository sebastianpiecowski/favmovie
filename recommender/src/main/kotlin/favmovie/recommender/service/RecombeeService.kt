package favmovie.recommender.service

import com.recombee.api_client.RecombeeClient
import com.recombee.api_client.api_requests.*
import com.recombee.api_client.exceptions.ResponseException
import favmovie.recommender.exception.RecombeeException
import favmovie.recommender.model.Rating
import favmovie.recommender.model.Recommendation
import favmovie.recommender.repository.RatingRepository
import favmovie.recommender.repository.RecommendationRepository
import favmovie.recommender.util.Constraints
import model.command.recommendation.SetRatingInternalCommand
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@Service
class RecombeeService @Autowired constructor(
        constraints: Constraints,
        val recommendationRepository: RecommendationRepository,
        val ratingRepository: RatingRepository
) {

    private val recombeeClient = RecombeeClient("favmovie", constraints.apiKey)

    private fun cacheRecommendations(userId: String, count: Long) {
        val recommendedMoviesId = recombeeClient.send(RecommendItemsToUser(userId, count)).ids.toList()
        val recommendation = recommendationRepository.findByUserId(userId) ?: Recommendation(ObjectId(), userId, recommendedMoviesId)
        recommendation.movies = recommendedMoviesId
        recommendationRepository.save(recommendation)
    }

    fun getRecommendations(userId: String, count: Long
    ): List<String> {
        return try {
            recommendationRepository.findByUserId(userId)?.movies?.shuffled()?.take(count.toInt()) ?: recombeeClient.send(RecommendItemsToUser(userId, count).setCascadeCreate(true)).ids.toList()
        } catch (ex: ResponseException) {
            throw RecombeeException(ex)
        }
    }

    fun setRating(setRatingInternalCommand: SetRatingInternalCommand): String {
        val response =
        try {
            recombeeClient.send(AddRating(setRatingInternalCommand.userId,
                    setRatingInternalCommand.movieId, setRatingInternalCommand.rating)
                    .setTimestamp(Timestamp.from(Instant.now()))
                    .setCascadeCreate(true))
        } catch (ex: ResponseException) {
            throw RecombeeException(ex)
        }
        cacheRecommendations(setRatingInternalCommand.userId, 50)
        val rating = checkIfRatingExist(setRatingInternalCommand.movieId, setRatingInternalCommand.userId)
        ratingRepository.save(
                favmovie.recommender.model.Rating(
                        rating?.id ?: ObjectId(),
                        setRatingInternalCommand.movieId,
                        setRatingInternalCommand.userId,
                        Instant.now().toEpochMilli(),
                        setRatingInternalCommand.rating
                )
        )
        return response
    }

    private fun checkIfRatingExist(movieId: String, userId: String): Rating? {
        val rating = ratingRepository.findByItemIdAndUserId(movieId, userId)
        if(rating != null) {
            deleteRating(movieId, userId)
        }
        return rating
    }

    fun getUserRatings(userId: String): List<Rating>{
        return ratingRepository.findByUserId(userId)
    }

    fun deleteRating(movieId: String, userId: String): String {
        val response = try {
            recombeeClient.send(DeleteRating(userId, movieId))
        } catch (ex: ResponseException) {
            throw RecombeeException(ex)
        }
        ratingRepository.deleteByItemIdAndUserId(movieId, userId)
        cacheRecommendations(userId, 50)
        return response
    }

    fun getItemRatings(movieId: String): List<Rating> {
        return ratingRepository.findByItemId(movieId)
    }
}