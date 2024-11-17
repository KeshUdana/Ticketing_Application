package managment.backend.service;

import Startup.SystemConfig;
import jakarta.annotation.PostConstruct;
import managment.backend.model.TicketPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    private final ConfigService configService;
    private final TicketPool ticketPool; // Injected TicketPool instance
    private SystemConfig config;
    private ExecutorService executorService; // For managing customer threads

    @Autowired
    public UserService(ConfigService configService, TicketPool ticketPool) {
        this.configService = configService;
        this.ticketPool = ticketPool;
    }

    @PostConstruct
    public void init() {
        this.config = configService.getConfig();
    }

    public void startCustomerThreads() {
        int customerThreads = config.getUserRetrievalRate();
        executorService = Executors.newFixedThreadPool(customerThreads);

        for (int i = 0; i < customerThreads; i++) {
            executorService.submit(() -> {
                try {
                    Integer ticketId = ticketPool.retrieveTicket();
                    if (ticketId != null) { // Ticket retrieved successfully
                        System.out.println("Customer purchased ticket #" + ticketId);
                    }
                } catch (Exception e) {
                    System.out.println("Error while retrieving a ticket: " + e.getMessage());
                }
            });
        }
    }

    public void stopCustomerThreads() {
        System.out.println("Stopping customer threads...");
        if (executorService != null) {
            try {
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
