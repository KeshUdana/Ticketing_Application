package Startup;

import managment.backend.model.Ticket;
import managment.backend.service.ConsumerService;
import managment.backend.service.ProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TicketingCLI {

    private static final Logger logger = LoggerFactory.getLogger(TicketingCLI.class);

    private boolean systemRunning = false;
    private BlockingQueue<Ticket> ticketQueue = new ArrayBlockingQueue<>(100); // Adjust capacity as needed
    private Thread producerThread; // Reference to producer thread
    private Thread consumerThread; // Reference to consumer thread

    public static void main(String[] args) {
        TicketingCLI cli = new TicketingCLI();
        cli.run();
    }

    public void run() {
        logger.info("Welcome to the Ticket Management System CLI");
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            displayMenu();
            String choice = scanner.nextLine().trim();
            exit = handleMenuChoice(choice, scanner); // Update exit flag
        }
        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Configure System");
        System.out.println("2. Start System");
        System.out.println("3. Stop System");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private boolean handleMenuChoice(String choice, Scanner scanner) {
        switch (choice) {
            case "1":
                configureSystem(scanner);
                break;
            case "2":
                startSystem();
                break;
            case "3":
                stopSystem();
                break;
            case "4":
                logger.info("Exiting Ticket Management System CLI.");
                System.out.println("Exiting Ticket Management System CLI.");
                return true; // Set exit flag to true
            default:
                logger.warn("Invalid choice made by user: {}", choice);
                System.out.println("Invalid choice. Please select a valid option.");
        }
        return false; // Continue running
    }

    private void startSystem() {
        if (systemRunning) {
            logger.warn("Attempted to start the system, but it is already running.");
            System.out.println("The system is already running!");
        } else {
            systemRunning = true;

            // Start producer and consumer threads
            producerThread = new Thread(new ProducerService(ticketQueue));
            consumerThread = new Thread(new ConsumerService(ticketQueue));

            producerThread.start();
            consumerThread.start();

            logger.info("System started successfully.");
            System.out.println("System started successfully.");
        }
    }

    private void stopSystem() {
        if (!systemRunning) {
            logger.warn("Attempted to stop the system, but it is not currently running.");
            System.out.println("The system is not currently running.");
        } else {
            systemRunning = false;
            logger.info("Stopping system...");

            // Optionally interrupt threads or manage shutdown here
            producerThread.interrupt(); // Interrupt producer thread
            consumerThread.interrupt(); // Interrupt consumer thread

            try {
                producerThread.join(); // Wait for producer thread to finish
                consumerThread.join(); // Wait for consumer thread to finish
            } catch (InterruptedException e) {
                logger.error("Error while stopping threads: {}", e.getMessage());
                Thread.currentThread().interrupt(); // Restore interrupted status
            }

            logger.info("System stopped successfully.");
            System.out.println("System stopped successfully.");
        }
    }

    private void configureSystem(Scanner scanner) {
        // Your existing configuration logic...

        // Example of creating a new configuration object (assuming you have a config instance)
        SystemConfig config = new SystemConfig();
        logger.info("System configuration saved successfully:\n{}", config);
    }

    private int getValidatedInteger(Scanner scanner) {
        int value;
        while (true) {
            try {
                value = Integer.parseInt(scanner.nextLine().trim());
                if (value < 0) {
                    throw new NumberFormatException("Value must be non-negative.");
                }
                break;
            } catch (NumberFormatException e) {
                logger.error("Invalid input for integer validation: {}", e.getMessage());
                System.out.print("Invalid input. Please enter a valid non-negative integer: ");
            }
        }
        return value;
    }
}