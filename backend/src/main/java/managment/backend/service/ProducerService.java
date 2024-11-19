package managment.backend.service;

import Startup.SystemConfig;
import managment.backend.model.Ticket;
import managment.backend.model.TicketPool;

public class ProducerService implements Runnable {
    private TicketPool ticketPool;
    private boolean systemRunning;  // Flag to control when to stop the producer

    public ProducerService(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
        this.systemRunning = true;  // Start with the producer running
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

                // Simulate the vendor release rate
                Thread.sleep(SystemConfig.getVendorReleaseRate());
            }
        } catch (InterruptedException e) {
            System.out.println("Producer thread interrupted.");
            Thread.currentThread().interrupt();
        }
    }
}
