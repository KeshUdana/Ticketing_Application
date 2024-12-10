package managment.backend;

import Startup.TicketingCLI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.IOException;

@SpringBootApplication(
		exclude = {
				DataSourceAutoConfiguration.class,
				HibernateJpaAutoConfiguration.class,
				SecurityAutoConfiguration.class
		}
)
@ComponentScan(basePackages = {"Startup"})
public class BackendApplication {

	public static void main(String[] args) throws IOException, InterruptedException {
		// Check if the config.json file exists
		File configFile = new File("config.json");

		if (!configFile.exists()) {
			// Start Spring Boot application
			SpringApplication.run(BackendApplication.class, args);
			TicketingCLI.main(args);
		} else {
			System.out.println("CLI from a previous session exists. Delete it and try again.");
		}
	}
}
