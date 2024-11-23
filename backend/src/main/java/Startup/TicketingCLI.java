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
    private Vendor vendor;//testing this to remove the damn errors

    private List<Thread> producerThreads = new ArrayList<>();
    private Thread consumerThread;

    private static final String CONFIG_FILE = "config.json";



    public static void main(String[] args) {
        TicketingCLI cli = new TicketingCLI();
        cli.run();
    }
    private void displayMainMenu() {
        System.out.println("+++++++++++Welcome to Ticketing Application++++++++++");
        System.out.println("\nMain Menu:");
        System.out.println("1. Configure System");
        System.out.println("2. Exit");
        System.out.print("Enter your choice: ");
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

        // Save configuration and initialize TicketPool
        SystemConfig.saveConfig(config);
        ticketPool = new TicketPool(new ArrayBlockingQueue<>(config.getTotalTickets()));
        System.out.println("System configuration saved successfully.");


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


        // Load configuration
        SystemConfig config = SystemConfig.loadConfig();

        int totalTickets = config.getTotalTickets();
        int maxCapacity = config.getMaxTicketCapacity();

        int numProducerThreads = totalTickets / maxCapacity;
        if (totalTickets % maxCapacity > 0) {
            numProducerThreads++;
        }

        for (int i = 0; i < numProducerThreads; i++) {
            Thread producerThread = new Thread(new ProducerService(ticketPool, new Vendor("Vendor " + (i + 1)),config.getVendorReleaseRate()));
            producerThreads.add(producerThread);
            producerThread.start();

        }
        for(int i=0;i<numProducerThreads;i++) {
            User user = new User("Consumer " + (i + 1));
            consumerThread = new Thread(new ConsumerService(ticketPool, user));
            consumerThread.start();
        }
       // System.out.println("System started successfully with " + numProducerThreads + " vendors");
    }


    private void stopSystem() {
        if (!systemRunning) {
            System.out.println("The system is not currently running.");
            return;
        }
        systemRunning = false;


        // Interrupt all threads
        producerThreads.forEach(Thread::interrupt);
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
