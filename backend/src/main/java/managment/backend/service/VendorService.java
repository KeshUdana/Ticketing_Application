package managment.backend.service;

import Startup.SystemConfig;
import jakarta.annotation.PostConstruct;
import managment.backend.model.TicketPool;
import managment.backend.service.ConfigService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class VendorService {
    private final ConfigService configService;
    private final TicketPool ticketPool; // Shared queue for tickets
    private SystemConfig config;

    public VendorService(ConfigService configService, TicketPool ticketPool) {
        this.configService = configService;
        this.ticketPool = ticketPool;
    }

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
