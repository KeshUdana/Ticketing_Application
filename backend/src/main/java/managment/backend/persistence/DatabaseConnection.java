package managment.backend.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL="jdbc:postgresql://localhost:5432/TicketingApplication";
    private static final String USERNAME="postgres";
    private static final String PASSWORD="keshawa";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
