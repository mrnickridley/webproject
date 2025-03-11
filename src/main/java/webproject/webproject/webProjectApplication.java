package webproject.webproject;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import webproject.webproject.services.payPalService;

/*"ApplicationRunner" allows you to define custom logic that should run once the application starts */
/*the "public void run(ApplicationArguments args)" runs the ApplicationRunner and 
retrieves and prints the Access Token when program runs */
@SpringBootApplication
public class webProjectApplication implements ApplicationRunner {

	private final payPalService serv;

	public webProjectApplication(payPalService pServ){
		this.serv = pServ;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(webProjectApplication.class, args);
	}

	@Override
    public void run(ApplicationArguments args) {
        // Call the method to get the token
        String token = serv.myToken();
        System.out.println("Access Token: " + token);
    }


}
