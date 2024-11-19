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
    private TicketPool ticketPool;

    private List<Thread> producerThreads = new ArrayList<>();
    private Thread consumerThread;

    private static final String CONFIG_FILE = "config.json";

    public TicketingCLI() {
        // Initialize TicketPool
        if (new File(CONFIG_FILE).exists()){
            ticketPool = new TicketPool(new ArrayBlockingQueue<>(SystemConfig.getTotalTickets()));
        }
    }

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
                    System.out.println("Exiting Ticket Management System CLI.");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }

            // If config.json exists, allow access to the control panel
            if (new File(CONFIG_FILE).exists() && !exit) {
                handleControlPanel(scanner);
            } else if (!exit) {
                System.out.println("Please configure the system first.");
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

        int totalTickets = SystemConfig.getTotalTickets();
        int maxCapacity = SystemConfig.getMaxTicketCapacity();

        int numProducerThreads = totalTickets / maxCapacity;

        if (totalTickets % maxCapacity > 0) {
            numProducerThreads++;
        }

        for (int i = 0; i < numProducerThreads; i++) {
            Vendor vendor = new Vendor();
            Thread producerThread = new Thread(new ProducerService(ticketPool));
            producerThreads.add(producerThread);
            producerThread.start();
        }

        User user = new User();
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

        for (Thread producerThread : producerThreads) {
            producerThread.interrupt();
        }

        consumerThread.interrupt();

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
