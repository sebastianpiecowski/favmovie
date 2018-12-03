package favmovie.movie.service

import com.recombee.api_client.RecombeeClient
import com.recombee.api_client.api_requests.ListItems
import com.recombee.api_client.api_requests.SetItemValues
import favmovie.movie.model.Movie
import favmovie.movie.repository.MovieRepository
import favmovie.movie.util.Constraints
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class RecombeeService @Autowired constructor(
        val constraints: Constraints,
        val movieRepository: MovieRepository,
        val mongoTemplate: MongoTemplate
) {

    private val recombeeClient = RecombeeClient("favmovie", constraints.apiKeyRecombee)
    var countOfSetItems = recombeeClient.send(ListItems()).count().toLong()

//    after setup project need to execute this function to set up properties of movie for recommendations
//
//    fun setItemProperties() {
//            recombeeClient.send(AddItemProperty("title", "string"))
//            recombeeClient.send(AddItemProperty("releaseYear", "timestamp"))
//            recombeeClient.send(AddItemProperty("adult", "boolean"))
//            recombeeClient.send(AddItemProperty("originalTitle", "string"))
//            recombeeClient.send(AddItemProperty("genres", "set"))
//            recombeeClient.send(AddItemProperty("originalLanguage", "string"))
//            recombeeClient.send(AddItemProperty("popularity", "double"))
//            recombeeClient.send(AddItemProperty("productionCompanies", "set"))
//            recombeeClient.send(AddItemProperty("productionCountries", "set"))
//            recombeeClient.send(AddItemProperty("cast", "set"))
//            recombeeClient.send(AddItemProperty("crew", "set"))
//    }

//    delete all records from recombeeDb
//
//    @PostConstruct
//    fun deleteDb() {
//        recombeeClient.send(ResetDatabase())
//        setItemProperties()
//    }

//    @Scheduled(fixedDelay = 300)
    fun setItemValues() {
        val movie = mongoTemplate.find(Query().skip(countOfSetItems).limit(1),
                Movie::class.java)
        if(!movie.isEmpty()) {
            val propertiesMap = Movie.getMapOfProperities(movie.first())
            try {
            val response = recombeeClient.send(SetItemValues(movie.first().id.toHexString(), propertiesMap)
                    .setCascadeCreate(true))
            } catch (ex: Exception) {
                //
            } finally {
                countOfSetItems++
            }
        }
    }
}