package managment.backend.model;

import jakarta.persistence.*;


@Entity
@Table(name="Ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticketID;
    @Column(name="Price")
    private double ticketPrice;
    @Column(name="Availability")
    private boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "VendorID")//Foreign Key
    private Vendor vendor;

    //Tickets Constructors
    public Ticket(double ticketPrice,boolean isAvailable,Vendor vendor){
        this.ticketPrice=ticketPrice;
        this.isAvailable=isAvailable;
        this.vendor=vendor;
    }

    public Ticket() {}//Empty constructor for database
    // Required by JPA: JPA needs a no-argument constructor to instantiate the entity during
    // persistence operations (e.g., retrieving data from the database).


    //Getters
    public int getTicketID(){return ticketID;}
   public double getTicketPrice(){return ticketPrice;}
    public boolean getAvailability(){return isAvailable;}

    //Setters
    public void setTicketID(int ticketID){
        this.ticketID=ticketID;
    }
    public void setTicketPrice(double ticketPrice){
        this.ticketPrice=ticketPrice;
    }
    public void setAvailability(boolean isAvailable){
        this.isAvailable=isAvailable;
    }
    @Override
    public String toString() {
        return "Ticket{" +
                "ticketID=" + ticketID +
                ", ticketPrice=" + ticketPrice +
                ", status=" + (isAvailable ? "AVAILABLE" : "RETRIEVED") +
                '}';
    }

}
