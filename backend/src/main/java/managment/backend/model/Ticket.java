package managment.backend.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ticketID; // Changed to private (encapsulation best practice)

    @Column(name = "Price")
    private double ticketPrice; // Changed to private

    @Column(name = "Type")
    private String ticketType; // Changed to private

    @Column(name = "Timestamp")
    private LocalDateTime timeStamp; // Changed to private

    // Default constructor (required by JPA)
    public Ticket(long id, double price, String type, String timeStamp) {}

    // Parameterized constructor
    public Ticket(long ticketID,double ticketPrice,String ticketType,LocalDateTime timeStamp) {
        this.ticketID=ticketID;
        this.ticketPrice = ticketPrice;
        this.ticketType =ticketType;
        this.timeStamp = LocalDateTime.now(); // Automatically sets the current time
    }



    // Getters
    public double getTicketID() {
        return ticketID;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public String getTicketType() {
        return ticketType;
    }
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    //Setters
    public void setTicketID(Long ticketID){
        this.ticketID=ticketID;
    }
    public void setTicketPrice(double price){
        this.ticketPrice=ticketPrice;
    }
    public void setTicketType(String ticketType){
        this.ticketType=ticketType;
    }
    public void setTimeStamp(LocalDateTime timeStamp){
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
