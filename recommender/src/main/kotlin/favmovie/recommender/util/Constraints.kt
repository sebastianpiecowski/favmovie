package favmovie.recommender.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class Constraints {

    @Value("\${api.key}")
    val apiKey: String? = null
}