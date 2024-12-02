package managment.backend.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String ticketID; // Changed to private (encapsulation best practice)

    @Column(name = "Price")
    private double ticketPrice; // Changed to private

    @Column(name = "Type")
    private String ticketType; // Changed to private

    @Column(name = "Timestamp")
    private String timeStamp; // Changed to private

    // Default constructor (required by JPA)
    public Ticket() {}

    // Parameterized constructor
    public Ticket(String ticketID,double ticketPrice,String ticketType,String timeStamp) {
        this.ticketID=ticketID;
        this.ticketPrice = ticketPrice;
        this.ticketType =ticketType;
        this.timeStamp = timeStamp; // Automatically sets the current time
    }



    // Getters
    public String getTicketID() {
        return ticketID;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public String getTicketType() {
        return ticketType;
    }
    public String getTimeStamp() {
        return timeStamp;
    }

    //Setters
    public void setTicketID(String ticketID){
        this.ticketID=ticketID;
    }
    public void setTicketPrice(double price){
        this.ticketPrice=ticketPrice;
    }
    public void setTicketType(String ticketType){
        this.ticketType=ticketType;
    }
    public void setTimeStamp(String timeStamp){
        this.timeStamp=timeStamp;
    }


    // Overriding toString for easy logging
    @Override
    public String toString() {
        return "Ticket{" +
                "ticketID=" + ticketID +
                ", ticketPrice=" + ticketPrice +
                ", ticketType='" + ticketType + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
