package favmovie.movie.model.producer

import com.fasterxml.jackson.annotation.JsonCreator
import info.movito.themoviedbapi.model.ProductionCompany

data class ProductionCompany @JsonCreator constructor(
        val name: String,
        val id: Int
) {
    constructor(productionCompany: ProductionCompany): this(
            productionCompany.name,
            productionCompany.id
    )
}