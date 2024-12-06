// LogController.java
package managment.backend.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import managment.backend.model.LogEntry;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private static final String JSON_LOG_FILE = "logs.json"; // path to your JSON log file
    private final Gson gson = new Gson();

    // Existing method to fetch all logs
    @GetMapping
    public List<LogEntry> getAllLogs() throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(JSON_LOG_FILE)));
        return gson.fromJson(json, new TypeToken<List<LogEntry>>() {}.getType());
    }

    // SSE endpoint to stream logs
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<LogEntry> streamLogs() {
        return Flux.create(sink -> {
            // Example: Emit logs periodically (you can replace this with actual log generation or reading logic)
            try {
                while (true) {
                    List<LogEntry> logs = getAllLogs();  // Fetch latest logs (or stream from a real-time source)
                    for (LogEntry log : logs) {
                        sink.next(log);  // Push log to the client
                    }
                    Thread.sleep(1000);  // Wait 1 second before sending the next batch of logs
                }
            } catch (InterruptedException e) {
                sink.error(e); // In case of an error
            }
        });
    }
}
