package managment.backend.model;

import Startup.SystemConfig;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TicketPool {
    private static TicketPool instance; // Singleton instance
    private BlockingQueue<Ticket> ticketQueue;
    private SystemConfig config;
    private boolean initialized = false;

    private AtomicInteger ticketsProduced = new AtomicInteger(0);
    private AtomicInteger ticketsConsumed = new AtomicInteger(0);

    public int getTicketsProduced() {
        return ticketsProduced.get();
    }

    public int getTicketsConsumed() {
        return ticketsConsumed.get();
    }

    public void incrementTicketsProduced() {
        ticketsProduced.incrementAndGet();
    }

    public void incrementTicketsConsumed() {
        ticketsConsumed.incrementAndGet();
    }

    public TicketPool() {
    }
    public static synchronized TicketPool getInstance() {
        if (instance == null) {
            instance = new TicketPool();
        }
        return instance;
    }

    public synchronized void initialize(SystemConfig config) {
        if (initialized) {
            throw new IllegalStateException("TicketPool is already initialized.");
        }
        if (config == null) {
            throw new IllegalArgumentException("SystemConfig cannot be null.");
        }
        this.config = config;
        this.ticketQueue = new ArrayBlockingQueue<>(config.getTotalTickets());

        this.initialized = true;
        System.out.println("TicketPool initialized with capacity: " + config.getTotalTickets());
    }

    public void addTicket(Ticket ticket) throws InterruptedException {
        if (!initialized) {
            throw new IllegalStateException("TicketPool not initialized yet.");
        }
        ticketQueue.put(ticket); // Blocking call to add a ticket
        incrementTicketsProduced();
        System.out.println("Ticket added: " + ticket);
    }

    public Ticket retrieveTicket() throws InterruptedException {
        if (!initialized) {
            throw new IllegalStateException("TicketPool not initialized yet.");
        }
        Ticket ticket = ticketQueue.take(); // Blocking call to retrieve a ticket
        incrementTicketsConsumed();
        System.out.println("Ticket retrieved: " + ticket);
        return ticket;
    }
    public boolean isInitialized() {
        return initialized;
    }

}
