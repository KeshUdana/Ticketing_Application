package Startup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public  class SystemConfig {
    private static final Logger logger = LoggerFactory.getLogger(SystemConfig.class);
    private static int totalTickets; // Total tickets in the system aka total transaction that should be done in a simulation
    private static int maxTicketCapacity; // Maximum tickets transactied within a second
    private static int vendorReleaseRate; // Rate at which vendors release tickets
    private static int userRetrievalRate; // Rate at which customers retrieve tickets

    private static final String CONFIG_FILE = "config.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Default Constructor
    public SystemConfig() {
    }

    // Getters and Setters...
    public static int getTotalTickets(){return totalTickets;}
    public static int getMaxTicketCapacity(){return maxTicketCapacity;}
    public static int getVendorReleaseRate(){return vendorReleaseRate;}
    public static int getUserRetrievalRate(){return userRetrievalRate;}

    protected void setTotalTickets(int totalTickets){this.totalTickets=totalTickets;}
    protected void setMaxTicketCapacity(int maxTicketCapacity){this.maxTicketCapacity=maxTicketCapacity;}
    protected void setUserRetrievalRate(int userRetrievalRate){this.userRetrievalRate=userRetrievalRate;}
    protected void setVendorReleaseRate(int vendorReleaseRate){this.vendorReleaseRate=vendorReleaseRate;}

    // Save Configuration to JSON
    public static void saveConfig(SystemConfig config) {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            gson.toJson(config, writer);
            logger.info("Configuration saved successfully.");
        } catch (IOException e) {
            logger.error("Failed to save configuration: {}", e.getMessage());
        }
    }

    // Load Configuration from JSON
    public static SystemConfig loadConfig() {
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            return gson.fromJson(reader, SystemConfig.class);
        } catch (IOException e) {
            logger.error("Failed to load configuration: {}", e.getMessage());
            return new SystemConfig(); // Return a default configuration in case of failure
        }
    }

    @Override
    public String toString() {
        return "SystemConfig {" +
                "totalTickets=" + totalTickets +
                ", maxTicketCapacity=" + maxTicketCapacity +
                ", vendorReleaseRate=" + vendorReleaseRate +
                ", userRetrievalRate=" + userRetrievalRate +
                '}';
    }
}