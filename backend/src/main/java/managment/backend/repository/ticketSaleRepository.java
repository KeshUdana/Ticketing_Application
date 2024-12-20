package managment.backend.repository;

import managment.backend.persistence.DatabaseConnection;
import managment.backend.persistence.TicketSales;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.UUID;

@Repository
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
        String query = "INSERT INTO ticket_sales (ticket_id, ticket_type, ticket_price, transaction_time, vendor_id, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        boolean initialAutoCommit = true;

        try {
            // Save the initial auto-commit state
            initialAutoCommit = connection.getAutoCommit();
            if (initialAutoCommit) {
                connection.setAutoCommit(false); // Temporarily disable auto-commit
            }

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setObject(1, UUID.fromString(ticketSales.getTicket().getTicketID()));
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

                // Commit only if autoCommit was disabled
                if (!initialAutoCommit) {
                    connection.commit();
                }
            } catch (SQLException e) {
                // Rollback only if autoCommit was disabled
                if (!initialAutoCommit) {
                    connection.rollback();
                }
                System.err.println("Error saving TicketSale: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                // Restore the original auto-commit state
                connection.setAutoCommit(initialAutoCommit);
            } catch (SQLException e) {
                System.err.println("Error restoring auto-commit: " + e.getMessage());
            }
        }
    }

}