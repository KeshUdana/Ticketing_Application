package managment.backend.service;
import managment.backend.model.Ticket;

import java.util.concurrent.BlockingQueue;

public class ConsumerService implements Runnable{
    private final BlockingQueue<Ticket> ticketQueue;

    public ConsumerService(BlockingQueue<Ticket> ticketQueue) {
        this.ticketQueue = ticketQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Ticket purchasedTicket = ticketQueue.take(); // Retrieve a ticket from the queue
                System.out.println("Consumed: " + purchasedTicket);
                Thread.sleep(1500); // Simulate time taken to purchase a ticket
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Ticket consumption interrupted.");
        }
    }
}
