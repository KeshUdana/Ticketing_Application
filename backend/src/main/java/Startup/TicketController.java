package Startup;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@RestController
public class TicketController { private BlockingQueue<String> updatesQueue = new LinkedBlockingQueue<>();
    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public void streamUpdates(HttpServletResponse response) throws IOException {
        System.out.println("Endpoint '/events' accessed!"); // Debug log to see if the connevtion s there

        response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
        PrintWriter writer = response.getWriter();


        while (!Thread.interrupted()) {
            try {
                String update = updatesQueue.take();
                writer.write("data: " + update + "\n\n");
                writer.flush();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}


