package favmovie.gateway

import favmovie.gateway.apiconfig.api.RecommenderService
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

@Configuration
class RetrofitConfig {

    @RequestScopedBean
    fun provideAuthService(@Qualifier("authApi") retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)

    @RequestScopedBean
    fun provideRecommenderService(@Qualifier("recommenderApi") retrofit: Retrofit): RecommenderService = retrofit.create(RecommenderService::class.java)

    @Bean("authApi")
    fun provideAuthRetrofit(converter: JacksonConverterFactory, @Qualifier("authClient") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(converter)
                .baseUrl("http://auth:8080/")
                .build()
    }

    @Bean("authClient")
    fun provideAuthOkHttp(interceptor: RequestLoggingInterceptor, idInterceptor: RequestIdInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(idInterceptor)
                .build()
    }

    @Bean("recommenderApi")
    fun provideRecommenderRetrofit(converter: JacksonConverterFactory, @Qualifier("authClient") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(converter)
                .baseUrl("http://recommender:8080/")
                .build()
    }



    @Bean
    fun provideJacksonConverterFactory(): JacksonConverterFactory = JacksonConverterFactory.create()

    @Bean
    fun provideScalarConverterFactory(): ScalarsConverterFactory = ScalarsConverterFactory.create()

    @Bean
    fun provideLoggingInterceptor(): RequestLoggingInterceptor = RequestLoggingInterceptor()

}