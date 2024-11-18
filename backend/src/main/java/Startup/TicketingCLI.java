package Startup;

import managment.backend.model.Ticket;
import managment.backend.service.ConsumerService;
import managment.backend.service.ProducerService;


import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TicketingCLI {


    private boolean systemRunning = false;
    private BlockingQueue<Ticket> ticketPool= new ArrayBlockingQueue<>(SystemConfig.getMaxTicketCapacity()); // Adjust capacity as needed
    private Thread producerThread;
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
                  //  logger.info("Exiting Ticket Management System CLI.");
                    System.out.println("Exiting Ticket Management System CLI.");
                    exit = true;
                    break;
                default:
                  //  logger.warn("Invalid choice made by user: {}", choice);
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

        // Start producer and consumer threads
        producerThread = new Thread(new ProducerService(ticketPool));
        consumerThread = new Thread(new ConsumerService(ticketPool));

        producerThread.start();
        consumerThread.start();

        System.out.println("System started successfully.");
    }

    private void stopSystem() {
        if (!systemRunning) {
            System.out.println("The system is not currently running.");
            return;
        }
        systemRunning = false;

        // Interrupt threads and wait for them to finish
        producerThread.interrupt();
        consumerThread.interrupt();

        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
           // logger.error("Error while stopping threads: {}", e.getMessage());
            System.out.println("Error while stopping threads");
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
