package app.dao;

import app.model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public void insert(Transaction t) throws SQLException {
        String sql = "INSERT INTO transactions(type, date, category, amount, memo) VALUES(?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, t.getType());
            pstmt.setString(2, t.getDate());
            pstmt.setString(3, t.getCategory());
            pstmt.setInt(4, t.getAmount());
            pstmt.setString(5, t.getMemo());
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM transactions WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Transaction> findByPeriod(String fromDate, String toDate) throws SQLException {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE date BETWEEN ? AND ? ORDER BY date ASC, id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fromDate);
            pstmt.setString(2, toDate);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public int getTotalByType(String type, String fromDate, String toDate) throws SQLException {
        String sql = "SELECT IFNULL(SUM(amount),0) AS total FROM transactions " +
                     "WHERE type=? AND date BETWEEN ? AND ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, type);
            pstmt.setString(2, fromDate);
            pstmt.setString(3, toDate);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt("total") : 0;
        }
    }

    public int getTotalIncomeOfMonth(String yearMonth) throws SQLException {
        String sql = "SELECT IFNULL(SUM(amount),0) AS total FROM transactions " +
                     "WHERE type='income' AND date LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, yearMonth + "%");
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt("total") : 0;
        }
    }

    public int getTotalExpenseOfMonth(String yearMonth) throws SQLException {
        String sql = "SELECT IFNULL(SUM(amount),0) AS total FROM transactions " +
                     "WHERE type='expense' AND date LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, yearMonth + "%");
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt("total") : 0;
        }
    }

    public int getTotalExpenseOfDay(String date) throws SQLException {
        String sql = "SELECT IFNULL(SUM(amount),0) AS total FROM transactions " +
                     "WHERE type='expense' AND date=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt("total") : 0;
        }
    }

    public List<Transaction> getDetailsOfDay(String date) throws SQLException {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE date=? ORDER BY id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Transaction> getDetailsOfMonth(String yearMonth) throws SQLException {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE date LIKE ? ORDER BY date ASC, id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, yearMonth + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getInt("id"),
                rs.getString("type"),
                rs.getString("date"),
                rs.getString("category"),
                rs.getInt("amount"),
                rs.getString("memo")
        );
    }
}
