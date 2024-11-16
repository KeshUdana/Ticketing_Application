package managment.backend.service;

import Startup.SystemConfig;
import jakarta.annotation.PostConstruct;
import managment.backend.model.TicketPool;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UserService {
    private final ConfigService configService;
    private final TicketPool ticketPool; // Shared pool for tickets
    private SystemConfig config;

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
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < customerThreads; i++) {
            executorService.submit(() -> {
                try {
                    int ticketId = ticketQueue.retrieveTicket();
                    System.out.println("Customer purchased ticket #" + ticketId);
                } catch (Exception e) {
                    System.out.println("No tickets available for purchase.");
                }
            });
        }

        executorService.shutdown();
    }
}
