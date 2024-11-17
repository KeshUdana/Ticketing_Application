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
    private final TicketPool ticketPool; // Shared pool for tickets
    private SystemConfig config;
    private ExecutorService executorService; // For managing vendor threads

    public VendorService(ConfigService configService, TicketPool ticketPool) {
        this.configService = configService;
        this.ticketPool = ticketPool;
    }

    @PostConstruct
    public void init() {
        this.config = configService.getConfig();  // Getting the configuration directly
        int totalTickets = config.getTotalTickets(); // Access the total tickets directly from the config
        System.out.println("Total tickets available in the system: " + totalTickets); // Logging for validation
    }

    public void startVendorThreads() {
        System.out.println("Vendor thread Creation started");
        int vendorThreads = config.getVendorReleaseRate(); // Get vendor thread rate from config
        executorService = Executors.newFixedThreadPool(vendorThreads); // Dynamically adjust the number of threads

        for (int i = 0; i < vendorThreads; i++) {
            executorService.submit(() -> {
                System.out.println("Vendor thread running");
                try {
                    boolean ticketId = ticketPool.addTicket(); // Simulate ticket addition
                    System.out.println("Vendor released ticket #" + ticketId);
                } catch (Exception e) {
                    System.out.println("No tickets available for release.");
                }
            });
        }
    }

    // Gracefully stop vendor threads
    public void stopVendorThreads() {
        System.out.println("Stopping vendor threads...");
        if (executorService != null) {
            try {
                // Gracefully shut down the executor service
                executorService.shutdown();
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }
        }
    }
}
