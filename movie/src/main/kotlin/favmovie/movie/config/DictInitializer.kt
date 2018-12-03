package favmovie.movie.config

import favmovie.movie.model.genre.Genres
import favmovie.movie.repository.GenreDictRepository
import favmovie.movie.util.Constraints
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.lang.Exception
import javax.annotation.PostConstruct

@Configuration
class DictInitializer @Autowired constructor(
        val constraints: Constraints,
        val genreDictRepository: GenreDictRepository
) {
    @PostConstruct
    fun getGenres() {
        var genres: Genres? = null

        try {
            genres = RestTemplate().getForObject(constraints.baseUrl + "genre/movie/list?api_key="
                    + constraints.apiKeyTmdb + "&language=pl", Genres::class.java)
        } catch (ex: Exception) {
            //
        }
        if(genres != null) {
            genreDictRepository.saveAll(genres.genres)
        }
    }
}