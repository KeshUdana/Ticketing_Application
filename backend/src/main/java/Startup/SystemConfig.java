package Startup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemConfig {
    private static final Logger logger = Logger.getLogger(SystemConfig.class.getName());

    private int totalTickets; // Total tickets in the system
    private int maxTicketCapacity; // Max tickets processed per second
    private int vendorReleaseRate; // Rate at which vendors release tickets
    private int userRetrievalRate; // Rate at which customers retrieve tickets

    private static final String CONFIG_FILE = "config.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Default Constructor
    public SystemConfig() {}

    // Getters and Setters
    public int getTotalTickets() { return totalTickets; }
    public int getMaxTicketCapacity() { return maxTicketCapacity; }
    public int getVendorReleaseRate() { return vendorReleaseRate; }
    public int getUserRetrievalRate() { return userRetrievalRate; }

    public void setTotalTickets(int totalTickets) { this.totalTickets = totalTickets; }
    public void setMaxTicketCapacity(int maxTicketCapacity) { this.maxTicketCapacity = maxTicketCapacity; }
    public void setVendorReleaseRate(int vendorReleaseRate) { this.vendorReleaseRate = vendorReleaseRate; }
    public void setUserRetrievalRate(int userRetrievalRate) { this.userRetrievalRate = userRetrievalRate; }

    // Save Configuration to JSON
    public static void saveConfig(SystemConfig config) {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            gson.toJson(config, writer);
            logger.info("Configuration saved successfully.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save configuration: {0}", e.getMessage());
        }
    }

    // Load Configuration from JSON
    public static SystemConfig loadConfig() {
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            return gson.fromJson(reader, SystemConfig.class);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load configuration: {0}", e.getMessage());
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
