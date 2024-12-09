package managment.backend;

import Startup.TicketingCLI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ComponentScan(basePackages = "Startup")
public class BackendApplication {

	public static void main(String[] args) throws IOException {
		// Check if the config.json file exists,from my previous execution
		File configFile = new File("config.json");
		if (!configFile.exists()) {
			// Start Spring Boot
			SpringApplication.run(BackendApplication.class, args);

			// Create an ExecutorService to run the ticketing simulation in a separate thread
			ExecutorService executorService = Executors.newSingleThreadExecutor();
			executorService.submit(() -> {
				try {
					TicketingCLI.main(args); // Execute the TicketingCLI.main method
				} catch (IOException e) {
					e.printStackTrace(); // Handle potential exceptions
				}
			});

		} else {
			System.out.println("CLI from previous session exists. Delete it and try again.");
		}
	}
}
