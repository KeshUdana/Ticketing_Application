package Startup;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@RestController
@RequestMapping(value = "/events")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/counts")
    public SseEmitter streamThreadCounts() {
        SseEmitter emitter = new SseEmitter();
        startAsyncTask(emitter);
        return emitter;
    }

    @Async
    public void startAsyncTask(SseEmitter emitter) {
        try {
            while (true) {
                emitter.send(Map.of(
                        "producerCount", ticketService.getProducerCount(),
                        "consumerCount", ticketService.getConsumerCount()
                ));
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
    }

}
