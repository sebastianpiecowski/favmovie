package favmovie.movie.model.production

import com.fasterxml.jackson.annotation.JsonCreator
import info.movito.themoviedbapi.model.ProductionCountry

data class ProductionCountry @JsonCreator constructor(
        val isoCode: String,
        val name: String
) {
    constructor(productionCountry: ProductionCountry): this(
            productionCountry.isoCode,
            productionCountry.name
    )
}
