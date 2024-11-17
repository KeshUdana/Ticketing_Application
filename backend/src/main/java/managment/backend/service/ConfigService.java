package managment.backend.service;

import Startup.SystemConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {

    private SystemConfig config;

    // Load the configuration at the start
    @PostConstruct
    public void initConfig() {
        try {
            // Attempt to load the configuration from config.json
            this.config = SystemConfig.loadConfig();

            // Check if config is null or incomplete, if so, create a default config
            if (config == null) {
                System.out.println("Config file is empty or invalid. Loading default configuration.");
                this.config = createDefaultConfig();
                SystemConfig.saveConfig(config);  // Save the default config to a file
            }
        } catch (Exception e) {
            // In case of any error, load default config and log the issue
            System.out.println("Error loading configuration. Using default configuration.");
            this.config = createDefaultConfig();
            SystemConfig.saveConfig(config);  // Save the default config to a file
        }
    }

    // Get configuration values
    public SystemConfig getConfig() {
        return config;
    }

    // Create a default configuration in case system crashes. This is for testing reasons only.
   private SystemConfig createDefaultConfig() {
       System.out.println("Just testing");
       return null;
   }
       /* SystemConfig defaultConfig = new SystemConfig ( maxTicket,totalTickets; int vendorReleaseRate;int customerRetrievalRate)
        defaultConfig.setMaxTicketCapacity(100);  // Default value for max tickets
        defaultConfig.setTotalTickets(50);  // Default value for total tickets
        defaultConfig.setVendorReleaseRate(5);  // Default value for vendor release rate
        defaultConfig.setUserRetrievalRate(2);  // Default value for user retrieval rate
        return defaultConfig;
    }*/
}
