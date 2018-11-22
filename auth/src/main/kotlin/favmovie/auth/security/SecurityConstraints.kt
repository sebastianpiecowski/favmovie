package favmovie.auth.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SecurityConstraints {

    @Value("\${security.constant.secret.key}")
    var secret: String? = null

    @Value("\${security.constant.token.prefix}")
    var prefix: String? = null

    @Value("\${security.constant.header.string}")
    var headerString: String? = null
}