package Startup;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketingCLI {
    private static TicketingCLI instance;  // Singleton instance of TicketingCLI
    private SystemConfig config;  // Holds the system configuration
    private BlockingQueue<Integer> ticketQueue;  // Queue to manage ticket availability
    private AtomicInteger ticketCounter;  // Counter to track the number of tickets issued
    private boolean running;  // Flag to track whether the system is running
    private Scanner input;  // Declare scanner as a field for user input

    // Constructor - Initializes the system
    private TicketingCLI() {
        // Instantiate the Scanner here so it can be used in both methods
        input = new Scanner(System.in);

        // Check if config.json exists before loading
        File configFile = new File("config.json");
        if (configFile.exists()) {
            config = ConfigManager.loadConfig();  // Load existing configuration
        } else {
            // If config file doesn't exist, prompt user for input and save it
            System.out.println("No configuration found. Setting up new configuration.");
            config = new SystemConfig();  // Create a new configuration
            setupConfig();  // Collect configuration from user
        }
        ticketQueue = new LinkedBlockingQueue<>();  // Initialize the ticket queue
        ticketCounter = new AtomicInteger(0);  // Initialize ticket counter
    }

    // Singleton pattern to ensure only one instance of TicketingCLI
    public static synchronized TicketingCLI getInstance() {
        if (instance == null) {
            instance = new TicketingCLI();
        }
        return instance;
    }

    // Method to collect and save configuration from the user
    private void setupConfig() {
        // Prompts user for input if no configuration file exists
        config.setMaxTicketCapacity(getValidIntInput("Enter Maximum Tickets Capacity for the system: "));
        config.setTotalTickets(getValidIntInput("Enter Total Tickets for the system: "));
        config.setVendorReleaseRate(getValidDoubleInput("Enter Vendor Ticket Release Rate: "));
        config.setUserRetrievalRate(getValidDoubleInput("Enter Customer Retrieval Rate: "));
        ConfigManager.saveConfig(config);  // Save the configuration to a JSON file
    }

    // Method to start the system and display the current configuration
    public void start() {
        // Display the current configuration
        System.out.println("Current Configuration: ");
        System.out.println(config.toString());

        // Allow user to start or stop the system
        System.out.print("Enter START to Start the System: ");
        while (true) {
            String command = input.nextLine().trim().toUpperCase();
            if ("START".equals(command)) {
                startTicketing();  // Start the ticketing process
            } else if ("STOP".equals(command)) {
                stopTicketing();  // Stop the ticketing process
                break;  // Exit the loop
            } else {
                System.out.println("Invalid command. Only enter START or STOP");
            }
        }
    }

    // Method to get valid integer input from the user
    private int getValidIntInput(String prompt) {
        int value;
        while (true) {
            System.out.println(prompt);
            if (input.hasNextInt()) {
                value = input.nextInt();
                input.nextLine();  // Consume the leftover newline character
                if (value > 0) {
                    return value;  // Return the valid positive integer
                }
            } else {
                input.next();  // discard invalid input
            }
            System.out.println("Invalid input. Enter a positive integer.");
        }
    }

    // Method to get valid double input from the user
    private double getValidDoubleInput(String prompt) {
        double value;
        while (true) {
            System.out.print(prompt);
            if (input.hasNextDouble()) {
                value = input.nextDouble();
                input.nextLine();  // Consume the leftover newline character
                if (value > 0) {
                    return value;  // Return the valid positive number
                }
            } else {
                input.next();  // discard invalid input
            }
            System.out.println("Invalid input. Please enter a positive number.");
        }
    }

    // Method to start the ticketing process (if not already running)
    private void startTicketing() {
        if (running) {
            System.out.println("Ticketing process is already running.");
            return;  // Return if already running
        }
        System.out.println("Starting ticketing process...");
        running = true;  // Set the flag to indicate the system is running
        new Thread(this::vendorTickets).start();  // Start the vendor ticketing process in a new thread
        new Thread(this::customerTickets).start();  // Start the customer ticketing process in a new thread
    }

    // Method to stop the ticketing process (if running)
    private void stopTicketing() {
        if (!running) {
            System.out.println("Ticketing process is not running.");
            return;  // Return if not running
        }
        System.out.println("Stopping ticketing process...");
        running = false;  // Set the flag to indicate the system is stopped
    }

    // Method to simulate the vendor releasing tickets
    private void vendorTickets() {
        while (running) {
            try {
                // If ticket counter is less than max capacity, release a new ticket
                if (ticketCounter.get() < config.getMaxTicketCapacity()) {
                    ticketQueue.put(ticketCounter.incrementAndGet());  // Add ticket to queue
                    System.out.println("Vendor Added ticket #" + ticketCounter.get());
                }
                Thread.sleep((long) (1000 / config.getVendorReleaseRate()));  // Sleep based on vendor release rate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // Handle interruption
            }
        }
    }

    // Method to simulate customers purchasing tickets
    private void customerTickets() {
        while (running) {
            try {
                Integer ticket = ticketQueue.take();  // Take a ticket from the queue
                System.out.println("Ticket #" + ticket + " purchased by Customer");
                Thread.sleep((long) (1000 / config.getUserRetrievalRate()));  // Sleep based on customer retrieval rate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // Handle interruption
            }
        }
    }

    // Main method to run the CLI
    public static void main(String[] args) {
        TicketingCLI cli = TicketingCLI.getInstance();  // Get the singleton instance
        cli.start();  // Start the system
    }
}
/*
* Comments explanation:
Constructor: Initializes the Scanner object, checks if the configuration file exists, loads or sets up the configuration accordingly, and initializes necessary fields like ticketQueue and ticketCounter.
Singleton pattern: The getInstance method ensures that only one instance of TicketingCLI exists.
setupConfig: Prompts the user for necessary configuration settings like maximum ticket capacity, release rate, and retrieval rate.
start: Displays the current configuration and waits for the user to enter "START" to start the system, or "STOP" to stop it.
getValidIntInput and getValidDoubleInput: These methods prompt the user for valid integer and double inputs, respectively, ensuring that the input is valid and positive.
startTicketing: Starts the ticketing process by launching two threads: one for the vendor releasing tickets and one for customers purchasing tickets.
stopTicketing: Stops the ticketing process by setting the running flag to false.
vendorTickets: Simulates the vendor releasing tickets and adding them to the queue.
customerTickets: Simulates customers purchasing tickets from the queue.
main: Initializes and starts the ticketing system.
* */