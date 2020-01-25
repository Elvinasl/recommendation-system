package recommendator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import recommendator.repositories.ClientRepository;
import recommendator.services.ClientPrincipalDetailsService;
import recommendator.config.security.jwt.JwtAuthenticationFilter;
import recommendator.config.security.jwt.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;


/**
 * The annotation @{@link EnableWebSecurity} triggers Spring to load the {@link AppConfig},
 * which for one thing exports the springSecurityFilterChainBean, which the filter registered by {@link SecurityConfig}
 * delegates to.
 * That filter looks for classes implementing {@link WebSecurityConfigurer}.
 * {@link WebSecurityConfigurerAdapter} is a subclass of {@link WebSecurityConfigurer} and thus this class is as well.
 * Then this class gets used for configuring the security of your application.
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@PropertySource("classpath:security.properties")
public class SecurityConfig {

    private ClientRepository clientRepository;
    private ClientPrincipalDetailsService clientPrincipalDetailsService;
    @Value("${jwt_secret}")
    private String secret;

    @Autowired
    public SecurityConfig(ClientRepository clientRepository, ClientPrincipalDetailsService clientPrincipalDetailsService) {
        this.clientRepository = clientRepository;
        this.clientPrincipalDetailsService = clientPrincipalDetailsService;
    }



    @Bean
    @Profile("dev")
    WebSecurityConfigurerAdapter dev() {
        return new WebSecurityConfigurerAdapter() {
            @Override
            public void configure(WebSecurity webSecurity) {
                // ignore all
                webSecurity.ignoring().mvcMatchers("/*");
            }
        };
    }

    @Bean
    @Profile("default")
    WebSecurityConfigurerAdapter standard() {
        return new WebSecurityConfigurerAdapter() {
            @Override
            protected void configure(AuthenticationManagerBuilder auth) {
                auth.authenticationProvider(authenticationProvider());
            }

            /**
             * In this method we get a {@link HttpSecurity} object we can use for configuring the security in our application.
             * <p>
             * It's important to know that the top most configuration should be more specific than the bottom configuration.
             * If I say the url /admin is accessible by admins only and then say all the URLS are open for everyone,
             * that is interpreted as "/admin is secured, but everything else is open for business".
             * However, if I were to do that the other way around, my rule for /admin would be ignored.
             *
             * @param http The object we configure our security in
             * @throws Exception
             */
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                // First we configure it to allow authentication and authorization in REST
                enableRESTAuthentication(http)
                        // Now let's say which requests we want to authorize
                        .authorizeRequests()
                        .and()
                        .cors()
                        .and()
                        // We're disabling defenses against Cross-Site Request Forgery,
                        // as the browser is not responsible for adding authentication information to the request
                        // which is wat the CSRF exploit relies on.
                        .csrf()
                        .disable();

            }


            /**
             * I separated the configuration for security in REST to simplify it.
             * In this method we enable logging in and configure it for a REST API.
             *
             * @param http The {@link HttpSecurity} object we can use for configuration
             * @return
             * @throws Exception
             */
            private HttpSecurity enableRESTAuthentication(HttpSecurity http) throws Exception {
                http
                        .authorizeRequests()
                        // configure access rules
                        .antMatchers(HttpMethod.POST, "/login", "/register").permitAll()
                        .antMatchers("/admin/*").hasRole("ADMIN")
                        .anyRequest().authenticated();

                // add jwt filters (1. authentication, 2. authorization)
                http
                        .addFilter(new JwtAuthenticationFilter(authenticationManager(), secret))
                        .addFilter(new JwtAuthorizationFilter(authenticationManager(),  clientRepository, secret));

                // As it's a REST API, we don't want Spring remembering sessions for users. It should be stateless.
                http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

                // We return it so we can chain more configuration
                return http;
            }

            @Bean
            public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList("http://localhost"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                configuration.setAllowedHeaders(Arrays.asList("Authorization", "content-type"));
                configuration.setExposedHeaders(Collections.singletonList("Authorization"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
            }

            @Bean
            DaoAuthenticationProvider authenticationProvider(){
                DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
                daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
                daoAuthenticationProvider.setUserDetailsService(clientPrincipalDetailsService);
                return daoAuthenticationProvider;
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
