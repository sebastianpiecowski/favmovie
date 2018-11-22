package favmovie.gateway.config.restTemplate

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.ResourceHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.util.*

@Configuration
class Configuration {

    @Bean
    fun restTemplate(): RestTemplate {
        val converters = ArrayList<HttpMessageConverter<*>>(
                Arrays.asList(MappingJackson2HttpMessageConverter(), ResourceHttpMessageConverter()))
        return RestTemplate(converters)
    }
}