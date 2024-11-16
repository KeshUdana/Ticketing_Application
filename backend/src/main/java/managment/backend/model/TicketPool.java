package managment.backend.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketPool {
    private final BlockingQueue<Integer> ticketQueue;
    private final AtomicInteger ticketCounter;
    private final int maxCapacity;

    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.ticketQueue = new LinkedBlockingQueue<>(maxCapacity); // Limit to max capacity
        this.ticketCounter = new AtomicInteger(0);
    }

    // Add a new ticket to the pool
    public boolean addTicket() {
        if (ticketCounter.get() < maxCapacity) {
            try {
                int newTicket = ticketCounter.incrementAndGet();
                ticketQueue.put(newTicket);
                System.out.println("Ticket #" + newTicket + " added to the pool.");
                return true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            System.out.println("Ticket pool is full.");
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
        return null;
    }

    // Get current ticket count
    public int getTicketCount() {
        return ticketQueue.size();
    }
}
