package managment.backend.service;

import Startup.SystemConfig;
import managment.backend.model.Ticket;
import managment.backend.model.TicketPool;
import managment.backend.model.Vendor;
import managment.backend.persistence.TicketSales;
import managment.backend.repository.ticketSaleRepository;

import java.time.LocalDateTime;

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

                // Generate a new ticket and set its properties using setters
                Ticket ticket = new Ticket();
                ticket.setTicketID(System.nanoTime());//Unique ID

                ticket.setTicketType(Math.random()<0.5?"VIP":"Regular");
                ticket.setTicketPrice(ticket.getTicketType()=="VIP"?1000.00:500.0);
                ticket.setTimeStamp(java.time.LocalDateTime.now().toString());

                //Create the transaction and save to the DB
                TicketSales sale=new TicketSales();
                sale.setTicket(ticket);
                sale.setVendor(vendor);
                sale.setUser(user);
                sale.setTransactionTime(LocalDateTime.now());
                sale.setTicketPrice(ticket.getTicketPrice());
                sale.setTicketType(ticket.getTicketType());

                //Save the transaction to the database
                ticketSaleRepository.save(sale);

                //Add the ticekt tot the pool and increment the count of produced ticekts
                ticketPool.addTicket(ticket);
                ticketPool.incrementTicketsProduced();

                System.out.println("Vendor " +
                        vendor.getVendorID() + " added ticket: "
                        + ticket.getTicketID()+", "
                        +ticket.getTicketPrice()+", "
                        +ticket.getTicketType());

                // Simulate vendor release rate
                Thread.sleep(1000 / config.getVendorReleaseRate());
            }
        } catch (InterruptedException e) {
            System.out.println("Producer thread interrupted.");
            Thread.currentThread().interrupt();
        }
    }

}
