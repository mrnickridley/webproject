package webproject.webproject.payPalConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/*This configuration class provides a Rest Template bean and injects the payPalService class */
@Configuration
public class payPalConfig {
    @Bean
    /*This method creates and returns an instance of the RestTemplate class
     * "RestTemplate" is a synchronious client provided bySpring for making Http requests to RESTful web services
     * By declaring this as a bean, you can inject "@Autowired" this RestTemplate into other components of your Spring application, promoting reusability and maintainability.
     */
    public RestTemplate template(){
        return new RestTemplate();
    }
}
