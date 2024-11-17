package Startup;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Scanner;

public class TicketingCLI {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get system parameters from the admin
        System.out.println("Enter Max Tickets: ");
        int maxTicket = scanner.nextInt();
        System.out.println("Enter Total Tickets: ");
        int totalTickets = scanner.nextInt();
        System.out.println("Enter Vendor Release Rate: ");
        int vendorReleaseRate = scanner.nextInt();
        System.out.println("Enter Customer Retrieval Rate: ");
        int customerRetrievalRate = scanner.nextInt();

        // Create a configuration object and set the parameters
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setMaxTicketCapacity(maxTicket);
        systemConfig.setTotalTickets(totalTickets);
        systemConfig.setVendorReleaseRate(vendorReleaseRate);
        systemConfig.setUserRetrievalRate(customerRetrievalRate);

        // Write this configuration to a config.json file
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("config.json"), systemConfig);
            System.out.println("Configuration saved to config.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




































































/*
import managment.backend.service.ConfigService;
import managment.backend.service.TicketingService;
import managment.backend.service.VendorService;
import managment.backend.service.UserService;
import managment.backend.model.TicketPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class TicketingCLI {

    private static TicketingCLI instance;  // Singleton instance of TicketingCLI
    private static final ReentrantLock lock = new ReentrantLock();
    private SystemConfig config;  // Holds the system configuration
    private BlockingQueue<Integer> ticketQueue;  // Queue to manage ticket availability
    private AtomicInteger ticketCounter;  // Counter to track the number of tickets issued
    private boolean running;  // Flag to track whether the system is running
    private Scanner input;  // Declare scanner as a field for user input
    private TicketingService ticketingService; // Service dependency

    @Autowired
    private ConfigService configService;  // Inject ConfigService to access the configuration

    // Private constructor to enforce Singleton
    private TicketingCLI() {
        input = new Scanner(System.in);
        this.config = configService.getConfig();  // Get the config from ConfigService
        ticketQueue = new LinkedBlockingQueue<>();  // Initialize the ticket queue
        ticketCounter = new AtomicInteger(0);  // Initialize ticket counter
    }

    // Singleton pattern to ensure only one instance of TicketingCLI
    public static TicketingCLI getInstance() {
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new TicketingCLI();
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    // Method to start the system and display the current configuration
    public void start() {
        // Display the current configuration
        System.out.println("Current Configuration: ");
        System.out.println(config.toString());

        // Initialize the service
        initializeService();

        // Allow user to start or stop the system
        System.out.print("Enter START to Start the System: ");
        while (true) {
            String command = input.nextLine().trim().toUpperCase();
            if ("START".equals(command)) {
                System.out.println("Starting the System...");
                ticketingService.startSystem();  // Use the service instance
                break;
            } else if ("STOP".equals(command)) {
                System.out.println("Stopping the System...");
                ticketingService.stopSystem();  // Use the service instance
                break;  // Exit the loop
            } else {
                System.out.println("Invalid command. Only enter START or STOP");
            }
        }
    }

    // Configure the TicketingService after parameters are entered
    private void initializeService() {
        // Create instances of the services required by TicketingService
        VendorService vendorService = new VendorService(configService,ticketPool);  // Pass ConfigService to VendorService
        UserService userService = new UserService(configService,ticketPool);  // Pass ConfigService to UserService
        TicketPool ticketPool = new TicketPool(configService.getTotalTickets());

        // Initialize TicketingService with the required services
        ticketingService = new TicketingService(vendorService, userService, ticketPool);
    }

    // Main method to run the CLI
    public static void main(String[] args) {
        // Get the Singleton instance of TicketingCLI
        TicketingCLI cli = TicketingCLI.getInstance();
        cli.start();  // Start the system
    }
}
*/