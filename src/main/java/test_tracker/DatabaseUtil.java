package test_tracker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseUtil {
    private static final String USER = "root";
    private static final String PASSWORD = "novell";
    private static final String DB_URL = "jdbc:mariadb://151.155.216.38:3306/test?user=" + USER + "&password=" + PASSWORD;

    private static final Logger LOGGER = Logger.getLogger(DatabaseUtil.class.getName());

    private DatabaseUtil() {}

    private static Statement createStatement(Connection conn) {
        Statement statement = null;

        try {
            statement = conn.createStatement();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Cannot create Statement: ", e);
        }

        return statement;
    }

    private static ResultSet createResultSet(Statement st, String query) {
        ResultSet resultSet = null;

        try {
            resultSet = st.executeQuery(query);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Cannot create ResultSet: " + query, e);
        }

        return resultSet;
    }

    public static ObservableList<String> getSections() {
        ObservableList<String> results = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement st = createStatement(conn);
            ResultSet rs = createResultSet(st, "SELECT sectionName FROM sections");

            if (rs != null) {
                while (rs.next()) {
                    results.add(rs.getString("sectionName"));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in getSections(): ", e);
        }

        return results;
    }

    public static ObservableList<String> getProducts() {
        ObservableList<String> results = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement st = createStatement(conn);
            ResultSet rs = createResultSet(st, "SELECT productName FROM products");

            if (rs != null) {
                while (rs.next()) {
                    results.add(rs.getString("productName"));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in getProducts(): ", e);
        }

        return results;
    }

    public static ResultSet getTests() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement st = createStatement(conn);
            return createResultSet(st, "SELECT * FROM tests");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in getTests(): ", e);
            return null;
        }
    }

    public static ObservableList<Row> getRows(int id) {
        ObservableList<Row> results = FXCollections.observableArrayList();

        try  {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement st = createStatement(conn);
            ResultSet rs = createResultSet(st, "SELECT * from servers where testID = " + id);

            if (rs != null) {
                while (rs.next()) {
                    results.add(new Row(rs.getString("description"), rs.getString("ip"), rs.getString("esxIP")));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in getRows(): ", e);
        }

        return results;
    }
}
