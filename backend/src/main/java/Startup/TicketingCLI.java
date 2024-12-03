package Startup;

import managment.backend.model.TicketPool;
import managment.backend.repository.TicketSaleRepository;
import managment.backend.service.ConsumerService;
import managment.backend.service.ProducerService;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TicketingCLI {

    private volatile boolean systemRunning = false;
    /*
    private ProducerService producerService;
    private ConsumerService consumerService;*/
    private TicketPool ticketPool;

    // Shared resourcesz
   // private managment.backend.model.TicketPool ticketPool;
    private List<Thread> producerThreads = new ArrayList<>();
    private List<Thread> consumerThreads = new ArrayList<>();


    private static final String CONFIG_FILE = "config.json";
    private TicketSaleRepository ticketSaleRepository;

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

        // Save configuration
        SystemConfig.saveConfig(config);
        System.out.println("System configuration saved successfully.");

        // Initialize TicketPool
        try {
            this.ticketPool = TicketPool.getInstance();
            ticketPool.initialize(config);
            System.out.println(" ");
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
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
        int numProducerThreads = totalTickets / maxCapacity + (totalTickets % maxCapacity > 0 ? 1 : 0);

        //Initiliaze Ticketing Repository before threads start
        TicketSaleRepository ticketSaleRepository = new TicketSaleRepository();

        // Start producer threads and consumer threads
        for (int i = 0; i < numProducerThreads; i++) {
            ProducerService producerService=new ProducerService(ticketPool,config,ticketSaleRepository);
          //  Vendor vendor=producerService.getVendor();
            Thread producerThread=new Thread(producerService);
            producerThreads.add(producerThread);
            producerThread.start();
        }

        // Start consumer threads
        for (int i = 0; i < numProducerThreads; i++) {
            ConsumerService consumerService=new ConsumerService(ticketPool,config,ticketSaleRepository);
           // User user=consumerService.getUser();
            Thread consumerThread=new Thread(consumerService);
            consumerThreads.add(consumerThread);
            consumerThread.start();

        }

        System.out.println("System started successfully with " + numProducerThreads + " producer-consumer pairs.");
    }

    private void stopSystem() {
        if (!systemRunning) {
            System.out.println("The system is not currently running.");
            return;
        }

        systemRunning = false;

        // Stop producer threads
        producerThreads.forEach(Thread::interrupt);

        // Stop consumer threads
        consumerThreads.forEach(Thread::interrupt);

        try {
            for (Thread thread : producerThreads) {
                thread.join();
            }
            for (Thread thread : consumerThreads) {
                thread.join();
            }
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
