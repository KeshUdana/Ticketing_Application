package managment.backend;

import Startup.TicketingCLI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

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
@EnableAsync
public class BackendApplication {

	public static void main(String[] args) throws IOException, InterruptedException {

		File configFile = new File("config.json");

		if (!configFile.exists()) {

			SpringApplication.run(BackendApplication.class, args);
			TicketingCLI.main(args);
		} else {
			System.out.println("CLI from a previous session exists. Delete it and try again.");
		}
	}
}
