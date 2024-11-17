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

    @Autowired
    public TicketPool(ConfigService configService) {
        this.configService = configService;

        int totalTickets = configService.getConfig().getTotalTickets();
        if (totalTickets <= 0) {
            throw new IllegalArgumentException("Ticket pool capacity must be greater than 0.");
        }

        this.ticketQueue = new LinkedBlockingQueue<>(totalTickets);
        this.ticketCounter = new AtomicInteger(0);

        for (int i = 0; i < totalTickets; i++) {
            ticketQueue.offer(i + 1);
        }
    }

    public boolean addTicket() {
        if (ticketCounter.get() < ticketQueue.remainingCapacity() + ticketQueue.size()) {
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

    public int getTicketCount() {
        return ticketQueue.size();
    }
}
