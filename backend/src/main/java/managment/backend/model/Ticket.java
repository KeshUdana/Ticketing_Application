package managment.backend.model;
import jakarta.persistence.*;

@Entity
@Table(name = "Ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String ticketID;

    @Column(name = "Price")
    private double ticketPrice;

    @Column(name = "Type")
    private String ticketType;

    @Column(name = "Timestamp")
    private String timeStamp;


    public Ticket() {}
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
