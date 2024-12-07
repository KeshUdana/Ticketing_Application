package managment.backend;

import Startup.TicketingCLI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) throws IOException {
		// Check if the config.json file exists,usually dis from previosu execution
		File configFile = new File("config.json");


		if (!configFile.exists()) {
			// Run the TicketingCLI to gather configuration from the user
			TicketingCLI.main(args);  //this is the entry pitn .Actual one
		} else {
			System.out.println("CLI from previous running exists bro");
		}
		// Start the Angular Server along with Spring Boot for usabiltiy
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
			String angularProjectPath = "../../frontend";
			File projectDir = new File(angularProjectPath);

			if (!projectDir.exists() || !new File(projectDir, "angular.json").exists()) {
				throw new IOException("Angular project directory not found or invalid: " + projectDir.getAbsolutePath());
			}

			ProcessBuilder processBuilder = new ProcessBuilder("ng", "serve");
			processBuilder.directory(projectDir);
			Process process = processBuilder.start();

			// Print output to the console
			new Thread(() -> {
				try (BufferedReader reader = new BufferedReader(
						new InputStreamReader(process.getInputStream()))) {
					String line;
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
					}
				} catch (IOException e) {
					System.err.println("Error reading Angular server output: " + e.getMessage());
				}
			}).start();

			System.out.println("Angular server started successfully in: " + projectDir.getAbsolutePath());
			return process;
		} catch (IOException e) {
			System.err.println("Failed to start Angular server: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}


}
