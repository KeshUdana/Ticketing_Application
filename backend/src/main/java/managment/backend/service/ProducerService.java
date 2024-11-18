package managment.backend.service;
import Startup.SystemConfig;
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
                // Create a new ticket with a unique ID and a random price
                double price = Math.random() * 100; // Random price between 0 and 100
                Ticket newTicket = new Ticket(ticketIdCounter++, price); // Increment ID for each new ticket
                ticketQueue.put(newTicket); // Add the ticket to the queue
                System.out.println("Added: " + newTicket);
                Thread.sleep(1000/ SystemConfig.getVendorReleaseRate()); // Simulate time taken to produce a ticket
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Vendor interrupted.");
        }
    }
}
