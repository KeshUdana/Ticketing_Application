package managment.backend.service;

import Startup.SystemConfig;
import jakarta.annotation.PostConstruct;
import managment.backend.model.TicketPool;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class VendorService {
    private final ConfigService configService;
    private final TicketPool ticketPool; // Injected shared pool for tickets
    private SystemConfig config;
    private ExecutorService executorService; // For managing vendor threads

    public VendorService(ConfigService configService, TicketPool ticketPool) {
        this.configService = configService;
        this.ticketPool = ticketPool;
    }

    @PostConstruct
    public void init() {
        this.config = configService.getConfig(); // Getting the configuration directly
        int totalTickets = config.getTotalTickets(); // Access the total tickets directly from the config
        System.out.println("Total tickets available in the system: " + totalTickets); // Logging for validation
    }

    public void startVendorThreads() {
        System.out.println("Vendor thread creation started...");
        int vendorThreads = config.getVendorReleaseRate(); // Number of threads matches release rate
        executorService = Executors.newFixedThreadPool(vendorThreads); // Dynamically adjust the number of threads

        for (int i = 0; i < vendorThreads; i++) {
            executorService.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) { // Run continuously until interrupted
                    try {
                        boolean success = ticketPool.addTicket(); // Attempt to add a ticket to the pool
                        if (success) {
                            System.out.println("Vendor released a ticket.");
                        } else {
                            System.out.println("Ticket pool is full. Vendor waiting...");
                            Thread.sleep(500); // Wait before retrying
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Restore the interrupted status
                        System.out.println("Vendor thread interrupted, stopping...");
                        break; // Exit the loop
                    } catch (Exception e) {
                        System.err.println("Error in vendor thread: " + e.getMessage());
                    }
                }
            });
        }
    }

    // Gracefully stop vendor threads
    public void stopVendorThreads() {
        System.out.println("Stopping vendor threads...");
        if (executorService != null) {
            try {
                executorService.shutdown(); // Gracefully shut down the executor service
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }
        }
    }
}
