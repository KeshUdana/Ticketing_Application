package Startup;

import managment.backend.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TicketingCLI {

    private static final Logger logger = LoggerFactory.getLogger(TicketingCLI.class);

    private boolean systemRunning = false;
    private BlockingQueue<Ticket> ticketQueue = new ArrayBlockingQueue<>(100); // Adjust capacity as needed

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
            handleMenuChoice(choice, scanner);
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

    private void handleMenuChoice(String choice, Scanner scanner) {
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

                break;
            default:
                logger.warn("Invalid choice made by user: {}", choice);
                System.out.println("Invalid choice. Please select a valid option.");
        }
    }

    private void startSystem() {
        if (systemRunning) {
            logger.warn("Attempted to start the system, but it is already running.");
            System.out.println("The system is already running!");
        } else {
            systemRunning = true;

            // Start producer and consumer threads
            Thread producerThread = new Thread(new VendorService(ticketQueue));
            Thread consumerThread = new Thread(new (ticketQueue));

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
            logger.info("System stopped successfully.");
            System.out.println("System stopped successfully.");

            // Optionally interrupt threads or manage shutdown here
        }
    }

    private void configureSystem(Scanner scanner) {
        // Your existing configuration logic...

        logger.info("System configuration saved successfully:\n{}", config);
    }

    private int getValidatedInteger(Scanner scanner) {
        // Your existing input validation logic...
        return value;
    }
}