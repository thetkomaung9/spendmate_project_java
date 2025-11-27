package app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database connection manager for SpendMate application.
 * Handles SQLite database initialization and connection pooling.
 * 
 * Database Schema:
 * - transactions: Stores all income and expense transactions
 * - budgets: Stores monthly budget limits
 * 
 * @author SpendMate Team
 */
public class DBConnection {

    private static final String URL = "jdbc:sqlite:spendmate.db";
    private static final String DB_NAME = "spendmate.db";
    
    // Static initializer to create database schema on first load
    static {
        initializeDatabase();
    }

    /**
     * Initialize the database schema and create required tables with indexes.
     * This method is called automatically when the class is loaded.
     */
    private static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Create transactions table with proper constraints
            String sqlTx = "CREATE TABLE IF NOT EXISTS transactions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "type TEXT NOT NULL CHECK(type IN ('income', 'expense'))," +
                    "date TEXT NOT NULL," +
                    "category TEXT NOT NULL," +
                    "amount INTEGER NOT NULL CHECK(amount >= 0)," +
                    "memo TEXT," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ");";

            // Create budgets table with unique constraint on year_month
            String sqlBudget = "CREATE TABLE IF NOT EXISTS budgets (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "year_month TEXT NOT NULL UNIQUE," +
                    "limit_amt INTEGER NOT NULL CHECK(limit_amt >= 0)," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ");";

            stmt.execute(sqlTx);
            stmt.execute(sqlBudget);

            // Create indexes for faster queries
            String idxTypeDate = "CREATE INDEX IF NOT EXISTS idx_tx_type_date ON transactions(type, date);";
            String idxCategory = "CREATE INDEX IF NOT EXISTS idx_tx_category ON transactions(category);";
            String idxDate = "CREATE INDEX IF NOT EXISTS idx_tx_date ON transactions(date);";
            
            stmt.execute(idxTypeDate);
            stmt.execute(idxCategory);
            stmt.execute(idxDate);

            System.out.println("Database initialized successfully: " + DB_NAME);

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    /**
     * Get a connection to the SQLite database.
     * 
     * @return A new database connection
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Ensure SQLite JDBC driver is loaded
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found", e);
        }
        return DriverManager.getConnection(URL);
    }

    /**
     * Test database connectivity.
     * 
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get the database file name.
     * 
     * @return The database file name
     */
    public static String getDatabaseName() {
        return DB_NAME;
    }
}
