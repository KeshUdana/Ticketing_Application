package Startup;

import org.springframework.stereotype.Service;

@Service
public class TicketService {
    private final TicketingCLI ticketCLI;

    public TicketService(TicketingCLI ticketCLI) {
        this.ticketCLI = ticketCLI;
    }

    public int getProducerCount() {
        return TicketingCLI.getProducerCount();
    }

    public int getConsumerCount() {
        return TicketingCLI.getConsumerCount();
    }
}


