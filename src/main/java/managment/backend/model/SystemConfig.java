package managment.backend.model;

import org.springframework.stereotype.Component;

@Component
public class SystemConfig {
    private int totalTickets; //Can change
    private int vendorReleaseRate;
    private int userRetrievalRate;
    private int maxTicketCapacity;
    private int maxThreads;//Not sure about this bit ,but keeping for later

    //Getters
    public int getMaxTicketCapacity(){ //No setter assigned as this is immutable in this system
        return maxTicketCapacity;
    }

    public int getTotalTicket() {
        return totalTickets;
    }

    public int getVendorReleaseRate(){
        return vendorReleaseRate;
    }
    public int getUserRetrievalRate(){
        return userRetrievalRate;
    }

    //Setters
    public void setTotalTickets(){
        this.totalTickets=totalTickets; //Subject to change under Vendors
    }
    public void setVendorReleaseRate(){
        this.vendorReleaseRate=vendorReleaseRate;
    }
    public void setUserRetrievalRate(){
        this.userRetrievalRate=userRetrievalRate;//Subject to change by Vendors as well
    }


}