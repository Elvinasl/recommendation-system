package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@Import(DatabaseConfig.class)
@ComponentScans({
        @ComponentScan("controllers"),
        @ComponentScan("repositories"),
        @ComponentScan("services")
})
public class AppConfig {

}
