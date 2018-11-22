package favmovie.recommender

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan

@ServletComponentScan
@SpringBootApplication
class RecommenderApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder(RecommenderApplication::class.java)
            .logStartupInfo(false)
            .run(*args)
}
