package managment.backend.service;

import Startup.SystemConfig;
import managment.backend.model.Ticket;
import managment.backend.model.TicketPool;

import managment.backend.model.User;
import managment.backend.model.Vendor;
import managment.backend.persistence.TicketSales;
import managment.backend.repository.TicketSaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@SuppressWarnings("ALL")
@Service
public class ProducerService implements Runnable {
    private final TicketPool ticketPool;
    private final Vendor vendor;
    private final SystemConfig config;
    @Autowired
    private TicketSaleRepository ticketSaleRepository;
    private boolean systemRunning; // Flag to control when to stop the producer
    private User user;

    @Autowired
    public ProducerService(TicketPool ticketPool, SystemConfig config, TicketSaleRepository ticketSaleRepository) {
        if (!ticketPool.isInitialized()) {
            throw new IllegalStateException("TicketPool must be initialized before creating ProducerService.");
        }
        this.ticketPool = ticketPool;
        this.config=config;
        this.ticketSaleRepository=ticketSaleRepository;
        this.systemRunning = true; // Start with the producer running

        //Initialize vendor details
        this.vendor=new Vendor();
        vendor.setVendorID(UUID.randomUUID().toString().substring(0,5));
        vendor.setVendorUsername("Vendor-name");
        vendor.setVendorEmail("vendor@gmail.com");
        vendor.setVendorPassword(UUID.randomUUID().toString());
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

                //Generate a new user for the transaction
                User user=new User();
                user.setUserID(UUID.randomUUID().toString().substring(0,5));
                user.setUserUsername("USER-name");
                user.setUserEmail("user@gmail.com");
                user.setUserPassword(UUID.randomUUID().toString());

                // Generate a new ticket and set its properties using setters
                Ticket ticket = new Ticket();
                ticket.setTicketID(UUID.randomUUID().toString().substring(0,5));//Unique UUI with just 5 characters
                ticket.setTicketType(Math.random()<0.5?"VIP":"Regular");
                ticket.setTicketPrice(ticket.getTicketType()=="VIP"?1000.00:500.0);
                ticket.setTimeStamp(java.time.LocalDateTime.now().toString());



                //Using gnerateTicektSale mthod instead as encapsualtion
                TicketSales sale=generateTicketSale(ticket,vendor,user);
                //Save the transaction to the database
                ticketSaleRepository.save(sale);
                System.out.println("Transaction saved for Ticket ID: " + ticket.getTicketID());

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

    }// Getter for user info to log in TicketingCLI

    private TicketSales generateTicketSale(Ticket ticket, Vendor vendor, User user){
    TicketSales sale=new TicketSales();
    sale.setTicket(ticket);
    sale.setVendor(vendor);
    sale.setUser(user);
    sale.setTransactionTime(LocalDateTime.now());
    sale.setTicketPrice(ticket.getTicketPrice());
    sale.setTicketType(ticket.getTicketType());
    return sale;
    }


    public Vendor getVendor() {
        return this.vendor;
    }


}
/*

                //Create the transaction and save to the DB
                TicketSales sale=new TicketSales();
                sale.setTicket(ticket);
                sale.setVendor(vendor);

                sale.setTransactionTime(LocalDateTime.now());
                sale.setTicketPrice(ticket.getTicketPrice());
                sale.setTicketType(ticket.getTicketType());

 */
