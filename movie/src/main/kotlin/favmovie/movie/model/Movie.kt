package favmovie.movie.model

import favmovie.movie.model.cast.PersonCast
import favmovie.movie.model.crew.PersonCrew
import favmovie.movie.model.genre.Genre
import favmovie.movie.model.producer.ProductionCompany
import favmovie.movie.model.production.ProductionCountry
import info.movito.themoviedbapi.model.MovieDb
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import java.sql.Timestamp
import java.time.LocalDate

data class Movie(
        @Id val id: ObjectId,
        val movieDbId: Int,
        val title: String,
        val originalTitle: String?,
        val popularity: Float?,
        val posterPath: String?,
        val releaseYear: String?,
        val adult: Boolean?,
        val genres: List<Genre>?,
        val overview: String?,
        val originalLanguage: String?,
        val status: String?,
        val productionCompanies: List<ProductionCompany>?,
        val productionCountries: List<ProductionCountry>?,
        val cast: List<PersonCast>?,
        val crew: List<PersonCrew>?
        ) {
    companion object {
        fun create(movie: MovieDb): Movie {
            return Movie(
                    ObjectId(),
                    movie.id,
                    movie.title,
                    movie.originalTitle,
                    movie.popularity,
                    movie.posterPath,
                    movie.releaseDate.substringBefore("-"),
                    movie.isAdult,
                    movie.genres.map { Genre(it) },
                    movie.overview,
                    movie.originalLanguage,
                    movie.status,
                    movie.productionCompanies.map { ProductionCompany(it) },
                    movie.productionCountries.map { ProductionCountry(it) },
                    movie.credits.cast.map { PersonCast(it) },
                    movie.credits.crew.map { PersonCrew(it) }
            )
        }

        fun getMapOfProperities(movie: Movie?): Map<String, Any?> {
            return mapOf(
                    "title" to movie?.title,
                    "adult" to movie?.adult,
                    "genres" to movie?.genres?.map { it.name }?.toSet(),
                    "popularity" to movie?.popularity,
                    "releaseYear" to if(!movie?.releaseYear.isNullOrBlank()) {
                        Timestamp.valueOf(LocalDate.of(movie?.releaseYear!!.toInt(), 1, 1).atStartOfDay())
                    } else {
                        null
                    },
                    "productionCompanies" to movie?.productionCompanies?.map { it.name }?.toSet(),
                    "productionCountries" to movie?.productionCountries?.map { it.isoCode }?.toSet(),
                    "cast" to movie?.cast?.map { it.name }?.toSet()?.take(8),
                    "crew" to movie?.crew?.map { it.name }?.toSet()?.take(4)
            )
        }
    }
}