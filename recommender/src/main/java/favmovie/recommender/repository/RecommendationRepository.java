package favmovie.recommender.repository;

import favmovie.recommender.model.Recommendation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecommendationRepository extends MongoRepository<Recommendation, String> {
    Recommendation findByUserId(String id);
}
