package Startup;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;


@RestController
@RequestMapping(value = "/events")
public class TicketController {
    private  TicketService ticketService;
    public TicketController(TicketService ticketService){
        this.ticketService=ticketService;
    }
    @GetMapping("/counts")
    public Map<String, Integer> getThreadCounts() {
        return Map.of(
                "producerCount", ticketService.getProducerCount(),
                "consumerCount", ticketService.getConsumerCount()
        );
    }
}

