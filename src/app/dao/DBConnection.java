package app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database connection manager for SpendMate application.
 * Handles MySQL database initialization and connection pooling.
 * 
 * Database Schema:
 * - SpendMate_transactions: Stores all income and expense transactions
 * - SpendMate_budgets: Stores monthly budget limits
 * 
 * @author SpendMate Team
 */
public class DBConnection {

    // MySQL 서버 연결 정보
    private static final String HOST = "nsyun.synology.me";
    private static final String PORT = "3306";
    private static final String DATABASE = "db";
    private static final String USER = "user";
    private static final String PASSWORD = "user1234";
    
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + 
            "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul&characterEncoding=UTF-8";
    
    // 테이블 이름 (다른 팀과 중복되지 않도록 SpendMate_ 접두사 사용)
    public static final String TABLE_TRANSACTIONS = "SpendMate_transactions";
    public static final String TABLE_BUDGETS = "SpendMate_budgets";
    
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

            // Create transactions table with proper constraints (MySQL syntax)
            String sqlTx = "CREATE TABLE IF NOT EXISTS " + TABLE_TRANSACTIONS + " (" +
                    "`id` INT PRIMARY KEY AUTO_INCREMENT, " +
                    "`type` VARCHAR(10) NOT NULL, " +
                    "`date` VARCHAR(10) NOT NULL, " +
                    "`category` VARCHAR(50) NOT NULL, " +
                    "`amount` INT NOT NULL, " +
                    "`memo` VARCHAR(255), " +
                    "`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

            // Create budgets table with unique constraint on year_month (MySQL syntax)
            String sqlBudget = "CREATE TABLE IF NOT EXISTS " + TABLE_BUDGETS + " (" +
                    "`id` INT PRIMARY KEY AUTO_INCREMENT, " +
                    "`year_month` VARCHAR(7) NOT NULL UNIQUE, " +
                    "`limit_amt` INT NOT NULL, " +
                    "`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

            stmt.execute(sqlTx);
            stmt.execute(sqlBudget);

            // Create indexes for faster queries
            String idxTypeDate = "CREATE INDEX idx_spendmate_tx_type_date ON " + TABLE_TRANSACTIONS + "(`type`, `date`)";
            String idxCategory = "CREATE INDEX idx_spendmate_tx_category ON " + TABLE_TRANSACTIONS + "(`category`)";
            String idxDate = "CREATE INDEX idx_spendmate_tx_date ON " + TABLE_TRANSACTIONS + "(`date`)";
            
            // MySQL에서 인덱스가 이미 존재하면 예외 발생하므로 개별 처리
            try { stmt.execute(idxTypeDate); } catch (SQLException ignored) {}
            try { stmt.execute(idxCategory); } catch (SQLException ignored) {}
            try { stmt.execute(idxDate); } catch (SQLException ignored) {}

            System.out.println("MySQL Database initialized successfully!");
            System.out.println("Server: " + HOST + ":" + PORT + "/" + DATABASE);
            System.out.println("Tables: " + TABLE_TRANSACTIONS + ", " + TABLE_BUDGETS);

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    /**
     * Get a connection to the MySQL database.
     * 
     * @return A new database connection
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Ensure MySQL JDBC driver is loaded
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
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
     * Get the database name.
     * 
     * @return The database name
     */
    public static String getDatabaseName() {
        return DATABASE;
    }
    
    /**
     * Get the transactions table name.
     * 
     * @return The transactions table name
     */
    public static String getTransactionsTable() {
        return TABLE_TRANSACTIONS;
    }
    
    /**
     * Get the budgets table name.
     * 
     * @return The budgets table name
     */
    public static String getBudgetsTable() {
        return TABLE_BUDGETS;
    }
}
