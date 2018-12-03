package favmovie.movie

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class FetcherApplication

fun main(args: Array<String>) {
    runApplication<FetcherApplication>(*args)
}
