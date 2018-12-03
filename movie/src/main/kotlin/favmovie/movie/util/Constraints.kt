package favmovie.movie.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class Constraints {
    @Value("\${api.key.tmdb}")
    val apiKeyTmdb: String? = null

    @Value("\${base.url}")
    val baseUrl: String? = null

    @Value("\${api.key.recombee}")
    val apiKeyRecombee: String? = null
}