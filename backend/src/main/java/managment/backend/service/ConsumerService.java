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
    public SystemConfig config;
    private volatile boolean running = true;

    // Constructor
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ConsumerService(TicketPool ticketPool, User user) {
        this.ticketPool = ticketPool;
        this.user = user;
    }

    @Override
    public void run() {
        try {
            while (running) {
                // Retrieve a ticket from the pool
                Ticket ticket = ticketPool.retrieveTicket();

                // Log consumption
                System.out.println("User " + user.getUserID() + " retrieved: " + ticket); // Fixed: Replaced `User.getUserID()` with `user.getUserID()`

                // Sleep for the global consumption rate duration
                Thread.sleep(1000 / config.getUserRetrievalRate());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("User " + user.getUserID() + " stopped."); // Fixed: Display user ID correctly
        }
    }

    // Stop the consumer
    public void stop() {
        running = false;
    }
}
