package managment.backend.controller;

import com.google.gson.Gson;
import managment.backend.model.LogEntry;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import com.google.gson.reflect.TypeToken;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private static final String JSON_LOG_FILE = "logs.json";
    private final Gson gson = new Gson();

    @GetMapping
    public List<LogEntry> getAllLogs() throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(JSON_LOG_FILE)));
        return gson.fromJson(json, new TypeToken<List<LogEntry>>() {}.getType());
    }
}

