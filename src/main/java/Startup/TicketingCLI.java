package Startup;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/*
* Implementing the TicketingCLI class as a singleton i to ensure that only one instance
* of the TicketingCLI exists across the entire application. This approach is often
* used in cases where shared resources or configuration need to be managed centrally.
* */

public class TicketingCLI {
    private static TicketingCLI instance; // Single instance of TicketingCLI (Singleton)
    private SystemConfig config; // Configuration settings for ticketing system
    private BlockingQueue<Integer> ticketQueue; // Queue to store tickets
    private AtomicInteger ticketCounter; // Counter to track ticket numbers
    private boolean running; // Indicates if the ticketing system is active
    private String prompt; // Prompt message for user input (not currently used)

    // Private constructor to prevent instantiation from outside (Singleton)
    private TicketingCLI() {
        config = new SystemConfig(); // Initialize configuration
        ticketQueue = new LinkedBlockingQueue<>(); // Initialize ticket queue
        ticketCounter = new AtomicInteger(0); // Initialize ticket counter
        running = false; // Set initial running status to false
    }

    // Public method to provide access to the single instance of TicketingCLI
    public static synchronized TicketingCLI getInstance() {
        if (instance == null) { // Check if instance is already created
            instance = new TicketingCLI(); // Create a new instance if null
        }
        return instance; // Return the single instance
    }

    // Sets up initial system configuration through user input
    private void setupConfig(Scanner input) {
        config.setMaxTicketCapacity(getValidIntInput(input, "Enter Maximum Tickets Capacity for the system: "));
        config.setTotalTickets(getValidIntInput(input, "Enter Total Tickets for the system: "));
        config.setVendorReleaseRate(getValidDoubleInput(input, "Enter Vendor Ticket Release Rate: "));
        config.setUserRetrievalRate(getValidDoubleInput(input, "Enter Customer Retrieval Rate: "));
    }

    // Starts the CLI to accept user commands
    public void start() {
        try (Scanner input = new Scanner(System.in)) {
            setupConfig(input); // Configure system settings based on user input

            while (true) { // Continuously waits for user commands
                String command = input.nextLine().trim().toUpperCase(); // Read and format command
                System.out.print("Enter START to Start the System: ");
                if ("START".equals(command)) { // Start ticketing if command is "START"
                    startTicketing();
                } else if ("STOP".equals(command)) { // Stop ticketing if command is "STOP"
                    stopTicketing();
                    break; // Exit the loop after stopping
                } else { // Handle invalid commands
                    System.out.println("Invalid command. Only enter START or STOP");
                }
            }
        }
    }

    // Gets valid integer input from user with positive value validation
    private int getValidIntInput(Scanner input, String prompt) {
        int value;
        while (true) { // Loop until valid input is entered
            System.out.println(prompt); // Display prompt
            if (input.hasNextInt()) { // Check if input is an integer
                value = input.nextInt();
                if (value > 0) { // Validate that input is positive
                    input.nextLine(); // Clear the newline
                    return value; // Return valid input
                }
            } else { // Handle invalid input
                input.next(); // Clear invalid input
            }
            System.out.println("Invalid input. Enter positive integer"); // Error message
        }
    }

    // Gets valid double input from user with positive value validation
    private double getValidDoubleInput(Scanner input, String prompt) {
        double value;
        while (true) { // Loop until valid input is entered
            System.out.print(prompt); // Display prompt
            if (input.hasNextDouble()) { // Check if input is a double
                value = input.nextDouble();
                if (value > 0) { // Validate that input is positive
                    input.nextLine(); // Clear the newline
                    return value; // Return valid input
                }
            } else { // Handle invalid input
                input.next(); // Clear invalid input
            }
            System.out.println("Invalid input. Please enter a positive number."); // Error message
        }
    }

    // Starts the ticketing process and launches vendor and customer threads
    private void startTicketing() {
        if (running) { // Check if ticketing is already running
            System.out.println("Ticketing process is currently running.");
            return;
        }
        System.out.println("Starting ticketing process...");
        running = true; // Set running status to true

        // Start vendor and customer threads for ticketing operations
        new Thread(this::vendorTickets).start();
        new Thread(this::customerTickets).start();
    }

    // Stops the ticketing process
    private void stopTicketing() {
        if (!running) { // Check if ticketing is already stopped
            System.out.println("Ticketing process is not running.");
            return;
        }
        System.out.println("Stopping ticketing process...");
        running = false; // Set running status to false
    }

    // Vendor thread function to add tickets to the queue at a defined rate
    private void vendorTickets() {
        while (running) { // Loop while ticketing is active
            try {
                if (ticketCounter.get() < config.getMaxTicketCapacity()) { // Check capacity limit
                    ticketQueue.put(ticketCounter.incrementAndGet()); // Add ticket to queue
                    System.out.println("Added ticket #" + ticketCounter.get()); // Log ticket addition
                }
                Thread.sleep((long) (1000 / config.getVendorReleaseRate())); // Delay based on release rate
            } catch (InterruptedException e) { // Handle interruption
                Thread.currentThread().interrupt();
            }
        }
    }

    // Customer thread function to retrieve tickets from the queue at a defined rate
    private void customerTickets() {
        while (running) { // Loop while ticketing is active
            try {
                Integer ticket = ticketQueue.take(); // Retrieve ticket from queue
                System.out.println("Ticket #" + ticket + " purchased by Customer"); // Log purchase
                Thread.sleep((long) (1000 / config.getUserRetrievalRate())); // Delay based on retrieval rate
            } catch (InterruptedException e) { // Handle interruption
                Thread.currentThread().interrupt();
            }
        }
    }

    // Main method to initialize and start the TicketingCLI instance
    public static void main(String[] args) {
        TicketingCLI cli = TicketingCLI.getInstance(); // Get the singleton instance
        cli.start(); // Start the CLI
    }
}
