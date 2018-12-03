package favmovie.movie.service

import exception.ServiceException
import favmovie.movie.model.Movie
import favmovie.movie.repository.MovieRepository
import favmovie.movie.util.Constraints
import info.movito.themoviedbapi.TmdbApi
import info.movito.themoviedbapi.TmdbMovies
import info.movito.themoviedbapi.model.MovieDb
import info.movito.themoviedbapi.tools.MovieDbException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class FetchService @Autowired constructor(
        constraints: Constraints,
        mongoTemplate: MongoTemplate,
        val movieRepository: MovieRepository
){
    private val db: TmdbMovies = try {
        TmdbApi(constraints.apiKeyTmdb).movies
    } catch (ex: MovieDbException) {
        throw ServiceException("tmdb")
    }
    private val lastFetchedMovie = mongoTemplate.find(Query().skip(movieRepository.count()-1).limit(1),
            Movie::class.java)
    private var increment = if(lastFetchedMovie.isEmpty()) {
        1
    } else {
        lastFetchedMovie.first().movieDbId + 1
    }

    @Scheduled(fixedDelay = 300)
    fun fetchMovies() {
            var movie: MovieDb? = null
            try {
                movie = db.getMovie(increment, "pl", TmdbMovies.MovieMethod.credits)
            } catch (ex: Exception) {
                System.err.println("Movie $increment not found")
            } finally {
                increment++
            }
            if (movie != null) {
                movieRepository.save(Movie.create(movie))
            }
    }




}