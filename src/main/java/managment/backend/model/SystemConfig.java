package managment.backend.model;

import org.springframework.stereotype.Component;

@Component
public class SystemConfig {
    private int totalTickets; // Can change
    private double vendorReleaseRate;
    private double userRetrievalRate;
    private int maxTicketCapacity;
    private int maxThreads; // Not sure about this bit, but keeping for later

    // Getters
    public int getMaxTicketCapacity() { // No setter assigned as this is immutable in this system
        return maxTicketCapacity;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public double getVendorReleaseRate() {
        return vendorReleaseRate;
    }

    public double getUserRetrievalRate() {
        return userRetrievalRate;
    }

    // Setters
    public void setMaxTicketCapacity(int maxTicketCapacity) { // This should be immutable later
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets; // Subject to change under Vendors
    }

    public void setVendorReleaseRate(double vendorReleaseRate) {
        this.vendorReleaseRate = vendorReleaseRate;
    }

    public void setUserRetrievalRate(double userRetrievalRate) {
        this.userRetrievalRate = userRetrievalRate; // Subject to change by Vendors as well
    }
}
