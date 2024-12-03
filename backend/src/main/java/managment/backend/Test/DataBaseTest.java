package managment.backend.Test;

import managment.backend.persistence.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class DataBaseTest {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
