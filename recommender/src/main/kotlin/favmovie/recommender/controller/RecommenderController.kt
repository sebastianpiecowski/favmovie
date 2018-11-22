package favmovie.recommender.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class RecommenderController {

    @GetMapping("recommendation")
    fun getRecommendation(): String {
        val test = "TASDA"
        return "Recomendation"
    }
}