package managment.backend.model;

import Startup.SystemConfig;

import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


@Component
public class TicketPool {

    private final BlockingQueue<Ticket> ticketQueue;
    // Constructor
    public TicketPool() {

        this.ticketQueue = new LinkedBlockingQueue<>(SystemConfig.getTotalTickets()); // Initialize with fixed capacity
    }

    // Add ticket to the pool
    public void addTicket(Ticket ticket) throws InterruptedException {
        ticketQueue.put(ticket); // Blocks if the queue is full
        System.out.println("Ticket added: " + ticket);
    }

    // Retrieve ticket from the pool
    public Ticket retrieveTicket() throws InterruptedException {
        Ticket ticket = ticketQueue.take(); // Blocks if the queue is empty
        System.out.println("Ticket retrieved: " + ticket);
        return ticket;
    }

    // Get the current number of tickets in the pool
    public int getCurrentSize() {
        return ticketQueue.size();
    }

    // Check if the pool is full
    public boolean isFull() {
        return ticketQueue.remainingCapacity() == 0;
    }

    // Check if the pool is empty
    public boolean isEmpty() {
        return ticketQueue.isEmpty();
    }
}

