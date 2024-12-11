package Startup;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/events")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/counts")
    public Map<String, Integer> getThreadCounts() {
        Map<String, Integer> counts = new HashMap<>();
        counts.put("ProducerCount",ticketService.getProducerCount());
        counts.put("ConsumerCount", ticketService.getConsumerCount());
        return counts;
    }
}
