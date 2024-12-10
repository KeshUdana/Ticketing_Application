package managment.backend.service;

import managment.backend.model.Ticket;
import managment.backend.model.TicketPool;
import managment.backend.model.User;
import managment.backend.repository.TicketSaleRepository;
import Startup.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ConsumerService  {

    private final TicketPool ticketPool;
   // private final User user;
    private final TicketSaleRepository ticketSaleRepository;
    public SystemConfig config;
    private boolean systemRunning;

    // Constructor
    @Autowired
    public ConsumerService(TicketPool ticketPool, SystemConfig config, TicketSaleRepository ticketSaleRepository) {
        if (!ticketPool.isInitialized()) {
            throw new IllegalStateException("TicketPool must be initialized before creating ProducerService.");
        }
        this.ticketPool = ticketPool;
        this.config=config;
        this.ticketSaleRepository=ticketSaleRepository;
        this.systemRunning = true; // Start with the producer running
/*
        //Initialize Consumer details
    this.user=new User();
    user.setUserID(UUID.randomUUID().toString().substring(0,5));
    user.setUserUsername("USER-name");
    user.setUserEmail("user@gmail.com");
    user.setUserPassword(UUID.randomUUID().toString());*/
}
/*
    @Override
    public void run() {
        try {
            while (systemRunning) {
                if (ticketPool.getTicketsConsumed() >= config.getTotalTickets()) {
                    System.out.println("All tickets consumed. Stopping consumer: " + user.getUserID());
                    stop();
                    break;
                }

                // Retrieve and process ticket
                Ticket ticket = ticketPool.retrieveTicket();
                ticketPool.incrementTicketsConsumed();

                // Log the transaction in TicketingCLI
                System.out.println("--------------------------------------");
                System.out.printf("User: %-20s%n", user.getUserUsername());
                System.out.printf("Ticket ID: %-15s%n", ticket.getTicketID());
                System.out.printf("Price: %-15.2f%n", ticket.getTicketPrice());
                System.out.printf("Type: %-15s%n", ticket.getTicketType());
                System.out.println("========================================");
                System.out.println(" ");

                // Simulate user retrieval rate
                Thread.sleep(1000 / config.getUserRetrievalRate());
            }
        } catch (InterruptedException e) {
            System.out.println("Consumer thread interrupted.");
            Thread.currentThread().interrupt();
        }
    }
    // Stop the consumer
    public void stop() {
        systemRunning = false;
    }
*/
}
