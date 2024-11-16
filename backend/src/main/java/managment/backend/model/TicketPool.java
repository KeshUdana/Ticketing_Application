package managment.backend.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TicketPool {
    private final BlockingQueue<Ticket> ticketQueue;

    public TicketPool(int capacity) {
        ticketQueue = new LinkedBlockingQueue<>(capacity);  // Set max ticket pool capacity
    }

    public void addTicket(Ticket ticket) throws InterruptedException {
        ticketQueue.put(ticket);  // Adds a ticket to the pool (vendor releasing a ticket)
    }

    public Ticket retrieveTicket() throws InterruptedException {
        return ticketQueue.take();  // Retrieves a ticket from the pool (user buying a ticket)
    }

    public int getAvailableTicketCount() {
        return ticketQueue.size();
    }
}
