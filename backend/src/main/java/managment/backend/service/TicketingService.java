package managment.backend.service;

import managment.backend.model.TicketPool;
import org.springframework.stereotype.Service;

@Service
public class TicketingService {

    private final VendorService vendorService;
    private final UserService userService;
    private final TicketPool ticketPool;

    public TicketingService(VendorService vendorService, UserService userService, TicketPool ticketPool) {
        this.vendorService = vendorService;
        this.userService = userService;
        this.ticketPool = ticketPool;
    }

    // Start the system and initiate both vendor and user threads
    public void startSystem() {
        System.out.println("System starting...");

        // Log initial ticket pool status
        System.out.println("Initial ticket pool size: " + ticketPool.getTicketCount());

        // Start vendor threads
        vendorService.startVendorThreads(); // Assuming VendorService has a method to start vendor threads

        // Start user threads for customers
        userService.startCustomerThreads();

        // Monitor ticket pool after system starts
        new Thread(() -> {
            while (ticketPool.getTicketCount() > 0) {
                try {
                    Thread.sleep(1000); // Log ticket pool status every second
                    System.out.println("Current ticket pool size: " + ticketPool.getTicketCount());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("All tickets have been sold.");
        }).start();
    }

    // Stop the system and stop both vendor and user threads
    public void stopSystem() {
        System.out.println("System stopping...");

        // Stop user threads (optional if needed, depending on how the threads are managed)
        userService.stopCustomerThreads();  // Assuming a method exists to stop customer threads

        // Stop vendor threads (optional if needed)
        vendorService.stopVendorThreads();  // Assuming a method exists to stop vendor threads

        /*Clear the ticket pool to reset ticket availability (optional)
        ticketPool.clear(); // Clear remaining tickets in the pool*/
    }
}