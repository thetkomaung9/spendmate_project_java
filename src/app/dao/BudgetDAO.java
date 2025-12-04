package app.dao;

import app.model.Budget;
import java.sql.*;

public class BudgetDAO {

    private static final String TABLE = DBConnection.TABLE_BUDGETS;

    public Budget findByYearMonth(String yearMonth) throws SQLException {
        String sql = "SELECT * FROM " + TABLE + " WHERE `year_month` = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, yearMonth);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Budget(
                        rs.getInt("id"),
                        rs.getString("year_month"),
                        rs.getInt("limit_amt")
                );
            }
        }
        return null;
    }

    public void upsert(Budget b) throws SQLException {
        Budget existing = findByYearMonth(b.getYearMonth());
        if (existing == null) {
            String sql = "INSERT INTO " + TABLE + "(`year_month`, `limit_amt`) VALUES(?, ?)";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, b.getYearMonth());
                pstmt.setInt(2, b.getLimitAmt());
                pstmt.executeUpdate();
            }
        } else {
            String sql = "UPDATE " + TABLE + " SET `limit_amt`=? WHERE `year_month`=?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, b.getLimitAmt());
                pstmt.setString(2, b.getYearMonth());
                pstmt.executeUpdate();
            }
        }
    }
}
