package app;

import app.dao.BudgetDAO;
import app.dao.DBConnection;
import app.dao.TransactionDAO;
import app.dao.UserDAO;
import app.service.BudgetService;
import app.service.TransactionService;
import app.service.UserService;
import app.ui.LoginPanel;
import app.ui.MainFrame;
import java.sql.Connection;
import javax.swing.*;
import java.awt.*;

public class MainApp {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            System.out.println("Database initialized successfully: spendmate.db");

            TransactionDAO transactionDAO = new TransactionDAO();
            BudgetDAO budgetDAO = new BudgetDAO();
            UserDAO userDAO = new UserDAO();
            
            TransactionService transactionService = new TransactionService(transactionDAO);
            BudgetService budgetService = new BudgetService(budgetDAO, transactionDAO);
            UserService userService = new UserService(userDAO);

            SwingUtilities.invokeLater(() -> {
                // Create login frame
                JFrame loginFrame = new JFrame("SpendMate - Login");
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.setSize(500, 600);
                loginFrame.setMinimumSize(new Dimension(450, 550));
                loginFrame.setLocationRelativeTo(null);
                
                LoginPanel loginPanel = new LoginPanel(userService);
                loginPanel.setOnLoginSuccess(() -> {
                    loginFrame.dispose();
                    MainFrame mainFrame = new MainFrame(transactionService, budgetService);
                    mainFrame.setCurrentUser(userService.getCurrentUser());
                    mainFrame.setUserService(userService);
                    mainFrame.setVisible(true);
                });
                
                loginFrame.add(loginPanel);
                loginFrame.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
