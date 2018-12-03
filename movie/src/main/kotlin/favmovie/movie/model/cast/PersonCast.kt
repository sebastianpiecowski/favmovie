package favmovie.movie.model.cast

import com.fasterxml.jackson.annotation.JsonCreator
import info.movito.themoviedbapi.model.people.PersonCast

data class PersonCast @JsonCreator constructor(
        val id: Int,
        val character: String?,
        val name: String,
        val profilePath: String?
) {
    constructor(personCast: PersonCast): this (
            personCast.id,
            personCast.character,
            personCast.name,
            personCast.profilePath
    )
}