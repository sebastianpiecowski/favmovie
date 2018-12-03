package favmovie.movie.controller

import exception.NotFoundException
import favmovie.movie.model.Movie
import favmovie.movie.repository.MovieRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.web.bind.annotation.*
import util.unwrap
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.TemporalField

@RestController
@RequestMapping("/")
class MovieController @Autowired constructor(
        val movieRepository: MovieRepository,
        val mongoTemplate: MongoTemplate
) {

    @GetMapping("movies")
    fun getMovieById(@RequestParam("ids") ids: List<String>
    ): List<Movie> {
        return ids.map { movieRepository.findById(it).unwrap() ?: throw NotFoundException(it)}
    }

    @GetMapping("movies/filter")
    fun getMovie(@RequestParam("years") years: List<String>?,
                 @RequestParam("genres") genres: List<Int>?,
                 @RequestParam("popularity") popularity: Boolean?)
            : List<Movie> {
        return getFilteredMovies(years, genres, popularity)
    }

    private fun getFilteredMovies(years: List<String>?,
                                  genres: List<Int>?,
                                  popular: Boolean?
    ): List<Movie> {
        val yearCriteria = if(years != null) {
            Criteria.where("releaseYear").`in`(years)
        } else {
            Criteria()
        }
        val genreCriteria = if(genres != null) {
            Criteria.where("genres.id").`in`(genres)
        } else {
            Criteria()
        }
        val popularCriteria = if(popular == true) {
            Criteria.where("popularity").gte(10)
        } else {
            Criteria()
        }

        return mongoTemplate.find(
                Query(Criteria().andOperator(
                        yearCriteria,
                        genreCriteria,
                        popularCriteria
                )
                ),
                Movie::class.java
        )
    }

    @GetMapping("movies/search")
    fun getMovieByTitle(@RequestParam("title") title: String
    ): List<Movie> {
        return mongoTemplate.find(
                Query(Criteria.where("title").regex(title, "i")),
                Movie::class.java
        )
    }
}