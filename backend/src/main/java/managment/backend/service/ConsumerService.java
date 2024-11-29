package managment.backend.service;

import managment.backend.model.Ticket;
import managment.backend.model.TicketPool;
import managment.backend.model.User;
import Startup.SystemConfig;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService implements Runnable {

    private final TicketPool ticketPool;
    private final User user; // User object
  //  private final int userRetrievalRate;
    public SystemConfig config;
    private volatile boolean running = true;

    // Constructor
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ConsumerService(TicketPool ticketPool, User user,SystemConfig config) {
        this.ticketPool = ticketPool;
        this.user = user;
        this.config=config;
    }

    @Override
    public void run() {
        try {
            while (running) {
                if (ticketPool.getTicketsConsumed() >= config.getTotalTickets()) {
                    System.out.println("All tickets consumed. Stopping consumer: " + user.getUserID());
                    stop();
                    break;
                }

                // Retrieve and process ticket
                Ticket ticket = ticketPool.retrieveTicket();
                ticketPool.incrementTicketsConsumed();

                System.out.println("User " + user.getUserID() + " retrieved ticket: " + ticket.getTicketID()+" "+ticket.getTicketPrice());

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
        running = false;
    }
}
