package managment.backend.service;

import Startup.SystemConfig;
import jakarta.annotation.PostConstruct;
import managment.backend.model.TicketPool;
import managment.backend.service.ConfigService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class VendorService {
    private final ConfigService configService;
    private final TicketPool ticketPool; // Shared pool for tickets
    private SystemConfig config;

    public VendorService(ConfigService configService, TicketPool ticketPool) {
        this.configService = configService;
        this.ticketPool = ticketPool;
    }

    public void startVendorThreads() {
        int vendorThreads = config.getVendorReleaseRate();
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < vendorThreads; i++) {
            executorService.submit(() -> {
                try {
                    int ticketId = ticketPool.retrieveTicket();
                    System.out.println("Customer purchased ticket #" + ticketId);
                } catch (Exception e) {
                    System.out.println("No tickets available for purchase.");
                }
            });
        }

        executorService.shutdown();
    }
    public void stopVendorThread(){}

    @PostConstruct
    public void init() {
        this.config = configService.getConfig();
    }

    @Scheduled(fixedRate = 1000) // Run every second
    public void releaseTickets() {
        int vendorReleaseRate = config.getVendorReleaseRate();

        for (int i = 0; i < vendorReleaseRate; i++) {
            int ticketId = TicketPool.addTicket();
            System.out.println("Vendor released ticket #" + ticketId);
        }
    }
}
