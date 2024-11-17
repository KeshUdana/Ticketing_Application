package managment.backend.service;

import managment.backend.model.TicketPool;
import org.springframework.stereotype.Service;

@Service
public class TicketingService {

    private final VendorService vendorService;
    private final UserService userService;
    private TicketPool ticketPool; // Injected shared pool for tickets
    private Thread monitorThread; // Thread to monitor the ticket pool

    public TicketingService(VendorService vendorService, UserService userService) {
        this.vendorService = vendorService;
        this.userService = userService;
    }

    // Start the system and initiate both vendor and user threads
    public void startSystem() {
        System.out.println("System starting...");

        // Start vendor threads
        vendorService.startVendorThreads();

        // Start user threads for customers
        userService.startCustomerThreads();

        // Monitor ticket pool after system starts
        monitorThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("Current ticket pool size: " + ticketPool.getTicketCount());
                    Thread.sleep(1000); // Log ticket pool status every second
                }
            } catch (InterruptedException e) {
                System.out.println("Monitoring thread interrupted. Stopping monitoring...");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println("Error in monitoring thread: " + e.getMessage());
            }
        });
        monitorThread.start();
    }

    // Stop the system and stop both vendor and user threads
    public void stopSystem() {
        System.out.println("System stopping...");

        // Stop user threads
        userService.stopCustomerThreads();

        // Stop vendor threads
        vendorService.stopVendorThreads();

        // Stop the monitoring thread
        if (monitorThread != null && monitorThread.isAlive()) {
            monitorThread.interrupt();
            try {
                monitorThread.join(); // Wait for the thread to terminate
            } catch (InterruptedException e) {
                System.out.println("Interrupted while waiting for monitoring thread to stop.");
                Thread.currentThread().interrupt();
            }
        }

        // Optional: Clear the ticket pool (if reset functionality is needed)
        /*synchronized (ticketPool) {
            ticketPool.clear(); // Clear remaining tickets in the pool
        }*/
    }
}
