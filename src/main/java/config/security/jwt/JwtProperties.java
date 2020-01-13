package config.security.jwt;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@PropertySource("classpath:security.properties")
class JwtProperties {
    static String SECRET = "";
    static final int EXPIRATION_TIME = 864_000_000; // 10 days
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";

    public JwtProperties(Environment env) {
        SECRET = env.getProperty("jwt_secret");
    }
}
