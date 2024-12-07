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
		} else {
			System.out.println("CLI from previous running exists bro");
		}
		// Start the Angular Server along with Spring Boot
		Process angularProcess = startAngularServer();

		// Add shutdown hook to terminate Angular server when backend stops
		if (angularProcess != null) {
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				angularProcess.destroy();
				System.out.println("Angular server stopped.");
			}));
		}

		// Start Spring Boot application
		SpringApplication.run(BackendApplication.class, args);
	}

	private static Process startAngularServer() {
		try {
			String angularProjectPath = "../../../../../frontend";

			ProcessBuilder processBuilder = new ProcessBuilder("ng", "serve");
			processBuilder.directory(new File(angularProjectPath));
			processBuilder.inheritIO(); // Ensures logs are visible in the console

			// Start the Angular process
			Process process = processBuilder.start();
			System.out.println("Angular server started successfully.");
			return process;
		} catch (IOException e) {
			System.err.println("Failed to start Angular server: " + e.getMessage());
			return null;
		}
	}
}
