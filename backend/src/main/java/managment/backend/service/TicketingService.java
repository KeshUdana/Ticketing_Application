package managment.backend.service;

import managment.backend.model.TicketPool;
import managment.backend.service.VendorService;
import org.springframework.stereotype.Service;
import managment.backend.service.UserService;

@Service
public class TicketingService {
    private final VendorService vendorService;
    private final UserService userService;
    private final TicketPool ticketPool;

    public TicketingService(VendorService vendorService, UserService userService, TicketPool ticketPool) {
        this.vendorService = vendorService;
        this.userService = userService;
        this.ticketPool= ticketPool;
    }

    public void startSystem() {
        System.out.println("System starting...");
        userService.startCustomerThreads(); // Start customer threads
    }

    public void stopSystem() {
        System.out.println("System stopping...");
        ticketPool.clear(); // Stop ticketing operations
    }

}
