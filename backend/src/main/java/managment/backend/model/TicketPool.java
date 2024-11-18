package managment.backend.model;

import Startup.SystemConfig;

import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


@Component
public class TicketPool {

    private  BlockingQueue<Ticket> ticketQueue;
    // Constructor
    public TicketPool(BlockingQueue<Ticket>ticketQueue) {
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

    // Getter
    public BlockingQueue<Ticket>getTicketQueue(){
        return ticketQueue;
    }

}

