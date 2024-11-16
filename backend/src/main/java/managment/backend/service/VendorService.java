package managment.backend.service;

import Startup.ConfigManager;
import Startup.SystemConfig;
import managment.backend.model.Ticket;
import managment.backend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class VendorService {
    @Autowired
    private TicketRepository ticketRepository; // Inject repository

    private final AtomicInteger ticket = new AtomicInteger(0);
    private final SystemConfig config;

    public VendorService(BlockingQueue<Long> ticketQueue) {
        // Load configuration using ConfigManager
        this.config = ConfigManager.loadConfig();
    }

    // Method to process tickets (vendor adding tickets)
    @Scheduled(fixedRateString = "${vendor.releaseRate}") // fixedRateString can be read from config
    public void releaseTickets() {
        int vendorReleaseRate = config.getVendorReleaseRate();  // Get rate from config
        System.out.println("Vendor release rate: " + vendorReleaseRate);

        // Release tickets based on the vendor release rate
        for (int i = 0; i < vendorReleaseRate; i++) {
            Ticket ticket = new Ticket();  // Create new ticket
            ticket.setTicketID(ticketCount.incrementAndGet());  // Assign ticket ID
            try {
                ticketQueue.put(ticket);  // Add ticket to the queue (blocking)
                System.out.println("Vendor released a ticket: " + ticket.getTicketID());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
