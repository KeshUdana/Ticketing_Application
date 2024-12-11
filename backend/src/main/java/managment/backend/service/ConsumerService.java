package managment.backend.service;

import managment.backend.model.Ticket;
import managment.backend.model.TicketPool;
import managment.backend.model.User;
import managment.backend.repository.TicketSaleRepository;
import Startup.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ConsumerService  {

    private final TicketPool ticketPool;
    // private final User user;
    private final TicketSaleRepository ticketSaleRepository;
    public SystemConfig config;
    private boolean systemRunning;

    // Constructor
    @Autowired
    public ConsumerService(TicketPool ticketPool, SystemConfig config, TicketSaleRepository ticketSaleRepository) {
        if (!ticketPool.isInitialized()) {
            throw new IllegalStateException("TicketPool must be initialized before creating ProducerService.");
        }
        this.ticketPool = ticketPool;
        this.config = config;
        this.ticketSaleRepository = ticketSaleRepository;
        this.systemRunning = true; // Start with the producer running
    }}