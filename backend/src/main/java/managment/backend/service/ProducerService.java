package managment.backend.service;

import Startup.SystemConfig;
import managment.backend.model.Ticket;
import managment.backend.model.TicketPool;

import java.util.UUID;

public class ProducerService implements Runnable {
    private TicketPool ticketPool;

    public ProducerService(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        try {
            ticketPool.initialize(); // Ensure TicketPool is initialized
            while (true) {

                // Generate a new ticket
                Ticket ticket = new Ticket();
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
