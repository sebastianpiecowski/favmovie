package model.command.recommendation

import javax.validation.constraints.NotBlank

data class SetRatingCommand constructor(
        @field:NotBlank val itemId: String,
        @field:NotBlank val rating: Double
)