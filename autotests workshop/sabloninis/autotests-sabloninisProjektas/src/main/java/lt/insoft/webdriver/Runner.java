package lt.insoft.webdriver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"lt.insoft.webdriver.runner"})
public class Runner {

	public static void main(String[] args) {
		SpringApplication.run(Runner.class, args);
	}
	
}
