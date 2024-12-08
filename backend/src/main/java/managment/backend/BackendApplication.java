package managment.backend;

import Startup.TicketingCLI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


import java.io.File;
import java.io.IOException;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ComponentScan(basePackages = "Startup")
public class BackendApplication {

	public static void main(String[] args) throws IOException {
		// Check if the config.json file exists, usually from previous execution
		File configFile = new File("config.json");

		if (!configFile.exists()) {
			// Start Spring Boot application
			SpringApplication.run(BackendApplication.class, args);
			TicketingCLI.main(args);  // This is the entry point. Actual one,using core java has been given priority
		} else {
			System.out.println("CLI from previous running exists bro");
		}
	}
}



