package managment.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class JsonController {
    @GetMapping("/transactions")
    public List<Object> getTransactions() throws Exception {

        String filePath = Paths.get("../../../../../thread_visualize.json").toAbsolutePath().toString();
        System.out.println("File path being used: " + filePath);

        String json = Files.readString(Paths.get(filePath));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, List.class);
    }
}
