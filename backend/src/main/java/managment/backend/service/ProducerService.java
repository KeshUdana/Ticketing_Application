package managment.backend.service;

import Startup.SystemConfig;
import managment.backend.model.Ticket;
import managment.backend.model.TicketPool;
import managment.backend.model.Vendor;

public class ProducerService implements Runnable {
    private final TicketPool ticketPool;
    private final Vendor vendor;
    private final SystemConfig config;
    //private final int vendorReleaseRate;  Vendor release rate extracted from config

    private boolean systemRunning; // Flag to control when to stop the producer

    public ProducerService(TicketPool ticketPool, Vendor vendor, SystemConfig config) {
        if (!ticketPool.isInitialized()) {
            throw new IllegalStateException("TicketPool must be initialized before creating ProducerService.");
        }
        this.ticketPool = ticketPool;
        this.vendor = vendor;
        this.config=config;
        this.systemRunning = true; // Start with the producer running
    }

    // Method to stop the producer
   public void stopProducing() {
        systemRunning = false;
    }

    @Override
    public void run() {
        try {
            while (systemRunning) {
                if (ticketPool.getTicketsProduced() >= config.getTotalTickets()) {
                    System.out.println("Total tickets produced. Stopping producer: " + vendor.getVendorID());
                    stopProducing();
                    break;
                }

                // Generate and add ticket
                Ticket ticket = new Ticket();
                ticketPool.addTicket(ticket);
                ticketPool.incrementTicketProduced();

                System.out.println("Vendor " + vendor.getVendorID() + " added ticket: " + ticket);

                // Simulate vendor release rate
                Thread.sleep(1000 / config.getVendorReleaseRate());
            }
        } catch (InterruptedException e) {
            System.out.println("Producer thread interrupted.");
            Thread.currentThread().interrupt();
        }
    }

}
