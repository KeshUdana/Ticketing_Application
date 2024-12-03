package managment.backend.repository;

import managment.backend.persistence.DatabaseConnection;
import managment.backend.persistence.TicketSales;

import java.sql.*;

public class TicketSaleRepository {
    private Connection connection;

    public TicketSaleRepository() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    public void save(TicketSales ticketSales) {
        String query = "INSERT INTO ticket_sales (ticket_id,vendor_id,transaction_time,ticket_price,ticket_type,user_id) VALUES (?, ?, ?, ?, ?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1,ticketSales.getTicket().getTicketID());
            statement.setString(2, ticketSales.getVendor().getVendorID());
            statement.setTimestamp(3, Timestamp.valueOf(ticketSales.getTransactionTime()));
            statement.setDouble(4, ticketSales.getTicketPrice());
            statement.setString(5, ticketSales.getTicketType());
            statement.setString(6,ticketSales.getUser().getUserID());




            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("TicketSale saved to database.");
            }
        } catch (SQLException e) {
            System.err.println("Error saving TicketSale: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
