package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScans({
        @ComponentScan("controllers"),
        @ComponentScan("repositories"),
        @ComponentScan("services"),
        @ComponentScan("config")
})
public class AppConfig {

}
