package Startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class TicketingCLI {

    private static final Logger logger = LoggerFactory.getLogger(TicketingCLI.class);

    private boolean systemRunning = false;

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
                 // Ensure exit flag is set here
                break;
            default:
                logger.warn("Invalid choice made by user: {}", choice);
                System.out.println("Invalid choice. Please select a valid option.");
        }
    }

    private void configureSystem(Scanner scanner) {
        SystemConfig config = new SystemConfig();

        System.out.print("Enter total tickets: ");
        config.setTotalTickets(getValidatedInteger(scanner));

        System.out.print("Enter max ticket capacity: ");
        config.setMaxTicketCapacity(getValidatedInteger(scanner));

        System.out.print("Enter vendor release rate: ");
        config.setVendorReleaseRate(getValidatedInteger(scanner));

        System.out.print("Enter customer retrieval rate: ");
        config.setUserRetrievalRate(getValidatedInteger(scanner));

        SystemConfig.saveConfig(config);
        logger.info("System configuration saved successfully:\n{}", config);
    }

    private void startSystem() {
        if (systemRunning) {
            logger.warn("Attempted to start the system, but it is already running.");
            System.out.println("The system is already running!");
        } else {
            systemRunning = true;
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
        }
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