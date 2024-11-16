package Startup;

public class SystemConfig {
    private int totalTickets; //total tickets in System
    private int vendorReleaseRate;
    private int userRetrievalRate;
    private int maxTicketCapacity;//maximum tickets in System



    //Getters
    public int getTotalTickets(){return totalTickets;}

    public int getVendorReleaseRate() {
        return vendorReleaseRate;
    }
    public int getUserRetrievalRate() {
        return userRetrievalRate;
    }
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    //Setters
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }
    public void setVendorReleaseRate(int vendorReleaseRate) {
        this.vendorReleaseRate = vendorReleaseRate;
    }
    public void setUserRetrievalRate(int userRetrievalRate) {
        this.userRetrievalRate = userRetrievalRate;
    }
    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }
}

