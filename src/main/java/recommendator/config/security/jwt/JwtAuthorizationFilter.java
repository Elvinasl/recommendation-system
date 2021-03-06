package recommendator.config.security.jwt;

import com.auth0.jwt.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import recommendator.config.security.ClientPrincipal;
import recommendator.models.entities.Client;
import recommendator.repositories.ClientRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;


public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private ClientRepository clientRepository;
    private String secret;

    @Autowired
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, ClientRepository clientRepository, String secret) {
        super(authenticationManager);
        this.clientRepository = clientRepository;
        this.secret = secret;
    }

    @Autowired
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint, ClientRepository clientRepository, String secret) {
        super(authenticationManager, authenticationEntryPoint);
        this.clientRepository = clientRepository;
        this.secret = secret;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Read the Authorization header, where the JWT token should be
        String header = request.getHeader(JwtProperties.HEADER_STRING);

        // If header does not contain BEARER or is null delegate to Spring impl and exit
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        } else {
            // Be sure to clear everything if something when wrong
            SecurityContextHolder.clearContext();
        }

        // If header is present, try grab user principal from database and perform authorization
        Authentication authentication = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Continue filter execution
        chain.doFilter(request, response);
    }

    /*
        Based on jwt token we try to grab the user by its email which is coded in jwt token
     */
    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX, "");

        if (token.length() > 0) {
            // parse the token and validate it
            String email = JWT.require(HMAC512(secret.getBytes()))
                    .build()
                    .verify(token)
                    .getSubject();

            // Search in the DB if we find the user by token subject (username)
            // If so, then grab user details and create spring auth token using username, pass, authorities/roles
            if (email != null) {
                Client client = clientRepository.getByEmail(email);
                ClientPrincipal principal = new ClientPrincipal(client);
                return new UsernamePasswordAuthenticationToken(email, null, principal.getAuthorities());
            }
        }
        throw new BadCredentialsException("Bad credentials");
    }
}
