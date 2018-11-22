package favmovie.gateway.controller

import favmovie.gateway.apiconfig.api.RecommenderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/command/recommender")
class RecommenderController @Autowired constructor(
        val recommenderService: RecommenderService
) {

    @GetMapping("recommendation")
    fun getRecommendation():ResponseEntity<String> {
        val response = recommenderService.getRecommendation().execute()
        if(response.isSuccessful) {
            return ResponseEntity.ok().body(response.body())
        }
        else return ResponseEntity.ok().body("nie dziala cos")
    }
}