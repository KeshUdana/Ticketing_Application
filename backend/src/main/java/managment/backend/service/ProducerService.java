package managment.backend.service;

import Startup.SystemConfig;
import managment.backend.model.Ticket;
import managment.backend.model.TicketPool;
import managment.backend.model.Vendor;

public class ProducerService implements Runnable {
    private TicketPool ticketPool;
    private boolean systemRunning;  // Flag to control when to stop the producer
    private Vendor vendor;

    public ProducerService(TicketPool ticketPool,Vendor vendor) {
        this.ticketPool = ticketPool;
        this.systemRunning = true;  // Start with the producer running
        this.vendor=vendor;
    }

    // Method to stop the producer
    public void stopProducing() {
        systemRunning = false;
    }

    @Override
    public void run() {
        try {
            ticketPool.initialize(); // Ensure TicketPool is initialized
            while (systemRunning) {  // Control the loop with systemRunning flag
                // Generate a new ticket
                Ticket ticket = new Ticket();

                // Add the ticket to the pool, blocking if the pool is full
                ticketPool.addTicket(ticket);
                // Log consumption
                System.out.println("Vendor " + vendor.getVendorID() + " added " + ticket); // Fixed: Replaced `User.getUserID()` with `user.getUserID()`

                // Simulate the vendor release rate
                Thread.sleep(1000/SystemConfig.getVendorReleaseRate());
            }
        } catch (InterruptedException e) {
            System.out.println("Producer thread interrupted.");
            Thread.currentThread().interrupt();
        }
    }
}
