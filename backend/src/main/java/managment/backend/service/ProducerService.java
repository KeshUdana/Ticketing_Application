package managment.backend.service;
import managment.backend.model.Ticket;

import java.util.concurrent.BlockingQueue;
public class ProducerService implements Runnable{
    private final BlockingQueue<Ticket> ticketQueue;
    private int ticketIdCounter = 1; // To generate unique ticket IDs

    public ProducerService(BlockingQueue<Ticket> ticketQueue) {
        this.ticketQueue = ticketQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Simulate creating a new ticket
                Ticket newTicket = new Ticket(ticketIdCounter++, Math.random() * 100); // Random price
                ticketQueue.put(newTicket); // Add ticket to the queue
                System.out.println("Produced: " + newTicket);
                Thread.sleep(1000); // Simulate time taken to produce a ticket
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Ticket production interrupted.");
        }
    }
}
