package Startup;


import managment.backend.model.TicketPool;
import managment.backend.service.ConsumerService;
import managment.backend.service.ProducerService;
import managment.backend.model.User;
import managment.backend.model.Vendor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;


public class TicketingCLI {

    private boolean systemRunning = false;

    // Create the TicketPool object
    private TicketPool ticketPool = new TicketPool(new ArrayBlockingQueue<>(SystemConfig.getMaxTicketCapacity()));

    private List<Thread> producerThreads = new ArrayList<>();
    private Thread consumerThread;

    private static final String CONFIG_FILE = "config.json";

    public static void main(String[] args) {
        TicketingCLI cli = new TicketingCLI();
        cli.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            displayMainMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    configureSystem(scanner);
                    break;
                case "2":
                    // logger.info("Exiting Ticket Management System CLI.");
                    System.out.println("Exiting Ticket Management System CLI.");
                    exit = true;
                    break;
                default:
                    // logger.warn("Invalid choice made by user: {}", choice);
                    System.out.println("Invalid choice. Please select a valid option.");
            }

            // If config.json exists, allow access to the control panel
            if (new File(CONFIG_FILE).exists() && !exit) {
                handleControlPanel(scanner);
            }
        }
        scanner.close();
    }

    private void displayMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Configure System");
        System.out.println("2. Exit");
        System.out.print("Enter your choice: ");
    }

    private void handleControlPanel(Scanner scanner) {
        boolean backToMenu = false;

        while (!backToMenu) {
            System.out.println("\n////// CONTROL PANEL /////");
            System.out.println("1. START");
            System.out.println("2. STOP");
            System.out.println("3. Return to Main Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    startSystem();
                    break;
                case "2":
                    stopSystem();
                    break;
                case "3":
                    backToMenu = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    private void startSystem() {
        if (systemRunning) {
            System.out.println("The system is already running!");
            return;
        }
        systemRunning = true;

        // Dynamically calculate the number of producer threads based on Total Tickets and Max Ticket Capacity
        int numProducerThreads = SystemConfig.getTotalTickets() / SystemConfig.getMaxTicketCapacity();

        // If there are remaining tickets, we need one more thread
        if (SystemConfig.getTotalTickets() % SystemConfig.getMaxTicketCapacity() > 0) {
            numProducerThreads++;
        }

        // Create vendor (producer) threads
        for (int i = 0; i < numProducerThreads; i++) {
            Vendor vendor = new Vendor();  // Create a new vendor for each thread
            Thread producerThread = new Thread(new ProducerService(ticketPool, vendor));
            producerThreads.add(producerThread);
            producerThread.start();
        }

        // Create the consumer (user) thread
        User user = new User();  // Create a user for the consumer thread
        consumerThread = new Thread(new ConsumerService(ticketPool, user));
        consumerThread.start();

        System.out.println("System started successfully with " + numProducerThreads + " vendor threads.");
    }

    private void stopSystem() {
        if (!systemRunning) {
            System.out.println("The system is not currently running.");
            return;
        }
        systemRunning = false;

        // Interrupt all producer threads
        for (Thread producerThread : producerThreads) {
            producerThread.interrupt();
        }

        // Interrupt the consumer thread
        consumerThread.interrupt();

        // Wait for all threads to finish
        try {
            for (Thread producerThread : producerThreads) {
                producerThread.join();
            }
            consumerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Error while stopping threads.");
        }

        System.out.println("System stopped successfully.");
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
        System.out.println("System configuration saved successfully.");
    }

    private int getValidatedInteger(Scanner scanner) {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= 0) {
                    return value;
                }
                System.out.print("Value must be non-negative. Please try again: ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid integer: ");
            }
        }
    }
}
