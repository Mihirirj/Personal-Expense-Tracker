
package com.mihiri.expensetracker;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles all database operations for the expense tracker.
 * This class encapsulates all JDBC logic.
 */
public class DatabaseManager {
    // The database URL. For SQLite, this is the path to the file.
    private static final String DB_URL = "jdbc:sqlite:expenses.db";

    /**
     * Ensures the database and the 'expenses' table exist.
     * This method is called once when the application starts.
     */
    public void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS expenses ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " description TEXT NOT NULL,"
                + " category TEXT NOT NULL,"
                + " amount REAL NOT NULL,"
                + " date TEXT NOT NULL"
                + ");";

        // 'try-with-resources' statement ensures that each resource is closed at the end.
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    /**
     * Adds a new expense record to the database.
     * @param expense The Expense object to add.
     */
    public void addExpense(Expense expense) {
        String sql = "INSERT INTO expenses(description, category, amount, date) VALUES(?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // PreparedStatement is used to prevent SQL injection attacks.
            pstmt.setString(1, expense.getDescription());
            pstmt.setString(2, expense.getCategory());
            pstmt.setDouble(3, expense.getAmount());
            pstmt.setString(4, expense.getDate().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding expense: " + e.getMessage());
        }
    }

    /**
     * Retrieves all expenses from the database.
     * @return A list of all Expense objects.
     */
    public List<Expense> getAllExpenses() {
        String sql = "SELECT id, description, category, amount, date FROM expenses";
        List<Expense> expenses = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Loop through the result set and create Expense objects
            while (rs.next()) {
                expenses.add(new Expense(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getString("category"),
                        rs.getDouble("amount"),
                        LocalDate.parse(rs.getString("date"))
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving expenses: " + e.getMessage());
        }
        return expenses;
    }

    /**
     * Calculates the total expenses for each category for the current month.
     * @return A Map where the key is the category and the value is the total amount.
     */
    public Map<String, Double> getMonthlySummary() {
        // SQL query to sum amounts by category for the current month
        String sql = "SELECT category, SUM(amount) as total FROM expenses WHERE strftime('%Y-%m', date) = strftime('%Y-%m', 'now') GROUP BY category";
        Map<String, Double> summary = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                summary.put(rs.getString("category"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            System.out.println("Error generating summary: " + e.getMessage());
        }
        return summary;
    }
}