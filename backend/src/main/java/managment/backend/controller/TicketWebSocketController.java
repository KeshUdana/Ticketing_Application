package managment.backend.controller;

import managment.backend.model.Ticket;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class TicketWebSocketController {
    private SimpMessagingTemplate messagingTemplate;
    public TicketWebSocketController(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate=messagingTemplate;
    }
    public void sendNewTicket(Ticket ticket){
        messagingTemplate.convertAndSend("/topic/tickets",ticket);
    }
}

