import managment.backend.persistence.TicketSales;
import managment.backend.repository.TicketSaleRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcTicketSaleRepository implements TicketSaleRepository {

    private final String url = "jdbc:postgresql://localhost:5432/your_database";
    private final String user = "your_username";
    private final String password = "your_password";

    @Override
    public void save(TicketSales ticketSales) {
        String sql = "INSERT INTO ticket_sales (ticket_id, user_id, vendor_id, transaction_time, ticket_price, ticket_type) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ticketSales.getTicket().getTicketID());
            stmt.setString(2, ticketSales.getUser().getUserID());
            stmt.setString(3, ticketSales.getVendor().getVendorID());
            stmt.setTimestamp(4, Timestamp.valueOf(ticketSales.getTransactionTime()));
            stmt.setDouble(5, ticketSales.getTicketPrice());
            stmt.setString(6, ticketSales.getTicketType());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<TicketSales> findAll() {
        List<TicketSales> salesList = new ArrayList<>();
        String sql = "SELECT * FROM ticket_sales";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                TicketSales sales = new TicketSales();
                sales.setSalesID(rs.getLong("sales_id"));
                sales.setTransactionTime(rs.getTimestamp("transaction_time").toLocalDateTime());
                sales.setTicketPrice(rs.getDouble("ticket_price"));
                sales.setTicketType(rs.getString("ticket_type"));
                // Set Ticket, User, Vendor objects if needed
                salesList.add(sales);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salesList;
    }
}
