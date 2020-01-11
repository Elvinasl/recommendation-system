import config.AppConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@ContextConfiguration(classes = { AppConfig.class })
@WebAppConfiguration
public class SpringIntegrationTest {

}
