package favmovie.gateway.controller

import favmovie.gateway.AuthService
import favmovie.gateway.exception.AuthException
import favmovie.gateway.model.LoginUserCommand
import io.swagger.annotations.ApiParam
import model.CreatedIdModel
import model.api.user.ApiUserModel
import model.command.user.CreateUserCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import util.LoggingUtil
import util.deserialize
import util.validate
import javax.validation.Valid


@RestController
@RequestMapping("/command/auth")
class AuthController @Autowired constructor(
        val authService: AuthService
){

    private val logger = LoggingUtil.getLogger(javaClass)

    @GetMapping
    fun getLoggedUser(@RequestHeader(value = "Authorization") authToken: String
    ): ResponseEntity<ApiUserModel> {
        val response = authService.getLoggedUser(authToken).execute().validate()
        return ResponseEntity.ok().body(response)
    }

    @PostMapping("/users")
    fun createUser(
            @ApiParam("Dane użytkownika", required = true)
            @Valid @RequestBody request: CreateUserCommand
    ): ResponseEntity<CreatedIdModel> {
        val response = authService.createUser(request).execute().validate()
        return ResponseEntity.ok().body(response)
    }

    @PostMapping("login")
    fun login(
            @RequestBody loginCommand: LoginUserCommand
    ): ResponseEntity<Unit> {
        val response = authService.login(loginCommand).execute()
        val body = response.body()
        val authorization = response.headers().get("Authorization")
        val accessControl = response.headers().get("Access-Control-Expose-Headers")
        logger.info("Login finished with $authorization")
        if (response.isSuccessful && body != null && authorization != null) {
            val responseHeaders = HttpHeaders()
            responseHeaders.add("Authorization", authorization)
            responseHeaders.add("Access-Control-Expose-Headers", accessControl)
            return ResponseEntity.ok().headers(responseHeaders).body(body)
        }
        logger.error("Failed to login user with supplied credentials : ${loginCommand.email}")
        throw AuthException(response.errorBody()!!.deserialize())
    }
}