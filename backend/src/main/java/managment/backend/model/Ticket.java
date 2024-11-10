package managment.backend.model;

import jakarta.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name="Ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger ticketID;
    @Column(name="Price")
    private double ticketPrice;

    //Getters
    public BigInteger getTicketID(){return ticketID;}
   public double getTicketPrice(){return ticketPrice;}

    //Setters
    public void setTicketID(BigInteger ticketID){
        this.ticketID=ticketID;
    }
}
