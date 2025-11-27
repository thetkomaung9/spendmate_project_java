package app;

import app.dao.BudgetDAO;
import app.dao.DBConnection;
import app.dao.TransactionDAO;
import app.service.BudgetService;
import app.service.TransactionService;
import app.ui.MainFrame;
import java.sql.Connection;
import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            System.out.println("Database initialized successfully: spendmate.db");

            TransactionDAO transactionDAO = new TransactionDAO();
            BudgetDAO budgetDAO = new BudgetDAO();
            TransactionService transactionService = new TransactionService(transactionDAO);
            BudgetService budgetService = new BudgetService(budgetDAO, transactionDAO);

            SwingUtilities.invokeLater(() -> {
                MainFrame frame = new MainFrame(transactionService, budgetService);
                frame.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
