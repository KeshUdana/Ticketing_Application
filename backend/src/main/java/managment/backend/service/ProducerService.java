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
    private boolean systemRunning; // Flag to control when to stop the producer

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
/*
        //Initialize vendor details
        this.vendor=new Vendor();
        vendor.setVendorID(UUID.randomUUID().toString().substring(0,5));
        vendor.setVendorUsername("Vendor-name");
        vendor.setVendorEmail("vendor@gmail.com");
        vendor.setVendorPassword(UUID.randomUUID().toString());*/
    }}

/*    // Method to stop the producer
   public void stopProducing() {
        systemRunning = false;
    }

    @Override
    public void run() {
        try {
            while (systemRunning) {
                if (ticketPool.getTicketsProduced() >= config.getTotalTickets()) {
                    System.out.println("Total tickets produced. Stopping producer: " + vendor.getVendorID());
                    stopProducing();
                    break;
                }
                for (int i = 0; i < config.getTotalTickets(); i++) {

                    //Generate a new user

                    User user = new User();
                    user.setUserID(UUID.randomUUID().toString().substring(0, 5));//UUID woth just 5 character
                    user.setUserUsername("USER-name");
                    user.setUserEmail("user@gmail.com");
                    user.setUserPassword(UUID.randomUUID().toString());


                    Ticket ticket = new Ticket();
                    ticket.setTicketID(UUID.randomUUID().toString());//Unique UUID
                    ticket.setTicketType(Math.random() < 0.5 ? "VIP" : "Regular");
                    ticket.setTicketPrice(ticket.getTicketType() == "VIP" ? 1000.00 : 500.0);
                    ticket.setTimeStamp(java.time.LocalDateTime.now().toString());

                    //Using gnerateTicektSale mthod instead as encapsualtion
                    TicketSales sale = generateTicketSale(ticket, vendor, user);
                    ticketSaleRepository.save(sale);
                    System.out.println("Transaction saved for Ticket ID: " + ticket.getTicketID());
                    ticketPool.addTicket(ticket);
                    ticketPool.incrementTicketsProduced();


                    /////////////////New COnumer thread creation
                    ConsumerService consumerService=new ConsumerService(ticketPool,config,ticketSaleRepository);
                    Thread consumerThread=new Thread(consumerService);

                    consumerThreads.add(consumerThread);
                    consumerThread.start();
                    consumerCount++;
                    System.out.println("Consumer "+consumerThread.getId()+" Started");

                    System.out.println("========== Ticket Added ==========");
                    System.out.printf("Vendor ID: %-15s%n", vendor.getVendorID());
                    System.out.printf("Ticket ID: %-15s%n", ticket.getTicketID());
                    System.out.printf("Price: %-15.2f%n", ticket.getTicketPrice());
                    System.out.printf("Type: %-15s%n", ticket.getTicketType());


                    Thread.sleep(1000 / config.getVendorReleaseRate());
                }
            }
            } catch(InterruptedException e){
                System.out.println("Producer thread interrupted.");
                Thread.currentThread().interrupt();
            }


    }
    public static int pseudoConsumerCount(){return consumerCount;}



    private TicketSales generateTicketSale(Ticket ticket, Vendor vendor, User user){
    TicketSales sale=new TicketSales();
    sale.setTicket(ticket);
    sale.setVendor(vendor);
    sale.setUser(user);
    sale.setTransactionTime(LocalDateTime.now());
    sale.setTicketPrice(ticket.getTicketPrice());
    sale.setTicketType(ticket.getTicketType());
    return sale;
    }
}

*/