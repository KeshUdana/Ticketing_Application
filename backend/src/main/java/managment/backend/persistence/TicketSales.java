package managment.backend.persistence;

import jakarta.persistence.*;
import managment.backend.model.Ticket;
import managment.backend.model.User;
import managment.backend.model.Vendor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_sales")
public class TicketSales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salesID;

    @ManyToOne
    @JoinColumn(name = "ticket_id", referencedColumnName = "ticketID")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "vendor_id", referencedColumnName = "vendorID") // Fixed naming convention
    private Vendor vendor;

    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;

    @Column(name = "ticket_price")
    private Double ticketPrice;

    @Column(name = "ticket_type")
    private String ticketType;

    // Constructor with all fields
    public TicketSales(Ticket ticket, User user, Vendor vendor, LocalDateTime transactionTime, Double ticketPrice, String ticketType) {
        this.ticket = ticket;
        this.user = user;
        this.vendor = vendor;
        this.transactionTime = transactionTime;
        this.ticketPrice = ticketPrice;
        this.ticketType = ticketType;
    }

    // Default constructor
    public TicketSales() {
        this.transactionTime = LocalDateTime.now(); // Set default value for transaction time
    }

    // Getters and setters
    public Long getSalesID() {
        return salesID;
    }

    public void setSalesID(Long salesID) {
        this.salesID = salesID;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public void setUser(String userID) {
    }
}
