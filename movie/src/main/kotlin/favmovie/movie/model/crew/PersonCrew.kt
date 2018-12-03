package favmovie.movie.model.crew

import com.fasterxml.jackson.annotation.JsonCreator
import info.movito.themoviedbapi.model.people.PersonCrew

data class PersonCrew @JsonCreator constructor(
        val id: Int,
        val department: String?,
        val job: String?,
        val name: String,
        val profilePath: String?
) {
    constructor(personCrew: PersonCrew): this(
            personCrew.id,
            personCrew.department,
            personCrew.job,
            personCrew.name,
            personCrew.profilePath
    )
}