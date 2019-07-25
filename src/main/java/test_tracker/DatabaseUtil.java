package test_tracker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class DatabaseUtil {
    private static final String USER = "root";
    private static final String PASSWORD = "novell";
    private static final String DB_URL = "jdbc:mariadb://151.155.216.38:3306/test?user=" + USER + "&password=" + PASSWORD;

    private static final Logger LOGGER = Logger.getLogger(DatabaseUtil.class.getName());

    private DatabaseUtil() {
    }

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

    static ObservableList<Integer> getSectionIDs() {
        ObservableList<Integer> results = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement st = createStatement(conn);
            ResultSet rs = createResultSet(st, "SELECT id FROM sections");

            if (rs != null) {
                while (rs.next()) {
                    results.add(rs.getInt("id"));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in getSectionIDs(): ", e);
        }

        return results;
    }

    static ObservableList<String> getSections() {
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

    static ObservableList<Integer> getProductIDs() {
        ObservableList<Integer> results = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement st = createStatement(conn);
            ResultSet rs = createResultSet(st, "SELECT id FROM products");

            if (rs != null) {
                while (rs.next()) {
                    results.add(rs.getInt("id"));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in getProductIDs(): ", e);
        }

        return results;
    }

    static ObservableList<String> getProducts() {
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

    static ObservableList<Table> getTests() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement st = createStatement(conn);

            ObservableList<Table> listOfTablesAsObservableList = FXCollections.observableArrayList();

            // Create table objects
            ResultSet tables = createResultSet(st, "SELECT * FROM tests");
            if (tables != null) {
                try {
                    while (tables.next()) {
                        listOfTablesAsObservableList.add(new Table(
                                tables.getInt("id"),
                                tables.getString("testName"),
                                tables.getInt("section"),
                                tables.getInt("product"),
                                tables.getString("notes"),
                                tables.getInt("showNotes")
                        ));
                    }
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "", e);
                }
            }

            return listOfTablesAsObservableList;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in getTests(): ", e);
            return null;
        }
    }

    static ObservableList<Row> getRows(int id) {
        ObservableList<Row> results = FXCollections.observableArrayList();

        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement st = createStatement(conn);
            ResultSet rs = createResultSet(st, "SELECT * from servers where testID = " + id);

            if (rs != null) {
                while (rs.next()) {
                    results.add(new Row(rs.getInt("id"), rs.getString("description"), rs.getString("ip"), rs.getString("esxIP"), rs.getInt("testID")));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in getRows(): ", e);
        }

        return results;
    }

    static void addSection(String sectionName) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement st = createStatement(conn);
            if (st != null) {
                st.executeQuery("INSERT INTO sections SET sectionName = '" + sectionName + "';");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in updateRow(): ", e);
        }
    }

    static void addProduct(String productName) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement st = createStatement(conn);
            if (st != null) {
                st.executeQuery("INSERT INTO products SET productName = '" + productName + "';");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in updateRow(): ", e);
        }
    }

    static void addTest(String testName, int sectionID, int productID) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement st = createStatement(conn);
            if (st != null) {
                st.executeQuery("INSERT INTO tests SET testName = '" + testName + "', section = '" + sectionID + "', product = '" + productID + "', showNotes = 0;");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in updateRow(): ", e);
        }
    }

    static void addRow(String description, String ip, String esxIp, int testID) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement st = createStatement(conn);
            if (st != null) {
                st.executeQuery("INSERT INTO servers SET description = '" + description + "', ip = '" + ip + "', esxIP = '" + esxIp + "', testID = '" + testID + "';");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in updateRow(): ", e);
        }
    }

    static void updateRow(int id, String description, String ip, String esxIp) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement st = createStatement(conn);
            if (st != null) {
                st.executeQuery("UPDATE servers SET description = '" + description + "', ip = '" + ip + "', esxIP = '" + esxIp + "' WHERE id = " + id + ";");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in updateRow(): ", e);
        }
    }

    static void updateCell(int id, String col, String changedVal) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement st = createStatement(conn);
            if (st != null) {
                st.executeQuery("UPDATE servers SET " + col + " = '" + changedVal + "' WHERE id = " + id + ";");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in updateRow(): ", e);
        }
    }

    static void updateTableTitle(int id, String changedVal) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement st = createStatement(conn);
            if (st != null) {
                st.executeQuery("UPDATE tests SET testName = '" + changedVal + "' WHERE id = " + id + ";");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in updateTableTitle(): ", e);
        }
    }

    static void updateNotes(int id, String changedVal) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement st = createStatement(conn);
            if (st != null) {
                st.executeQuery("UPDATE tests SET notes = '" + changedVal + "' WHERE id = " + id + ";");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in updateNotes(): ", e);
        }
    }

    static void updateShowNotes(int id, int changedVal) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement st = createStatement(conn);
            if (st != null) {
                st.executeQuery("UPDATE tests SET showNotes = '" + changedVal + "' WHERE id = " + id + ";");
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in updateNotes(): ", e);
        }
    }

    static void deleteProduct(int id) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement st = createStatement(conn);
            if (st != null) {
                deleteTestsByProduct(id);
                st.executeQuery("DELETE FROM products WHERE id = " + id + ";");
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in deleteProduct(): ", e);
        }
    }

    static void deleteSection(int id) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement st = createStatement(conn);
            if (st != null) {
                deleteTestsBySection(id);
                st.executeQuery("DELETE FROM sections WHERE id = " + id + ";");
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in deleteSection(): ", e);
        }
    }

    static void deleteRow(int id) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement st = createStatement(conn);
            if (st != null) {
                st.executeQuery("DELETE FROM servers WHERE id = " + id + ";");
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in updateTableTitle(): ", e);
        }
    }

    static void deleteTest(int id) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement st = createStatement(conn);
            if (st != null) {
                st.executeQuery("DELETE FROM servers WHERE testID = " + id + ";");
                st.executeQuery("DELETE FROM tests WHERE id = " + id + ";");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in deleteTest(): ", e);
        }
    }

    private static void deleteTestsByProduct(int productID) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement st = createStatement(conn);
            if (st != null) {
                st.executeQuery("DELETE FROM servers WHERE testID IN (SELECT id FROM tests WHERE product = " + productID + ");");
                st.executeQuery("DELETE FROM tests WHERE product = " + productID + ";");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in deleteTestsByProduct(): ", e);
        }
    }

    private static void deleteTestsBySection(int sectionID) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement st = createStatement(conn);
            if (st != null) {
                st.executeQuery("DELETE FROM servers WHERE testID IN (SELECT id FROM tests WHERE section = " + sectionID + ");");
                st.executeQuery("DELETE FROM tests WHERE section = " + sectionID + ";");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL exception in deleteTestsBySection(): ", e);
        }
    }
}
