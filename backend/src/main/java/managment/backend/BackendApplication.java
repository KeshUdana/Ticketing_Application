package managment.backend;

import Startup.TicketingCLI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		// Initialize and run the CLI
		TicketingCLI cli = TicketingCLI.getInstance();
		cli.start(); // Gather config and validate

		// Start Spring Boot application
		SpringApplication.run(BackendApplication.class, args);
	}
}
