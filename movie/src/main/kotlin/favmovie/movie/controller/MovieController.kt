package favmovie.movie.controller

import exception.NotFoundException
import favmovie.movie.model.Movie
import favmovie.movie.model.MovieFiltered
import favmovie.movie.model.genre.Genre
import favmovie.movie.repository.GenreDictRepository
import favmovie.movie.repository.MovieRepository
import model.api.recommender.ApiSimpleRatingModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.web.bind.annotation.*
import util.unwrap

@RestController
@RequestMapping("/")
class MovieController @Autowired constructor(
        val movieRepository: MovieRepository,
        val mongoTemplate: MongoTemplate,
        val genreDictRepository: GenreDictRepository
) {

    @GetMapping("movies")
    fun getMovieById(@RequestParam("ids") ids: List<String>
    ): List<Movie> {
        return ids.map { movieRepository.findById(it).unwrap() ?: throw NotFoundException(it)}
    }

    @GetMapping("movies/filter")
    fun getMovie(@RequestParam("years") years: List<String>?,
                 @RequestParam("genres") genres: List<Int>?,
                 @RequestParam("popularity") popularity: Boolean?,
                 @RequestParam("pageNumber") pageNumber: Int,
                 @RequestParam("pageSize") pageSize: Int)
            : MovieFiltered {
        return getFilteredMovies(years, genres, popularity, pageNumber, pageSize)
    }

    private fun getFilteredMovies(years: List<String>?,
                                  genres: List<Int>?,
                                  popular: Boolean?,
                                  pageNumber: Int,
                                  pageSize: Int
    ): MovieFiltered {
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
        val criteria = Criteria().andOperator(
                yearCriteria,
                genreCriteria,
                popularCriteria
        )
        val movies =  mongoTemplate.find(
                Query(criteria).skip((pageNumber*pageSize).toLong())
                        .limit(pageSize),
                Movie::class.java
        )
        val count = mongoTemplate.count(
                Query(criteria),
                Movie::class.java
        )
        return if(popular==true) {
            MovieFiltered(movies.toList().sortedByDescending { it.popularity }, count)
        }
        else MovieFiltered(movies.toList(), count)
    }

    @GetMapping("movies/search")
    fun getMovieByTitle(@RequestParam("title") title: String,
                        @RequestParam("pageNumber") pageNumber: Int,
                        @RequestParam("pageSize") pageSize: Int
    ): MovieFiltered {
        val criteria = Criteria.where("title").regex(title, "i")
        val skip = pageNumber*pageSize
        val movies = mongoTemplate.find(
                Query(criteria)
                        .skip((skip).toLong()).limit(pageSize),
                Movie::class.java
        )
        val count = mongoTemplate.count(Query(criteria), Movie::class.java)
        return MovieFiltered(movies.toList(), count)
    }

    @GetMapping("movies/genres")
    fun getGenres(): List<Genre> {
        return genreDictRepository.findAll()
    }

    @PostMapping("movies/bestRating")
    fun getMovieByRating(@RequestBody ratings: List<ApiSimpleRatingModel>,
                         @RequestParam("pageNumber") pageNumber: Int,
                         @RequestParam("pageSize") pageSize: Int
    ): MovieFiltered? {
        if(ratings.size>=pageNumber*pageSize+pageSize) {
            val response = movieRepository.findAllById(ratings.map { it.itemId }.subList(pageNumber * pageSize, pageNumber * pageSize + pageSize))
            return MovieFiltered(response.toList(), ratings.size.toLong())
        } else {
            val response = movieRepository.findAllById(ratings.map { it.itemId }.takeLast(pageSize))
            return MovieFiltered(response.toList(), ratings.size.toLong())
        }
    }

}