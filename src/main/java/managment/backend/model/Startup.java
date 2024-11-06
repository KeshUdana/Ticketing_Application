package managment.backend.model;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class Startup implements CommandLineRunner {
    private final SystemConfig config;

    public Startup(SystemConfig config) {
        this.config = config;
    }

    @Override
    public void run(String[] args) {
        Scanner input = new Scanner(System.in);

        // Input 1
        System.out.println("Enter Maximum Tickets for the system: ");
        int maxTicketCapacity = input.nextInt();
        config.setMaxTicketCapacity(maxTicketCapacity);

        System.out.println("Enter maximum possible tickets per Vendor:");
        int totalTickets = input.nextInt();
        config.setTotalTickets(totalTickets);

        System.out.println("Enter Vendor Ticket release Rate: ");
        double vendorReleaseRate = input.nextDouble();
        config.setVendorReleaseRate(vendorReleaseRate);

        System.out.println("Enter Customer retrieval Rate: ");
        double userRetrievalRate = input.nextDouble();
        config.setUserRetrievalRate(userRetrievalRate);

        // Confirmation output
        System.out.println("\nConfiguration Set:");
        System.out.println("Max Ticket Capacity: " + config.getMaxTicketCapacity());
        System.out.println("Total Tickets: " + config.getTotalTickets());
        System.out.println("Vendor Release Rate: " + config.getVendorReleaseRate());
        System.out.println("User Retrieval Rate: " + config.getUserRetrievalRate());
    }
}
