package managment.backend.model;

import Startup.SystemConfig;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketPool {
    private static TicketPool instance; // Singleton instance
    private BlockingQueue<Ticket> ticketQueue;
    private SystemConfig config;
    private boolean initialized = false;

    ///////////////////////////////////////////////////////////////////////
    // Attributes to keep track of the tickets
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
    ///////////////////////////////////////////////////////////////////////

    // Private constructor for Singleton pattern
    private TicketPool() {
    }

    // Singleton getter
    public static synchronized TicketPool getInstance() {
        if (instance == null) {
            instance = new TicketPool();
        }
        return instance;
    }

    // Initialize TicketPool with a given configuration
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


    // Add a ticket to the pool
    public void addTicket(Ticket ticket) throws InterruptedException {
        if (!initialized) {
            throw new IllegalStateException("TicketPool not initialized yet.");
        }
        ticketQueue.put(ticket); // Blocking call to add a ticket
        incrementTicketsProduced();
        System.out.println("Ticket added: " + ticket);
    }

    // Retrieve a ticket from the pool
    public Ticket retrieveTicket() throws InterruptedException {
        if (!initialized) {
            throw new IllegalStateException("TicketPool not initialized yet.");
        }
        Ticket ticket = ticketQueue.take(); // Blocking call to retrieve a ticket
        incrementTicketsConsumed();
        System.out.println("Ticket retrieved: " + ticket);
        return ticket;
    }

    // Check if the pool is initialized
    public boolean isInitialized() {
        return initialized;
    }
}
