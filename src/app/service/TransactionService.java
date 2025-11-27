package app.service;

import app.dao.TransactionDAO;
import app.model.Transaction;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionService {

    private final TransactionDAO transactionDAO;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TransactionService(TransactionDAO dao) {
        this.transactionDAO = dao;
    }

    public void addTransaction(String type, String date, String category, int amount, String memo) throws SQLException {
        if (!"income".equals(type) && !"expense".equals(type)) {
            throw new IllegalArgumentException("type must be income or expense");
        }
        Transaction t = new Transaction(type, date, category, amount, memo);
        transactionDAO.insert(t);
    }

    public List<Transaction> getMonthDetails(String yearMonth) throws SQLException {
        return transactionDAO.getDetailsOfMonth(yearMonth);
    }

    public List<Transaction> getDayDetails(LocalDate date) throws SQLException {
        return transactionDAO.getDetailsOfDay(date.format(dateFormatter));
    }

    public int getMonthIncome(String yearMonth) throws SQLException {
        return transactionDAO.getTotalIncomeOfMonth(yearMonth);
    }

    public int getMonthExpense(String yearMonth) throws SQLException {
        return transactionDAO.getTotalExpenseOfMonth(yearMonth);
    }

    public int getDayExpense(LocalDate date) throws SQLException {
        return transactionDAO.getTotalExpenseOfDay(date.format(dateFormatter));
    }
}
