package Startup;


import managment.backend.model.*;
import managment.backend.persistence.TicketSales;
import managment.backend.repository.TicketSaleRepository;
import managment.backend.service.ConsumerService;
import managment.backend.service.ProducerService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Component
public class TicketingCLI {

    private volatile boolean systemRunning = false;
    private TicketPool ticketPool;
    private Vendor vendor;



    private List<Thread> producerThreads = new ArrayList<>();
    private List<Thread> consumerThreads = new ArrayList<>();


    private static final String JSON_LOG_FILE="thread_visualize.json";
    private final List<LogEntry>logs=new ArrayList<>();
    private final Gson gson=new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE = "config.json";
    private static int producerCount = 0;
    private static int consumerCount = 0;



    public static void main(String[] args) throws IOException, InterruptedException {
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

    public void run() throws IOException, InterruptedException {
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

        SystemConfig.saveConfig(config);
        System.out.println("System configuration saved successfully.");

        // Initialize TicketPool
        try {
            this.ticketPool = TicketPool.getInstance();
            ticketPool.initialize(config);
            // Initialize Ticketing Repository and Pool
            System.out.println("TicketPool initialized ");
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleControlPanel(Scanner scanner) throws IOException, InterruptedException {
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

    private void startSystem() throws IOException, InterruptedException {
        if (systemRunning) {
            System.out.println("The system is already running!");
            return;
        }
        systemRunning = true;

        SystemConfig config = SystemConfig.loadConfig();
        int totalTickets = config.getTotalTickets();
        int maxCapacity = config.getMaxTicketCapacity();
        int numProducerThreads = totalTickets / maxCapacity + (totalTickets % maxCapacity > 0 ? 1 : 0);
        int numConsumers = config.getTotalTickets();

        TicketSaleRepository ticketSaleRepository = new TicketSaleRepository();
        CountDownLatch latch = new CountDownLatch(1);

        // Start producer threads
        for (int i = 0; i < numProducerThreads; i++) {
            Vendor vendor = createVendor();
            Thread producerThread = new Thread(() -> {
                try {
                    latch.await();
                    while (ticketPool.getTicketsProduced() < config.getTotalTickets()) {

                        User user = createUser();
                        Ticket ticket = createTicket();

                        TicketSales sale = generateTicketSale(ticket, vendor, user);
                        ticketSaleRepository.save(sale);
                        ticketPool.addTicket(ticket);
                        ticketPool.incrementTicketsProduced();

                        logTicketDetails(vendor, ticket);
                        Thread.sleep(1000 / config.getVendorReleaseRate());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Producer thread interrupted.");
                }
            });

            producerThreads.add(producerThread);
            producerThread.start();
            producerCount++;
            logThreadEvent("Producer ", producerThread.getId(), " Started");
        }

        // Start consumer threads
        for (int j = 0; j < numConsumers; j++) {
            Thread consumerThread = new Thread(() -> {
                try {
                    latch.await(); // Wait until `TicketPool` is ready
                    while (true) {
                        Ticket ticket = ticketPool.retrieveTicket();
                        ticketPool.incrementTicketsConsumed();
                        if (ticket == null) {
                            break; // Exit if no tickets are left
                        }
                        logThreadEvent("Consumer ", Thread.currentThread().getId(), " Processed Ticket ID: " + ticket.getTicketID());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Consumer thread interrupted.");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            consumerThreads.add(consumerThread);
            consumerThread.start();
            consumerCount++;
           // System.out.println("Consumer " + consumerThread.getId() + " Started");
        }

        latch.countDown();
        System.out.println("System started successfully with " + numProducerThreads + " producers and " + numConsumers + " consumers");
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
            return;}
        systemRunning = false;

        producerThreads.forEach(thread -> {
            thread.interrupt();
            producerCount--;
            try {
                logThreadEvent("Producer ", thread.getId(),"Stopped");
            } catch (IOException e) {
                throw new RuntimeException(e);}
        });

        consumerThreads.forEach(thread -> {
            thread.interrupt();
            consumerCount--;
            try {
                logThreadEvent("Consumer ", thread.getId(), " Stopped");
            } catch (IOException e) {
                throw new RuntimeException(e);}
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
    //////////////////////////////////////////////////////////////
    // Get the current thread counts for front end
    public static int getProducerCount() {return producerCount;}
    public static int getConsumerCount(){return consumerCount;}

    //////////////////////////////////////////////////////////////////
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
    /////////////////////////////////////////////////////////////////////////
    //Producer Related Code
    private Vendor createVendor(){
        this.vendor=new Vendor();
        vendor.setVendorID(UUID.randomUUID().toString().substring(0,5));
        vendor.setVendorUsername("Vendor-name");
        vendor.setVendorEmail("vendor@gmail.com");
        vendor.setVendorPassword(UUID.randomUUID().toString());
        return vendor;
    }
    private User createUser() {
        User user = new User();
        user.setUserID(UUID.randomUUID().toString().substring(0, 8)); // Longer unique ID
        user.setUserUsername("USER-name");
        user.setUserEmail("user@gmail.com");
        user.setUserPassword(UUID.randomUUID().toString());
        return user;
    }

    private Ticket createTicket() {
        Ticket ticket = new Ticket();
        ticket.setTicketID(UUID.randomUUID().toString());
        ticket.setTicketType(Math.random() < 0.5 ? "VIP" : "Regular");
        ticket.setTicketPrice(ticket.getTicketType().equals("VIP") ? 1000.00 : 500.0);
        ticket.setTimeStamp(java.time.LocalDateTime.now().toString());
        return ticket;
    }

    private void logTicketDetails(Vendor vendor, Ticket ticket) {
        System.out.println("========== Ticket Added ==========");
        System.out.printf("Vendor ID: %-15s%n", vendor.getVendorID());
        System.out.printf("Ticket ID: %-15s%n", ticket.getTicketID());
        System.out.printf("Price: %-15.2f%n", ticket.getTicketPrice());
        System.out.printf("Type: %-15s%n", ticket.getTicketType());
    }
    private TicketSales generateTicketSale(Ticket ticket, Vendor vendor, User user){
        TicketSales sale=new TicketSales();
        sale.setTicket(ticket);
        sale.setVendor(vendor);
        sale.setUser(user);
        sale.setTransactionTime(LocalDateTime.now());
        sale.setTicketPrice(ticket.getTicketPrice());
        sale.setTicketType(ticket.getTicketType());
        return sale;
    }

}

