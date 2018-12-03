package favmovie.movie.repository;

import favmovie.movie.model.genre.Genre;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GenreDictRepository extends MongoRepository<Genre, Integer> { }
