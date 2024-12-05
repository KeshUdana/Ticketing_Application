package managment.backend.controller;

import managment.backend.model.Ticket;
import managment.backend.repository.TicketSaleRepository;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class TicketRestController {
    private final TicketSaleRepository ticketSaleRepository;
    public TicketRestController(TicketSaleRepository ticketSaleRepository){
        this.ticketSaleRepository=ticketSaleRepository;
    }
    @GetMapping
    public List<Ticket> getAllTickets(){
        return ticketSaleRepository.findAll();
    }
}
