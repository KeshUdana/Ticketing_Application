package managment.backend.service;

import Startup.SystemConfig;
import Startup.TicketingCLI;
import managment.backend.model.Ticket;
import managment.backend.model.TicketPool;

import managment.backend.model.User;
import managment.backend.model.Vendor;
import managment.backend.persistence.TicketSales;
import managment.backend.repository.TicketSaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


@Service
public class ProducerService  {
    private final TicketPool ticketPool;
    private final SystemConfig config;
    private TicketSaleRepository ticketSaleRepository;
    private boolean systemRunning;
    private List<Thread> consumerThreads = new ArrayList<>();
    private static int consumerCount = 0;

    @Autowired
    public ProducerService(TicketPool ticketPool, SystemConfig config, TicketSaleRepository ticketSaleRepository) {
        if (!ticketPool.isInitialized()) {
            throw new IllegalStateException("TicketPool must be initialized before creating ProducerService.");
        }
        this.ticketPool = ticketPool;
        this.config=config;
        this.ticketSaleRepository=ticketSaleRepository;
        this.systemRunning = true; // Start with the producer running

    }}

