package favmovie.gateway

import favmovie.gateway.model.LoginUserCommand
import model.CreatedIdModel
import model.api.user.ApiUserModel
import model.command.user.CreateUserCommand
import retrofit2.Call
import retrofit2.http.*

interface AuthService {

    @GET("users")
    fun getLoggedUser(
            @Header(value = "Authorization") authToken: String
    ): Call<ApiUserModel>

    @POST("users")
    fun createUser(
            @Body createUserCommand: CreateUserCommand
    ): Call<CreatedIdModel>

    @POST("login")
    fun login(
            @Body command: LoginUserCommand
    ): Call<Unit>
}
