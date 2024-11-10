package Startup;

public class SystemConfig {
    private int totalTickets; //total tickets in System
    private double vendorReleaseRate;
    private double userRetrievalRate;
    private int maxTicketCapacity;//maximum tickets in System

    //Getters
    public int getTotalTickets() {
        return totalTickets;
    }
    public double getVendorReleaseRate() {
        return vendorReleaseRate;
    }
    public double getUserRetrievalRate() {
        return userRetrievalRate;
    }
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    //Setters
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }
    public void setVendorReleaseRate(double vendorReleaseRate) {
        this.vendorReleaseRate = vendorReleaseRate;
    }
    public void setUserRetrievalRate(double userRetrievalRate) {
        this.userRetrievalRate = userRetrievalRate;
    }
    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }
}

