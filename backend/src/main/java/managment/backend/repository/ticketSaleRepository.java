package managment.backend.repository;

import managment.backend.persistence.DatabaseConnection;
import managment.backend.persistence.TicketSales;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.UUID;

@Repository
public class TicketSaleRepository {
    private Connection connection;

    public TicketSaleRepository() {        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    public void save(TicketSales ticketSales) {
        String query = "INSERT INTO ticket_sales (ticket_id, ticket_type, ticket_price, transaction_time, vendor_id, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            // Set auto-commit to false
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setObject(1,UUID.fromString( ticketSales.getTicket().getTicketID())); // Assuming UUID is being handled properly
                statement.setString(2, ticketSales.getTicketType());
                statement.setDouble(3, ticketSales.getTicketPrice());
                statement.setTimestamp(4, Timestamp.valueOf(ticketSales.getTransactionTime()));
                statement.setString(5, ticketSales.getVendor().getVendorID());
                statement.setString(6, ticketSales.getUser().getUserID());

                // Execute the insert
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("TicketSale saved to database.");
                }

                // Commit the transaction
                connection.commit();
            } catch (SQLException e) {
                // Rollback in case of an error
                connection.rollback();
                System.err.println("Error saving TicketSale: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            // Handle connection error or setting auto-commit
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                // Restore the default autocommit mode
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error restoring auto-commit: " + e.getMessage());
            }
        }
    }
}