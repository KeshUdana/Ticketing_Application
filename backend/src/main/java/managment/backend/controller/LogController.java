package managment.backend.controller;


import managment.backend.model.LogEntry;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    @GetMapping("/stream")
    public SseEmitter streamLogs() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        // Dis for Simulating real-time log generation
        new Thread(() -> {
            try {
                for (long i = 0; i < 10; i++) {  // Use long for the loop variable
                    LogEntry logEntry = new LogEntry(
                            "2024-12-06T12:34:56",
                            "INFO",
                            i,  //
                            "Status-" + i
                    );
                    emitter.send(logEntry, MediaType.APPLICATION_JSON);
                    Thread.sleep(1000);
                }
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }
}


