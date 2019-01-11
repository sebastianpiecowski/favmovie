package favmovie.recommender.repository;

import favmovie.recommender.model.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RatingRepository extends MongoRepository<Rating, String> {
    List<Rating> findByUserId(String userId);
    List<Rating> findByItemId(String itemId);
    Rating findByItemIdAndUserId(String itemId, String userId);
    Long deleteByItemIdAndUserId(String itemId, String userId);
}
