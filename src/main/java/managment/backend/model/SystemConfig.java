package managment.backend.model;

import org.springframework.stereotype.Component;

@Component
public class SystemConfig {
    private int totalTickets; //Can change
    private int vendorReleaseRate;
    private int userRetrievalRate;
    private int maxTicketCapacity;
    private int maxThreads;
}
