package favmovie.auth.repository;

import favmovie.auth.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmailAndDeletedDateIsNull(String email);
    Boolean existsByEmail(String email);
}
