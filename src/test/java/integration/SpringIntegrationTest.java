package integration;

import config.AppConfig;
import exceptions.responses.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

@ContextConfiguration(classes = { AppConfig.class })
@WebAppConfiguration
public class SpringIntegrationTest {

    RestTemplate restTemplate;

    public SpringIntegrationTest(){
        restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    public ResponseEntity<Response> post(String url, Object object){
        return restTemplate.postForEntity(url, object, Response.class);
    }


}
