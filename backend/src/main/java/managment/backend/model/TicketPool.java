package managment.backend.model;

import managment.backend.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TicketPool {
    private final BlockingQueue<Integer> ticketQueue;
    private final AtomicInteger ticketCounter;
    private final ConfigService configService;

    // Inject ConfigService to load the total tickets capacity from the config
    @Autowired
    public TicketPool(ConfigService configService) {
        this.configService = configService;

        // Retrieve the total number of tickets (capacity) from the config
        int totalTickets = configService.getConfig().getTotalTickets();
        if (totalTickets <= 0) {
            throw new IllegalArgumentException("Ticket pool capacity must be greater than 0.");
        }

        this.ticketQueue = new LinkedBlockingQueue<>(totalTickets); // Set capacity from config
        this.ticketCounter = new AtomicInteger(0);

        // Add initial tickets to the pool
        for (int i = 0; i < totalTickets; i++) {
            boolean ticketID = ticketQueue.offer(i + 1);  // Ticket IDs start from 1
        }
    }

    // Add a new ticket to the pool
    public boolean addTicket() {
        int totalTickets = ticketQueue.remainingCapacity() + ticketQueue.size(); // Dynamic capacity
        if (ticketCounter.get() < totalTickets) {
            try {
                int newTicket = ticketCounter.incrementAndGet();
                ticketQueue.put(newTicket);
                System.out.println("Ticket #" + newTicket + " added to the pool.");
                return true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return false;
    }

    // Retrieve a ticket from the pool
    public Integer retrieveTicket() {
        try {
            Integer ticket = ticketQueue.take();
            System.out.println("Ticket #" + ticket + " retrieved from the pool.");
            return ticket;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return null; // Failure to retrieve
    }

    // Get current ticket count
    public int getTicketCount() {
        return ticketQueue.size();
    }
}
