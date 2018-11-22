package favmovie.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.ServletComponentScan

@ServletComponentScan
@SpringBootApplication
class GatewayApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder(GatewayApplication::class.java)
            .logStartupInfo(false)
            .run(*args)
}