package config.security.filters;

import config.security.ClientPrincipal;
import models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import repositories.ClientRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiValidationFilter extends BasicAuthenticationFilter {

    private ClientRepository clientRepository;
    private static String API_KEY_HEADER = "API-KEY";

    @Autowired
    public ApiValidationFilter(AuthenticationManager authenticationManager, ClientRepository clientRepository) {
        super(authenticationManager);
        this.clientRepository = clientRepository;
    }

    @Autowired
    public ApiValidationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint, ClientRepository clientRepository) {
        super(authenticationManager, authenticationEntryPoint);
        this.clientRepository = clientRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // Read the Authorization header, where the JWT token should be
        String apiKey = request.getHeader(API_KEY_HEADER);

        if (apiKey == null) {
            // Be sure to clear everything if something when wrong
            SecurityContextHolder.clearContext();
            throw new BadCredentialsException("API key was not found...");
        }

        // If header is present, try grab user principal from database and perform authorization
        Authentication authentication = getApiAuthentication(apiKey);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    private Authentication getApiAuthentication(String apiKey) {

        if (apiKey.length() > 0) {
            Client client = clientRepository.getByApiKey(apiKey);
            ClientPrincipal principal = new ClientPrincipal(client);
            return new UsernamePasswordAuthenticationToken(client.getEmail(), null, principal.getAuthorities());
        }
        throw new BadCredentialsException("Invalid API key");
    }
}
