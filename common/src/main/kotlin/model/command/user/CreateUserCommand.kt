package model.command.user

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import util.PASSWORD_REGEX
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern


data class CreateUserCommand(
        @JsonProperty("firstName")
        @field:NotBlank val firstName: String,
        @JsonProperty("lastName")
        @field:NotBlank val lastName: String,
        @JsonProperty("email")
        @field:NotBlank @Email val email: String,
        @JsonProperty("password")
        @field:Pattern(regexp = PASSWORD_REGEX) var password: String
)