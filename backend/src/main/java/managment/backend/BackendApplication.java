package managment.backend;

import Startup.TicketingCLI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) throws IOException {
		// Check if the config.json file exists
		File configFile = new File("config.json");

		// If the config.json file doesn't exist, run the CLI to gather the config
		if (!configFile.exists()) {
			// Run the TicketingCLI to gather configuration from the user
			TicketingCLI.main(args);  // Directly call the main method of TicketingCLI
		}else{
			System.out.println("CLI from previous running exists bro");
		}

		// Start Spring Boot application
		SpringApplication.run(BackendApplication.class, args);
	}
}
