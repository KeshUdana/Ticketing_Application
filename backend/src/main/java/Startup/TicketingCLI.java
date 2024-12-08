package Startup;



import managment.backend.model.LogEntry;
import managment.backend.model.TicketPool;
import managment.backend.repository.TicketSaleRepository;
import managment.backend.service.ConsumerService;
import managment.backend.service.ProducerService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class TicketingCLI {

    private volatile boolean systemRunning = false;
    private TicketPool ticketPool;

    // Shared resources

    private List<Thread> producerThreads = new ArrayList<>();
    private List<Thread> consumerThreads = new ArrayList<>();

    //Attributes needed for front end
    private static final String JSON_LOG_FILE="thread_visualize.json";
    private final List<LogEntry>logs=new ArrayList<>();
    private final Gson gson=new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE = "config.json";
    private TicketSaleRepository ticketSaleRepository;


    public static void main(String[] args) throws IOException {
        TicketingCLI cli = new TicketingCLI();

        cli.run();
    }

    private void displayMainMenu() {
        String reset = "\033[0m";
        String green = "\033[32m";
        String bold = "\033[1m";

        System.out.println(green + "\n**********************************************");
        System.out.println("          " + bold + "Welcome to the Ticketing System" + reset + "        ");
        System.out.println("**********************************************" + reset);
        System.out.println("\nPlease choose an option from the menu below:");

        // Display the options
        System.out.println("1. Configure System");
        System.out.println("2. Exit");

        // Prompt the user for input
        System.out.print("\nEnter your choice: ");
    }

    public void run() throws IOException {
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

    private void handleControlPanel(Scanner scanner) throws IOException {
        boolean backToMenu = false;

        while (!backToMenu) {
            System.out.println("\n**********************************************");
            System.out.println("                CONTROL PANEL                  ");
            System.out.println("**********************************************");

            // Display available options
            System.out.println("\n1. " + "START");
            System.out.println("2. " + "STOP");
            System.out.println("3. " + "Return to Main Menu");

            // Prompt for input
            System.out.print("\nEnter your choice: "); String choice = scanner.nextLine().trim();
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

    private void startSystem() throws IOException {
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

        //Initialize Ticketing Repository before threads start
        TicketSaleRepository ticketSaleRepository = new TicketSaleRepository();

        // Start producer threads and consumer threads
        for (int i = 0; i < numProducerThreads; i++) {
            ProducerService producerService=new ProducerService(ticketPool,config,ticketSaleRepository);
          //  Vendor vendor=producerService.getVendor();
            Thread producerThread=new Thread(producerService);
            producerThreads.add(producerThread);
            producerThread.start();
            logThreadEvent("Producer ",producerThread.getId()," Started");


        }
        // Start consumer threads
        for (int i = 0; i < numProducerThreads; i++) {
            ConsumerService consumerService=new ConsumerService(ticketPool,config,ticketSaleRepository);
            Thread consumerThread=new Thread(consumerService);
            consumerThreads.add(consumerThread);
            consumerThread.start();
            logThreadEvent("Consumer ",consumerThread.getId()," Started");
        }
        System.out.println("System started successfully with " + numProducerThreads + " producer-consumer pairs.");
    }
    private synchronized void logThreadEvent(String threadType,Long threadID,String status) throws IOException {
        LogEntry logEntry=new LogEntry(LocalDateTime.now().toString(),threadType,threadID,status);
        logs.add(logEntry);

        try(FileWriter writer=new FileWriter(JSON_LOG_FILE)){
            gson.toJson(logs,writer);
        }catch (IOException e){
            System.err.println("Error writing to Json LOG file: "+e.getMessage());
        }
    }

    private void stopSystem() {
        if (!systemRunning) {
            System.out.println("The system is not currently running.");
            return;
        }

        systemRunning = false;
        //Stop producer threads
        producerThreads.forEach(thread -> {
            thread.interrupt();
            try {
                logThreadEvent("Producer ", thread.getId(),"Stopped");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        //Stop consumer threads
        consumerThreads.forEach(thread -> {
            thread.interrupt();
            try {
                logThreadEvent("Consumer ", thread.getId(), " Stopped");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

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

