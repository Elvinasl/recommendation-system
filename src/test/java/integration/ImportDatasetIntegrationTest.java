package integration;

import io.cucumber.java.en.When;
import org.springframework.http.ResponseEntity;
import recommendator.exceptions.responses.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImportDatasetIntegrationTest extends SpringIntegrationTest {

    @When("the client imports a dataset")
    public void the_client_imports_a_dataset() {
//        ResponseEntity<Response> response = post("http://localhost:8080/import", "{}");
//        assertEquals(300, response);
    }
}
