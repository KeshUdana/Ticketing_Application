package managment.backend.service;

import Startup.SystemConfig;
import jakarta.annotation.PostConstruct;
import managment.backend.model.TicketPool;
import managment.backend.service.ConfigService;
import org.springframework.scheduling.annotation.Scheduled;
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
        this.config = configService.getConfig();
    }

    public void startVendorThreads() {
        int vendorThreads = config.getVendorReleaseRate(); // Get vendor thread rate from config
        executorService = Executors.newFixedThreadPool(vendorThreads); // Dynamically adjust the number of threads

        for (int i = 0; i < vendorThreads; i++) {
            executorService.submit(() -> {
                try {
                    int ticketId = ticketPool.retrieveTicket(); // Simulate ticket retrieval
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

    // Scheduled task to release tickets at a fixed rate
    @Scheduled(fixedRate = 1000) // Run every second
    public void releaseTickets() {
        int vendorReleaseRate = config.getVendorReleaseRate(); // Get release rate from config

        for (int i = 0; i < vendorReleaseRate; i++) {
            int ticketId = ticketPool.addTicket(); // Add a new ticket to the pool
            System.out.println("Vendor released ticket #" + ticketId);
        }
    }
}
