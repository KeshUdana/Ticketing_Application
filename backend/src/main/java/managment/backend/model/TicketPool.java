package managment.backend.model;

import Startup.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TicketPool {

    private final BlockingQueue<Integer> ticketQueue;
    private final AtomicInteger ticketCounter;

    public TicketPool() {


        int totalTickets = SystemConfig.getTotalTickets();
        if (totalTickets <= 0) {
            throw new IllegalArgumentException("Ticket pool capacity must be greater than 0.");
        }

        this.ticketQueue = new LinkedBlockingQueue<>(totalTickets);
        this.ticketCounter = new AtomicInteger(0);

        for (int i = 0; i < totalTickets; i++) {
            ticketQueue.offer(i + 1);
        }
    }
}
