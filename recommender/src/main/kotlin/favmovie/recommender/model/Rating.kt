package favmovie.recommender.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

data class Rating constructor(
        @Id val id: ObjectId,
        val itemId: String,
        val userId: String,
        val timestamp: Long,
        val rating: Double
)