package managment.backend.model;

import Startup.SystemConfig;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TicketPool {
    private BlockingQueue<Ticket> ticketQueue;

    public <E> TicketPool(ArrayBlockingQueue<E> es) {
    }

    public void initialize() {
        if (ticketQueue == null) {
            ticketQueue = new ArrayBlockingQueue<>(SystemConfig.getTotalTickets());
        }
    }

    public void addTicket(Ticket ticket) throws InterruptedException {
        if (ticketQueue == null) {
            throw new IllegalStateException("TicketPool not initialized yet.");
        }
        ticketQueue.put(ticket);
        System.out.println("Ticket added: " + ticket);
    }

    public Ticket retrieveTicket() throws InterruptedException {
        if (ticketQueue == null) {
            throw new IllegalStateException("TicketPool not initialized yet.");
        }
        Ticket ticket = ticketQueue.take();
        System.out.println("Ticket retrieved: " + ticket);
        return ticket;
    }
}
