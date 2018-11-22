package favmovie.gateway

import favmovie.gateway.model.HealthCheckResponse
import favmovie.gateway.model.LoginUserCommand
import model.CreatedIdModel
import model.command.user.CreateUserCommand
import retrofit2.Call
import retrofit2.http.*

interface AuthService {

    @GET("")
    fun test(
            @Header(value = "Authorization") authToken: String,
            @Path("login") login: String
    ): Call<String>

    @GET("actuator/health")
    fun healthCheck() : Call<HealthCheckResponse>

    @POST("users")
    fun createUser(
            @Body createUserCommand: CreateUserCommand
    ): Call<CreatedIdModel>

    @POST("login")
    fun login(
            @Body command: LoginUserCommand
    ): Call<Unit>
}
